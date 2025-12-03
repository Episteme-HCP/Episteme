# Resource Files - License & Attribution

This document tracks licenses and attributions for all resource files in the JScience library.

## Summary

**Total Resources**: 104 files  
**Licensed Content**: 4 files requiring attribution  
**Public Domain**: 66 image files (constellations - public domain astronomical data)  
**Free Use**: Historical/geographic data compilations  

---

## Licensed Resources (Require Attribution)

### 1. Sciences Taxonomy
- **File**: `sciences.xml`
- **Source**: Stephen Chrisomalis
- **License**: Free for personal use with attribution
- **Copyright**: © 2004 Stephen Chrisomalis
- **URL**: http://phrontistery.50megs.com
- **Contact**: forthright@sympatico.ca
- **Terms**: "Links to this page may be made without permission. You may download a copy of this page for your personal use, provided that it is not modified in any way and this message is present in full on any print or electronic copies."
- **Attribution in Code**: ✅ `SciencesDatabase.java`

### 2. Nobel Prize Winners
- **File**: `history/nobelwinners.xml`
- **Source**: Nobel Foundation / Public compilation
- **License**: Facts are public domain, compilation may have terms
- **Status**: ⚠️ **NEEDS VERIFICATION** - Check nobelprize.org terms

### 3. Fields Medal Winners
- **File**: `history/fieldswinners.xml`
- **Source**: International Mathematical Union
- **License**: Public factual data
- **Status**: ✅ Facts are public domain

### 4. CODATA Physical Constants
- **File**: Java code `PhysicalConstants.java`
- **Source**: CODATA / NIST
- **License**: Public domain (US Government work)
- **URL**: https://physics.nist.gov/cuu/Constants/
- **Attribution**: ✅ Cited in Javadoc

---

## Public Domain / Free Use

### Geographic Data
- `politics/iso3166.txt` - ISO country codes (public standard)
- `politics/timezones.xml` - Timezone data (public compilation)
- `geography/worldheritage.txt` - UNESCO list (public data)

### Historical Data
- `history/chronology*.txt` - Historical timelines (factual public domain)
- `history/biographies.txt` - Public figures (factual)
- `history/inventions.txt` - Historical facts (public domain)
- `history/sciencediscoveries.xml` - Scientific history (factual)

### Images (66 files)
- Constellation maps (`astronomy/solarsystem/constellations/images/*.png`)
  - **Status**: Public domain astronomical data
  - **Source**: Standard constellation maps
- Paleomaps (`geography/paleomaps/*.jpg`)
  - **License**: Check `geography/paleomaps/license.txt`
  - **Status**: ⚠️ **NEEDS REVIEW**
- Traffic simulation UI (`architecture/traffic/images/*.gif`)
  - **Status**: Part of JScience codebase

### Psychology/Sociology
- `psychology/dsmiv.xml` - Diagnostic classifications
  - **Status**: ⚠️ **NEEDS VERIFICATION** - APA copyright
  - **Recommendation**: Use for educational purposes only or replace with ICD-11 (WHO, public)

---

## Resources NOT Requiring Attribution

- Package documentation (`package.html` files)
- Build scripts (`build.xml`)
- Configuration files
- Coding standards (`javacodingrecommendations.txt`)
- TODO lists (`todo.txt`)

---

## Action Items

### Immediate (Legal Compliance)
1. ✅ **sciences.xml**: Attribution added to `SciencesDatabase.java`
2. ⚠️ **nobelwinners.xml**: Verify nobelprize.org terms of use
3. ⚠️ **dsmiv.xml**: Check APA copyright, consider replacing with ICD-11
4. ⚠️ **paleomaps**: Review license.txt file

### Recommended (Best Practices)
5. Add LICENSE.txt to resources/ directory
6. Create ATTRIBUTIONS.md with all credits
7. Add license headers to parser classes
8. Document data sources in Javadoc for all database classes

---

## External Data Sources (API Connectors)

For data too large to bundle:

### 1. Biological Taxonomy
- **GBIF API**: Global Biodiversity Information Facility
  - URL: https://www.gbif.org/developer/species
  - License: CC0 / CC-BY (varies by dataset)
  - No bundling needed: fetch on demand

### 2. Chemical Data
- **PubChem API**: NIH/NLM
  - URL: https://pubchem.ncbi.nlm.nih.gov/docs/pug-rest
  - License: Public domain (US Government)
  - No bundling needed: fetch on demand

### 3. Astronomical Data
- **NASA APIs**: Various datasets
  - License: Public domain (US Government)
  - No bundling needed: fetch on demand

### 4. Physical Constants
- **NIST API**: Latest CODATA values
  - URL: https://physics.nist.gov/cuu/Constants/
  - License: Public domain (US Government)
  - Currently bundled in code (small dataset)

---

## Compliance Checklist

- [x] Identify all third-party data
- [x] Document sources and licenses
- [ ] Verify Nobel Prize data terms
- [ ] Review DSM-IV usage (consider replacement)
- [ ] Check paleomap licenses
- [x] Add attribution to code (sciences.xml)
- [ ] Create LICENSE.txt for resources
- [ ] Create ATTRIBUTIONS.md

---

**Last Updated**: 2025-11-28  
**Reviewed By**: Silvere Martin-Michiellot  
**Status**: In Progress - 4 items need verification
