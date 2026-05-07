# Z-Anatomy 3D Human Body Models

This directory contains professional 3D anatomical models from the **Z-Anatomy** project (<https://github.com/LluisV/Z-Anatomy>), an open-source anatomical atlas.

## Downloaded Models (FBX Format)

- **SkeletalSystem100.fbx** - Complete skeletal system
- **MuscularSystem100.fbx** - Muscular system
- **VisceralSystem100.fbx** - Visceral organs (digestive, respiratory, etc.)
- **CardioVascular41.fbx** - Cardiovascular system (heart, blood vessels)
- **NervousSystem100.fbx** - Nervous system (brain, spinal cord, nerves)

## Important: Conversion Required

JavaFX does not natively support FBX format. To use these models in `HumanBodyViewer`:

1. **Install Blender** (<https://www.blender.org/download/>)
2. **Open each FBX file in Blender**
3. **Export as OBJ**:
   - File → Export → Wavefront (.obj)
   - Enable "Write Materials"
   - Save as `<SystemName>.obj`
4. **Place OBJ files in this directory**

## Usage in HumanBodyViewer

Once OBJ files are created, the viewer will auto-load them into the corresponding layers:

- Skeleton layer ← `SkeletalSystem100.obj`
- Muscle layer ← `MuscularSystem100.obj`
- Organs layer ← `VisceralSystem100.obj`
- Circulatory layer ← `CardioVascular41.obj`
- Nervous layer ← `NervousSystem100.obj`

## License

Z-Anatomy models are licensed under **CC BY-SA 4.0** (Creative Commons Attribution-ShareAlike 4.0 International).

Attribution: Z-Anatomy (<https://z-anatomy.net/>) based on BodyParts3D.
