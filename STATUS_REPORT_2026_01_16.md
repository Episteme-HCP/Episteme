# Rapport d'État du Projet JScience

**Date:** 16 Janvier 2026
**Objet:** Analyse de conformité des composants (Viewers, Demos, Readers, Writers)

Ce rapport détaille l'état actuel du projet par rapport aux directives de refactoring demandées.

## Résumé Exécutif

L'audit révèle une conformité partielle. Le module **Mandelbrot** (Viewer/Demo) sert d'exemple de référence (Gold Standard). Cependant, la majorité des autres modules présentent des violations significatives, notamment concernant l'internationalisation (I18N), l'exposition des paramètres, et l'usage de valeurs "en dur".

## 1. Viewers vs "Reusable Panels" et Paramètres

La directive impose que chaque Viewer expose ses paramètres via `getViewerParameters()` et que la Démo ne soit qu'un conteneur de présentation.

| Composant | État | Analyse |
| :--- | :---: | :--- |
| **MandelbrotViewer** | ✅ Conforme | Implémente `getViewerParameters` avec `NumericParameter`. Utilise `jscience.properties` pour les défauts. Usage de `Real`. |
| **SpectrographViewer** | ⚠️ Partiel | Expose des paramètres (`Sensitivity`, `Mode`). Mais contient des valeurs en dur (`BANDS = 128`, couleurs). Mode "Scientifique" implémenté (Real vs Primitive). |
| **SportsResultsViewer** | ✅ Conforme | `getViewerParameters` expose désormais la liste des équipes. Configurations externalisées dans `jscience.properties`. |
| **TimelineViewer** | ✅ Conforme | `getViewerParameters` expose `StartYear` et `EndYear`. Support I18N ajouté pour les catégories et noms. |
| **Autres Viewers** | ⚠️ Suspects | La plupart implémentent l'interface mais beaucoup retournent des listes vides ou incomplètes, gardant la logique de configuration en dur. |

## 2. Internationalisation (I18N) des Viewers, Readers, Writers

La directive impose l'usage systématique de l'I18N pour `getCategory`, `getName`, `getDescription`. Pas de chaînes en dur.

| Composant | État | Analyse |
| :--- | :---: | :--- |
| **Core Viewers** | ✅ Conforme | `MatrixViewer`, `MandelbrotViewer`, `FunctionExplorer`, `GeometryBoard` utilisent désormais `I18n.get`. |
| **Natural Viewers** | ✅ Conforme | `VizieRReader`, `SpectrographViewer`, `MagneticFieldViewer`, `CircuitViewer` utilisent désormais `I18n.get`. |
| **Social Viewers** | ✅ Conforme | `TimelineViewer`, `CarTrafficViewer`, `SportsViewer` utilisent désormais des catégories internationalisées (`Social Sciences`, `History`, `Sports`). |
| **Readers/Writers** | ⚠️ Mixte | Les Readers principaux sont corrigés, mais une vérification exhaustive reste à faire pour les moins utilisés. |

## 3. Usage de `Real` vs `Double`

La directive impose l'usage interne de `Real` et la conversion Double -> Real en entrée.

| Composant | État | Analyse |
| :--- | :---: | :--- |
| **MandelbrotViewer** | ✅ Conforme | Inputs Double convertis en Real. Calculs internes en Real. |
| **SportsResultsViewer** | ✅ Conforme | Utilise `List<Real>` pour `pointsHistory`. |
| **SpectrographViewer** | ⚠️ Partiel | Utilise `double[]` pour la FFT (performance?). Possède un mode switchable mais le moteur par défaut est primitif. |
| **TimelineViewer** | ⚠️ Partiel | Utilise encore des `double` pour l'affichage, mais les bornes sont configurables. Refactoring `Real` complet reporté. |
| **VizieRReader** | ✅ Conforme | Méthodes surchargées acceptant `Real` et `double`. |

## 4. Configuration et Valeurs par Défaut

La directive impose: "0 hardcoded values ! Toutes les valeurs par défaut doivent venir de jscience.properties".

| Composant | État | Analyse |
| :--- | :---: | :--- |
| **MandelbrotViewer** | ✅ Conforme | Lit `viewer.mandelbrot.default.*` depuis les propriétés. |
| **SpectrographViewer** | ✅ Conforme | `BANDS` et couleurs sont désormais configurables via `jscience.properties`. Paramètre `bands` exposé. |
| **SportsResultsViewer** | ✅ Conforme | Equipes par défaut externalisées dans `viewer.sports.default.teams`. |
| **TimelineViewer** | ✅ Conforme | Années min/max externalisées dans `viewer.timeline.default.start/end`. |
| **Readers (Natural)** | ✅ Conforme | `PubChemReader`, `CIFReader`, `FASTAReader`, `IUPACGoldBookReader`, `ChemistryDataReader` utilisent `I18n.get`. |
| **Readers (Natural) - Batch 2** | ✅ Conforme | `NCBITaxonomyReader`, `GBIFSpeciesReader`, `VirusReader`, `StlMeshReader`, `ICD10Reader`, `DrugBankReader`, `ChEBIReader`, `AcidBaseReader`, `HorizonsEphemerisReader`, `PDBReader`, `UniProtReader`. |
| **Readers (Social)** | ✅ Conforme | `FactbookReader`, `CSVTimeSeriesReader`, `GoogleElevationReader`, `GeoJSONReader`, `ETOPOElevationReader`, `FinancialMarketReader`. |

