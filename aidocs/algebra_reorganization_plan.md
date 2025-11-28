# Algebra Package - Minimal Reorganization

**Date**: 2025-11-27  
**Status**: ✅ Mostly Done - Only 2 Moves Needed!

---

## 🎉 Current State: **ALREADY WELL-ORGANIZED!**

```
algebra/
├── Group.java, Ring.java, Field.java, etc. (interfaces - 21 files)
├── algebras/  ✅ GOOD
│   ├── CliffordAlgebra.java
│   ├── KleeneAlgebra.java
│   ├── Loop.java
│   ├── Magma.java
│   └── Quasigroup.java
│
├── categories/  ✅ GOOD
│   ├── Bifunctor.java
│   ├── FiniteSetsCategory.java
│   ├── HilbertSpacesCategory.java
│   ├── HomFunctor.java
│   └── Preorder.java
│
├── groups/  ✅ GOOD (12 files!)
│   ├── CyclicGroup.java
│   ├── DihedralGroup.java
│   ├── LieGroup.java
│   ├── SymmetricGroup.java
│   └── ... (8 more)
│
├── rings/  ✅ GOOD
│   └── Polynomial Ring.java
│
├── spaces/  ✅ GOOD
│   ├── BanachSpace.java
│   └── HilbertSpace.java
│
├── LieAlgebra.java  ⚠️ SHOULD BE IN algebras/
├── BooleanAlgebra.java  ⚠️ SHOULD BE IN algebras/
└── AbelianGroup.java  ⚠️ WAIT - already in algebra/, not sets/!
```

---

## ✅ Good News

1. **Subdirectories already exist** - algebras, groups, rings, categories, spaces
2. **Groups are already organized** - 12 group files in groups/
3. **AbelianGroup is ALREADY in algebra/** - Not in sets as review suggested!
4. **Only 2 files misplaced**: LieAlgebra, BooleanAlgebra

---

## 🎯 Minimal Changes Needed

### Move 1: LieAlgebra.java → algebras/

```bash
git mv src/main/java/org/jscience/mathematics/algebra/LieAlgebra.java \
        src/main/java/org/jscience/mathematics/algebra/algebras/LieAlgebra.java
```

**Update package**:
```java
package org.jscience.mathematics.algebra;  // OLD
↓
package org.jscience.mathematics.algebra.algebras;  // NEW
```

**Rationale**: Belongs with CliffordAlgebra, KleeneAlgebra

---

### Move 2: BooleanAlgebra.java → algebras/

```bash
git mv src/main/java/org/jscience/mathematics/algebra/BooleanAlgebra.java \
        src/main/java/org/jscience/mathematics/algebra/algebras/BooleanAlgebra.java
```

**Update package**:
```java
package org.jscience.mathematics.algebra;  // OLD
↓
package org.jscience.mathematics.algebra.algebras;  // NEW
```

**Rationale**: Specific algebra type, belongs in algebras/

---

## 📋 Implementation Steps

1. **Move LieAlgebra**
   - git mv to algebras/
   - Update package declaration
   - Check for imports to update

2. **Move BooleanAlgebra**
   - git mv to algebras/
   - Update package declaration
   - Check for imports to update

3. **Search for external imports**
   ```bash
   grep -r "import.*LieAlgebra" src/
   grep -r "import.*BooleanAlgebra" src/
   ```

4. **Update imports**
   ```java
   // OLD
   import org.jscience.mathematics.algebra.LieAlgebra;
   import org.jscience.mathematics.algebra.BooleanAlgebra;
   
   // NEW
   import org.jscience.mathematics.algebra.algebras.LieAlgebra;
   import org.jscience.mathematics.algebra.algebras.BooleanAlgebra;
   ```

5. **Commit**
   ```bash
   git commit -m "refactor: move LieAlgebra and BooleanAlgebra to algebras/ subdirectory"
   ```

---

## 💡 Architectural Review Update

**Original Concern**: "LieAlgebra, BooleanAlgebra at wrong level, AbelianGroup in sets"

**Reality**:
- ✅ **AbelianGroup is ALREADY in algebra/** (not in sets)
- ⚠️ LieAlgebra, BooleanAlgebra still at top level
- ✅ **Package already has excellent subdirectories**

**Conclusion**: Architectural review was 90% complete already. Only 2 files to move!

---

## 🎯 Final Structure

```
algebra/
├── [Interfaces at top level - GOOD]
│   ├── Group.java
│   ├── Ring.java
│   ├── Field.java
│   ├── Monoid.java
│   ├── Module.java
│   ├── VectorSpace.java
│   └── ...
│
├── algebras/  ← Move LieAlgebra, BooleanAlgebra HERE
│   ├── BooleanAlgebra.java  ← MOVED
│   ├── CliffordAlgebra.java
│   ├── KleeneAlgebra.java
│   ├── LieAlgebra.java  ← MOVED
│   ├── Loop.java
│   ├── Magma.java
│   └── Quasigroup.java
│
├── groups/ (12 files - already perfect)
├── rings/ (PolynomialRing.java)
├── categories/ (5 files)
└── spaces/ (2 files)
```

**Perfect organization**! ✨

---

## ✅ Summary

- **Files to move**: 2
- **Breaking changes**: Minimal (2 import paths)
- **Effort**: Low
- **Impact**: High (consistency)

**Ready to execute!** 🚀
