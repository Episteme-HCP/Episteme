/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.server.server.service;

import org.episteme.server.server.model.ConfigProperty;
import org.episteme.server.server.repository.ConfigPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing runtime configuration properties.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Service
public class ConfigurationService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationService.class);

    private final ConfigPropertyRepository repository;

    @Autowired
    public ConfigurationService(ConfigPropertyRepository repository) {
        this.repository = repository;
    }

    /**
     * Get a property value, falling back to the provided default if not found in database.
     */
    public String getProperty(String key, String defaultValue) {
        return repository.findById(key)
                .map(ConfigProperty::getValue)
                .orElse(defaultValue);
    }

    /**
     * Set a property value and persist it.
     */
    public void setProperty(String key, String value, String description) {
        LOG.info("Updating dynamic config: {} = {}", key, value);
        ConfigProperty property = repository.findById(key)
                .orElse(new ConfigProperty(key, value, description));
        property.setValue(value);
        if (description != null) property.setDescription(description);
        repository.save(property);
    }

    /**
     * Delete a property (reverting to file-based or hardcoded default).
     */
    public void deleteProperty(String key) {
        LOG.info("Deleting dynamic config: {}", key);
        repository.deleteById(key);
    }
}