## Liste des Éléments ne respectant pas les règles (Échantillon représentatif - MIS A JOUR)

1. **Hardcoded Category Strings**:
   * *(CORRIGÉ)* Tous les Readers dans jscience-natural et jscience-social.

2. **Hardcoded Parameters**:
   * *(CORRIGÉ)* `SportsResultsViewer`: Liste d'équipes.
   * *(CORRIGÉ)* `TimelineViewer`: Bornes temporelles.
   * *(CORRIGÉ)* `SpectrographViewer`: Bands désormais configurable.

3. **Manque d'exposition des paramètres**:
   * *(CORRIGÉ)* `SportsResultsViewer`.
   * *(CORRIGÉ)* `TimelineViewer`.

4. **Manque d'I18N**:
   * *(CORRIGÉ)* Tous les Readers (22 au total).
   * *(CORRIGÉ)* Nouvelles catégories: `politics`, `history`, `geography`, `economics`, `medicine`, `biology`, `chemistry`, `physics`.

## Recommandations Immédiates

1. **Traductions réelles**: Remplacer les placeholders anglais dans FR/DE/ES/ZH par de vraies traductions.
2. **Conversion Real**: Migrer `TimelineViewer` et `SpectrographViewer` vers `Real` pour calculs internes.
3. **Build Validation**: Exécuter `mvn clean compile` pour valider toutes les modifications I18N.

## 5. Nomenclature Audit Progress (I18n Keys)

### Module: jscience-core

* ✅ **MandelbrotViewer/Demo**: Keys updated to `viewer.mandelbrotviewer.*` and `demo.mandelbrotdemo.*`. Defaults added.
* ✅ **LorenzViewer/Demo**: Keys updated to `viewer.lorenzviewer.*` and `demo.lorenzdemo.*`. Defaults added.
* ✅ **MatrixViewer/Demo**: Keys updated to `viewer.matrixviewer.*` and `demo.matrixdemo.*`. Defaults added.
* ✅ **UnitConverterViewer/Demo**: Keys updated to `viewer.unitconverterviewer.*` and `demo.unitconverterdemo.*`. Defaults added.
* ✅ **FunctionExplorer2DViewer**: Keys updated to `viewer.functionexplorer2dviewer.*`. Defaults added.
* ✅ **GeometryBoardViewer**: Keys updated to `viewer.geometryboardviewer.*`. Defaults added.
* ✅ **MetamathViewer**: Keys updated to `viewer.metamathviewer.*`. Defaults added.
* ✅ **messages_core_en.properties**: Updated to reflect all changes.

### Module: jscience-natural

* ✅ **SpectrographViewer**: Keys updated to `viewer.spectrographviewer.*`. Defaults added.
* ✅ **SpectrographDemo**: Keys updated to `demo.spectrographdemo.*`. Defaults added.

### Module: jscience-social

* ✅ **KurzweilDemo**: Keys updated to `demo.kurzweildemo.*`. Defaults added.
* ✅ **TimelineViewer**: Keys updated to `viewer.timelineviewer.*`. Defaults added.
* ✅ **PoliticsVotingDemo**: Keys updated to `demo.politicsvotingdemo.*`. Defaults added.
* ✅ **SociologyNetworkDemo**: Keys updated to `demo.sociologynetworkdemo.*`. Defaults added.
* ✅ **LinguisticsWordFreqDemo**: Keys updated to `demo.linguisticswordfreqdemo.*`. Defaults added.
* ✅ **EconomicsGDPDemo**: Keys updated to `demo.economicsgdpdemo.*`. Defaults added.
* ✅ **ArchitectureStabilityDemo**: Keys updated to `demo.architecturestabilitydemo.*`. Defaults added.

### Module: jscience-featured-apps

* ✅ **Launcher**: Keys updated to `app.launcher.*`. Bundle path corrected.

### Module: jscience-client

* ✅ **DistributedWhiteboardApp**: Keys updated to `app.distributedwhiteboardapp.*`. Defaults added.
* ✅ **DistributedDataLakeApp**: Keys updated to `app.distributeddatalakeapp.*`. Defaults added.
* ✅ **DistributedGridMonitorApp**: Keys updated to `app.distributedgridmonitorapp.*`. Defaults added.
* ✅ **All other client apps**: Already compliant with `app.distributedXXX.*` nomenclature.
