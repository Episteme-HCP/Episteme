# Anatomical 3D Models for HumanBodyViewer

This directory is intended for professional anatomical 3D models (STL/OBJ format).

## Recommended Free Sources (CC-BY-SA Licensed)

### 1. BodyParts3D (RIKEN/DBCLS) - OBJ Format

**Direct Download**: <https://lifesciencedb.jp/bp3d/>

- Over 2,000 anatomical structures in Wavefront OBJ format
- Used as the base for Z-Anatomy project
- License: CC-BY-SA 2.1 Japan

### 2. Z-Anatomy - Blender/.blend Format

**GitHub**: <https://github.com/LluisV/Z-Anatomy>

- 5,000+ anatomical structures
- Requires Blender to export to OBJ/STL
- License: CC-BY-SA 4.0

### 3. embodi3D.com - STL Format

**Website**: <https://www.embodi3d.com/files/category/4-bone/>

- 10,000+ bone models derived from CT scans
- Free registration required
- Includes: skull, spine, pelvis, extremities

### 4. NIH 3D Print Exchange

**Website**: <https://3d.nih.gov/>

- Medical models in STL/GLB format
- Free for educational use

## Usage in HumanBodyViewer

1. Download STL files from above sources
2. Place them in this directory
3. In HumanBodyViewer, click "Load STL File..." button
4. Select the downloaded model

## Recommended Files to Download

| Structure | Source | Suggested File |
|-----------|--------|----------------|
| Full Skeleton | embodi3D | human_skeleton.stl |
| Skull | embodi3D | skull_detailed.stl |
| Heart | embodi3D | heart.stl |
| Brain | NIH 3D | brain.stl |
| Lungs | BodyParts3D | lungs.obj |
| Spine | embodi3D | spine_complete.stl |

## File Naming Convention

For auto-loading (future enhancement):

- `skeleton_*.stl` - Skeletal system
- `muscle_*.stl` - Muscular system
- `organ_*.stl` - Internal organs
- `nervous_*.stl` - Nervous system
- `circulatory_*.stl` - Circulatory system
