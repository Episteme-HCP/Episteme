# JScience Data Resources

## Overview

This directory contains fallback data resources used when external APIs are unavailable or during offline operation.

---

## Files

### 1. worldbank-fallback.json

**Purpose**: Fallback country data when World Bank API is unavailable

**Schema**:

```json
{
  "metadata": {
    "source": "string - Data source identifier",
    "version": "string - Data version",
    "lastUpdated": "string - ISO date when data was last updated",
    "description": "string - Human-readable description"
  },
  "countries": [
    {
      "code": "string - ISO 3166-1 alpha-3 country code",
      "name": "string - Official country name",
      "region": "string - World Bank region classification",
      "incomeLevel": "string - World Bank income level",
      "capital": "string - Capital city name",
      "longitude": "string - Decimal longitude",
      "latitude": "string - Decimal latitude",
      "population": "number - Total population",
      "areaSqKm": "number - Land area in square kilometers",
      "gdpUsd": "number - GDP in current USD",
      "gdpPerCapitaUsd": "number - GDP per capita in current USD",
      "currency": "string - ISO 4217 currency code"
    }
  ]
}
```

**Usage**:

```java
WorldBankLoader loader = new WorldBankLoader();
List<Region> countries = loader.loadFromResource();
```

**Update Procedure**:

1. Fetch latest data from World Bank API
2. Export to JSON format following schema above
3. Validate JSON structure
4. Update `lastUpdated` field in metadata
5. Replace this file

---

### 2. CIA World Factbook Data

**Location**: Loaded from `/factbook/*.xml`

**Format**: CIA World Factbook XML export

**Fields Extracted**:

- Country name and codes (ISO 2/3, numeric)
- Capital city
- Area (square kilometers)
- Population
- Government type
- Geographic region/continent

**Usage**:

```java
FactbookLoader loader = new FactbookLoader();
try (InputStream is = getClass().getResourceAsStream("/factbook/countries.xml")) {
    List<Country> countries = loader.load(is);
}
```

---

## Data Sources

### World Bank Open Data

- **URL**: <https://data.worldbank.org>
- **API**: <https://api.worldbank.org/v2>
- **License**: Creative Commons Attribution 4.0 (CC-BY 4.0)
- **Update Frequency**: Annually for most indicators

### CIA World Factbook

- **URL**: <https://www.cia.gov/the-world-factbook/>
- **Format**: XML exports available
- **License**: Public domain (U.S. Government work)
- **Update Frequency**: Varies by field

---

## Cache Management

### World Bank Indicator Cache

**Configuration**:

```properties
worldbank.cache.enabled=true
worldbank.cache.ttl.hours=24
```

**Cache Location**: In-memory (WorldBankLoader)

**Clear Cache**:

```java
WorldBankLoader loader = new WorldBankLoader();
loader.clearCache();
```

---

## Data Quality

### Validation Rules

1. **Country Codes**: Must be valid ISO 3166-1 alpha-3
2. **Population**: Must be positive number
3. **Area**: Must be positive number
4. **GDP**: Can be null for unavailable data
5. **Coordinates**: Must be valid WGS84 decimal degrees

### Known Issues

- Some small territories may be missing
- GDP data may be outdated for volatile economies
- Population figures are estimates

---

## Maintenance

### Regular Tasks

- **Monthly**: Check for World Bank API updates
- **Quarterly**: Verify fallback data accuracy
- **Annually**: Full data refresh from sources

### Testing

```bash
# Validate JSON schema
mvn test -Dtest=WorldBankLoaderTest

# Test fallback loading
mvn test -Dtest=DataLoadingIT
```

---

## Future Enhancements

### Planned

1. Add more countries to fallback data (current: 10, target: 50+)
2. Include additional economic indicators
3. Add historical data support
4. Implement delta updates

### Wishlist

- Real-time indicator subscriptions
- GraphQL query support
- Data versioning system
- Automated update pipeline

---

## Schema Versions

### v1.0 (2026-01-01)

- Initial schema with 10 countries
- Basic economic indicators
- Geographic coordinates

---

*Last updated: 2026-01-01*  
*Maintained by: JScience Development Team*
