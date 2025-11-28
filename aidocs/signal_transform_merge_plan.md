# Signal/Transform Package Merge - Execution Plan

**Date**: 2025-11-27  
**Phase**: 1 - Critical Fixes  
**Status**: Analysis Complete → Ready for Execution

---

## 📊 Current State Analysis

### signal/  
- `FastFourierTransform.java` - Cooley-Tukey FFT (recursive, power-of-2)
- `Wavelets.java` - Haar wavelet transform

### analysis/transform/
- `DiscreteFourierTransform.java` - DFT using FFT (implements Transform interface, VectorFunction)
- `Transform.java` - Interface (extends Bijection)

---

## ✅ Finding: NO Duplicates!

**Initial Concern**: "FourierTransform vs FastFourierTransform duplicate"

**Reality**: 
- signal/**FastFourierTransform** - Static utility methods (Cooley-Tukey algorithm)
- transform/**DiscreteFourierTransform** - Class implements Transform/VectorFunction interfaces

**Relationship**: DiscreteFourierTransform could/should **use** FastFourierTransform internally!

---

## 🎯 Merge Strategy

### Option A: Keep Both, Make DFT Use FFT ✅ RECOMMENDED

```
analysis/transform/
├── Transform.java (interface)
├── DiscreteFourierTransform.java (uses FastFourierTransform internally)
├── FastFourierTransform.java (← MOVE from signal, algorithm implementation)
└── WaveletTransform.java (← RENAME from Wavelets)
```

**Changes**:
1. **Move** `signal/FastFourierTransform.java` → `transform/FastFourierTransform.java`
2. **Rename** `signal/Wavelets.java` → `transform/WaveletTransform.java`
3. **Update** `DiscreteFourierTransform` to use `FastFourierTransform` internally
4. **Delete** empty signal/ package

**Benefits**:
✅ Clear separation: algorithm vs interface implementation
✅ Reusable FFT algorithm
✅ Modern naming (WaveletTransform not Wavelets)
✅ All transforms in one place

---

### Option B: Merge Into Single Class ❌ NOT RECOMMENDED

Make DiscreteFourierTransform contain the FFT algorithm directly.

**Rejected because**:
- Loses reusability
- Mixes interface implementation with algorithm
- Harder to test/benchmark different algorithms

---

## 📝 Detailed Changes

### 1. Move FastFourierTransform

```bash
git mv src/main/java/org/jscience/mathematics/signal/FastFourierTransform.java \
        src/main/java/org/jscience/mathematics/analysis/transform/FastFourierTransform.java
```

**Update package**:
```java
// OLD
package org.jscience.mathematics.signal;

// NEW  
package org.jscience.mathematics.analysis.transform;
```

**Update imports**: (check for external references)

---

### 2. Rename & Move Wavelets

```bash
git mv src/main/java/org/jscience/mathematics/signal/Wavelets.java \
        src/main/java/org/jscience/mathematics/analysis/transform/WaveletTransform.java
```

**Update**:
```java
// Rename class
public final class Wavelets { ... }  // OLD
↓
public final class WaveletTransform { ... }  // NEW

// Update package
package org.jscience.mathematics.signal;  // OLD
↓
package org.jscience.mathematics.analysis.transform;  // NEW
```

**Methods stay the same**:
- `haarTransform(Vector<Real>)`
- `inverseHaarTransform(Vector<Real>)`

---

### 3. Enhance DiscreteFourierTransform (OPTIONAL)

**Current**: Has own FFT implementation in `fft()` method

**Option**: Use FastFourierTransform instead

```java
// BEFORE
private Complex[] fft(Complex[] x, boolean inv) {
    // ... own implementation
}

// AFTER
@Override
public Vector<Complex> evaluate(Vector<Complex> input) {
    if (inverse) {
        return FastFourierTransform.inverseTransform(input);
    } else {
        return FastFourierTransform.transform(input);
    }
}
```

**Consideration**: Keep both implementations?
- FastFourierTransform: Recursive Cooley-Tukey
- DiscreteFourierTransform.fft(): May have different algorithm

**Decision**: Keep both for now, consolidate later if identical

---

### 4. Delete signal Package

```bash
# After moving both files
rmdir src/main/java/org/jscience/mathematics/signal
```

---

## 🔍 Pre-Merge Checklist

- [x] Analyze both packages
- [x] Identify NO duplicates
- [x] Determine relationships
- [x] Plan moves and renames
- [ ] Check for external imports to signal.*
- [ ] Verify no other files in signal/
- [ ] Create merge implementation
- [ ] Test compilation
- [ ] Update documentation

---

## 🚀 Implementation Steps

### Step 1: Check External Dependencies
```bash
grep -r "import org.jscience.mathematics.signal" src/
```

### Step 2: Move FastFourierTransform
- git mv command
- Update package declaration
- Update any imports

### Step 3: Rename & Move Wavelets → WaveletTransform
- git mv with rename
- Update class name
- Update package declaration  
- Update any external references

### Step 4: Verify signal/ Empty
```bash
ls src/main/java/org/jscience/mathematics/signal/
```

### Step 5: Delete signal/ Package
```bash
git rm -r src/main/java/org/jscience/mathematics/signal/
```

### Step 6: Commit
```bash
git add -A
git commit -m "refactor: merge signal into transform package"
```

---

## 📊 Impact Analysis

### Files Modified: 3
- FastFourierTransform.java (package change)
- Wavelets.java → WaveletTransform.java (rename + package)
- DiscreteFourierTransform.java (optional - if using FFT internally)

### Packages: -1
- Deleted: org.jscience.mathematics.signal

### Breaking Changes: Minimal
- Anyone importing `signal.FastFourierTransform` → `transform.FastFourierTransform`
- Anyone using class `Wavelets` → `WaveletTransform`

### Migration:
```java
// OLD
import org.jscience.mathematics.signal.FastFourierTransform;
import org.jscience.mathematics.signal.Wavelets;

// NEW
import org.jscience.mathematics.analysis.transform.FastFourierTransform;
import org.jscience.mathematics.analysis.transform.WaveletTransform;

// OLD
Wavelets.haarTransform(vector);

// NEW
WaveletTransform.haarTransform(vector);
```

---

## ✅ Validation

After merge, verify:
1. All files compile
2. transform/ contains 4 files
3. signal/ deleted
4. No broken imports
5. Tests pass (if any)

---

## 🎯 Final Structure

```
analysis/transform/
├── Transform.java           (interface - extends Bijection)
├── DiscreteFourierTransform.java  (DFT class implements Transform, VectorFunction)
├── FastFourierTransform.java     (FFT algorithm - static utilities)
└── WaveletTransform.java         (Haar wavelet - static utilities)
```

**Clear organization**:
- **Interface**: Transform
- **Object-oriented wrapper**: DiscreteFourierTransform 
- **Algorithm implementations**: FastFourierTransform, WaveletTransform

---

## 📝 Commit Message

```
refactor: merge signal package into analysis/transform

Consolidate transform-related classes:
- Moved FastFourierTransform: signal → transform
- Renamed Wavelets → WaveletTransform (modern naming)
- Deleted signal package (now empty)

All transforms now in analysis/transform/:
- Transform interface (extends Bijection)
- DiscreteFourierTransform (DFT/FFT wrapper)
- FastFourierTransform (Cooley-Tukey algorithm)
- WaveletTransform (Haar wavelet)

No duplicates found - FastFourierTransform is algorithm,
DiscreteFourierTransform is object-oriented wrapper.

BREAKING CHANGE: Import paths changed
- signal.FastFourierTransform → transform.FastFourierTransform
- signal.Wavelets → transform.WaveletTransform (class renamed)
```

---

**Ready to execute!** 🚀
