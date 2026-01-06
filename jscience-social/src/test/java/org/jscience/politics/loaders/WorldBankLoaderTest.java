package org.jscience.politics.loaders;

import org.jscience.politics.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WorldBankReader.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class WorldBankReaderTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    private WorldBankReader loader;

    @BeforeEach
    void setUp() {
        loader = new WorldBankReader(mockHttpClient);
    }

    @Test
    void testLoadAll_FromApi_Success() throws Exception {
        // Arrange
        String apiResponse = """
                [
                    {"page":1,"pages":1,"per_page":500,"total":2},
                    [
                        {
                            "id": "USA",
                            "iso2Code": "US",
                            "name": "United States",
                            "capitalCity": "Washington D.C.",
                            "region": {"value": "North America"},
                            "incomeLevel": {"value": "High income"}
                        },
                        {
                            "id": "FRA",
                            "iso2Code": "FR",
                            "name": "France",
                            "capitalCity": "Paris",
                            "region": {"value": "Europe & Central Asia"},
                            "incomeLevel": {"value": "High income"}
                        }
                    ]
                ]
                """;

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(apiResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Act
        List<Country> countries = loader.loadAll();

        // Assert
        assertNotNull(countries);
        assertEquals(2, countries.size());

        Country usa = countries.stream().filter(c -> "USA".equals(c.getAlpha3())).findFirst().orElse(null);
        assertNotNull(usa);
        assertEquals("United States", usa.getName());
        assertEquals("US", usa.getAlpha2());
        assertEquals("Washington D.C.", usa.getCapital());

        Country france = countries.stream().filter(c -> "FRA".equals(c.getAlpha3())).findFirst().orElse(null);
        assertNotNull(france);
        assertEquals("France", france.getName());
        assertEquals("Paris", france.getCapital());
    }

    @Test
    void testLoadAll_ApiFailure_FallbackToResource() throws Exception {
        // Arrange - API returns error
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Act
        List<Country> countries = loader.loadAll();

        // Assert - Should fallback to sample data
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    @Test
    void testLoadAll_ApiException_FallbackToResource() throws Exception {
        // Arrange - API throws exception
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Network error"));

        // Act
        List<Country> countries = loader.loadAll();

        // Assert - Should fallback to sample data
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    @Test
    void testLoadAll_Caching() throws Exception {
        // Arrange
        String apiResponse = """
                [
                    {"page":1,"pages":1,"per_page":500,"total":1},
                    [{"id": "DEU", "iso2Code": "DE", "name": "Germany", "region": {"value": "Europe"}}]
                ]
                """;

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(apiResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Act - Call twice
        List<Country> first = loader.loadAll();
        List<Country> second = loader.loadAll();

        // Assert - API should only be called once due to caching
        verify(mockHttpClient, times(1)).send(any(), any());
        assertSame(first, second);
    }

    /*
     * @Test
     * void testClearCache() throws Exception {
     * // Cache clearing is now managed by the framework/AbstractLoader and is not
     * exposed directly for manual clearing in the same way.
     * // Test disabled as implementation details of AbstractLoader cache clearing
     * are opaque here.
     * }
     */

    @Test
    void testFetchIndicatorData_Success() throws Exception {
        // Arrange
        String indicatorResponse = """
                [
                    {"page":1,"pages":1},
                    [
                        {"date": "2023", "value": 25000000000000},
                        {"date": "2022", "value": 24500000000000}
                    ]
                ]
                """;

        HttpResponse<String> mockIndicatorResponse = mock(HttpResponse.class);
        when(mockIndicatorResponse.statusCode()).thenReturn(200);
        when(mockIndicatorResponse.body()).thenReturn(indicatorResponse);

        CompletableFuture<HttpResponse<String>> future = CompletableFuture.completedFuture(mockIndicatorResponse);
        when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(future);

        // Act
        Map<String, Double> data = loader.fetchIndicatorData("USA", "NY.GDP.MKTP.CD").join();

        // Assert
        assertNotNull(data);
        assertEquals(2, data.size());
        assertEquals(25000000000000.0, data.get("2023"));
        assertEquals(24500000000000.0, data.get("2022"));
    }

    @Test
    void testFetchIndicatorData_EmptyResponse() throws Exception {
        // Arrange
        String indicatorResponse = """
                [
                    {"page":1,"pages":1},
                    []
                ]
                """;

        HttpResponse<String> mockIndicatorResponse = mock(HttpResponse.class);
        when(mockIndicatorResponse.statusCode()).thenReturn(200);
        when(mockIndicatorResponse.body()).thenReturn(indicatorResponse);

        CompletableFuture<HttpResponse<String>> future = CompletableFuture.completedFuture(mockIndicatorResponse);
        when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(future);

        // Act
        Map<String, Double> data = loader.fetchIndicatorData("XXX", "INVALID").join();

        // Assert
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    void testLoad_DelegatesToLoadAll() throws Exception {
        // Arrange
        String apiResponse = """
                [
                    {"page":1,"pages":1,"per_page":500,"total":1},
                    [{"id": "CHN", "iso2Code": "CN", "name": "China", "region": {"value": "East Asia"}}]
                ]
                """;

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(apiResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Act
        Country country = loader.load("CHN");

        // Assert
        assertNotNull(country);
        assertEquals("China", country.getName());
    }

    @Test
    void testGetResourcePath() {
        assertEquals("/org/jscience/politics/worldbank-fallback.json", loader.getResourcePath());
    }

    @Test
    void testGetResourceType() {
        assertEquals(Country.class, loader.getResourceType());
    }

    @Test
    void testGetInstance_Singleton() {
        WorldBankReader instance1 = WorldBankReader.getInstance();
        WorldBankReader instance2 = WorldBankReader.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testLoadAll_SkipsAggregates() throws Exception {
        // Arrange - Response includes aggregates that should be skipped
        String apiResponse = """
                [
                    {"page":1,"pages":1,"per_page":500,"total":3},
                    [
                        {"id": "WLD", "iso2Code": "1W", "name": "World", "region": {"value": "Aggregates"}},
                        {"id": "HIC", "iso2Code": "XD", "name": "High income", "region": {"value": "Aggregates"}},
                        {"id": "GBR", "iso2Code": "GB", "name": "United Kingdom", "region": {"value": "Europe"}}
                    ]
                ]
                """;

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(apiResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Act
        List<Country> countries = loader.loadAll();

        // Assert - Only UK should be included, aggregates skipped
        assertEquals(1, countries.size());
        assertEquals("United Kingdom", countries.get(0).getName());
    }
}
