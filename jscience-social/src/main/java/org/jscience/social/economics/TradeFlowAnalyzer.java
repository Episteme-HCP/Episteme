/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.social.economics;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Trade flow analysis using gravity model and related methods.
 */
public final class TradeFlowAnalyzer {

    private TradeFlowAnalyzer() {}

    public record Country(
        String code,
        String name,
        Real gdp,
        Real population,
        double latitude,
        double longitude
    ) {}

    public record TradeFlow(
        Country exporter,
        Country importer,
        Real value,
        int year
    ) {}

    public record GravityModelResult(
        Country origin,
        Country destination,
        Real predictedFlow,
        Real actualFlow,
        Real residual
    ) {}

    /**
     * Gravity model prediction for bilateral trade.
     * Tij = G Ã— (Yi Ã— Yj) / Dij^Î²
     * 
     * @param exporter Exporting country.
     * @param importer Importing country.
     * @param gravityConstant G - scaling constant.
     * @param distanceExponent Î² - typically around 1.
     * @return Predicted trade flow.
     */
    public static Real predictGravityModel(Country exporter, Country importer,
            Real gravityConstant, double distanceExponent) {
        
        double distance = haversineDistance(
            exporter.latitude(), exporter.longitude(),
            importer.latitude(), importer.longitude()
        );
        
        // Avoid division by zero for same-country
        distance = Math.max(distance, 100);
        
        Real gdpProduct = exporter.gdp().multiply(importer.gdp());
        Real distanceFactor = Real.of(Math.pow(distance, distanceExponent));
        
        return gravityConstant.multiply(gdpProduct).divide(distanceFactor);
    }

    /**
     * Extended gravity model with additional factors.
     */
    public static Real predictExtendedGravity(Country exporter, Country importer,
            Real gravityConstant, double distanceExponent,
            boolean commonLanguage, boolean commonBorder, boolean tradeAgreement) {
        
        Real basePrediction = predictGravityModel(exporter, importer, 
            gravityConstant, distanceExponent);
        
        double multiplier = 1.0;
        if (commonLanguage) multiplier *= 1.5;
        if (commonBorder) multiplier *= 2.0;
        if (tradeAgreement) multiplier *= 1.8;
        
        return basePrediction.multiply(Real.of(multiplier));
    }

    /**
     * Calculates trade openness (trade as % of GDP).
     */
    public static Real tradeOpenness(Real exports, Real imports, Real gdp) {
        return exports.add(imports).divide(gdp).multiply(Real.of(100));
    }

    /**
     * Revealed Comparative Advantage (Balassa Index).
     * RCA = (Xij/Xi) / (Xwj/Xw)
     */
    public static Real revealedComparativeAdvantage(
            Real countryProductExports,
            Real countryTotalExports,
            Real worldProductExports,
            Real worldTotalExports) {
        
        Real countryShare = countryProductExports.divide(countryTotalExports);
        Real worldShare = worldProductExports.divide(worldTotalExports);
        
        return countryShare.divide(worldShare);
    }

    /**
     * Herfindahl-Hirschman Index for export concentration.
     */
    public static Real exportConcentration(Map<String, Real> exportsByProduct) {
        Real totalExports = exportsByProduct.values().stream()
            .reduce(Real.ZERO, Real::add);
        
        double hhi = 0;
        for (Real productExport : exportsByProduct.values()) {
            double share = productExport.divide(totalExports).doubleValue();
            hhi += share * share;
        }
        
        return Real.of(hhi);
    }

    /**
     * Intra-industry trade index (Grubel-Lloyd).
     * GL = 1 - |X - M| / (X + M)
     */
    public static Real grubelLloydIndex(Real exports, Real imports) {
        Real sum = exports.add(imports);
        if (sum.compareTo(Real.ZERO) == 0) return Real.ZERO;
        
        Real diff = exports.subtract(imports).abs();
        return Real.of(1).subtract(diff.divide(sum));
    }

    /**
     * Terms of trade.
     * ToT = Export Price Index / Import Price Index Ã— 100
     */
    public static Real termsOfTrade(Real exportPriceIndex, Real importPriceIndex) {
        return exportPriceIndex.divide(importPriceIndex).multiply(Real.of(100));
    }

    /**
     * Analyzes bilateral trade matrix.
     */
    public static Map<String, Map<String, Real>> buildTradeMatrix(List<TradeFlow> flows) {
        Map<String, Map<String, Real>> matrix = new HashMap<>();
        
        for (TradeFlow flow : flows) {
            matrix
                .computeIfAbsent(flow.exporter().code(), k -> new HashMap<>())
                .merge(flow.importer().code(), flow.value(), Real::add);
        }
        
        return matrix;
    }

    /**
     * Finds top trading partners.
     */
    public static List<Map.Entry<String, Real>> topTradingPartners(
            List<TradeFlow> flows, String countryCode, int limit) {
        
        Map<String, Real> partners = new HashMap<>();
        
        for (TradeFlow flow : flows) {
            if (flow.exporter().code().equals(countryCode)) {
                partners.merge(flow.importer().code(), flow.value(), Real::add);
            }
            if (flow.importer().code().equals(countryCode)) {
                partners.merge(flow.exporter().code(), flow.value(), Real::add);
            }
        }
        
        return partners.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(limit)
            .toList();
    }

    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}

