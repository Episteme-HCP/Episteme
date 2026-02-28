# Native Libraries Installation Guide for Episteme HPC

## Overview

Episteme HPC requires several native libraries for optimal performance. This guide provides download links, installation instructions, and configuration details for Windows systems.

---

## 1. MPI (Message Passing Interface) - RDMA Support

### Option A: MPJ Express (Recommended for Windows)
**Purpose**: Provides `mpi.Win` class for RDMA operations in distributed computing.

**Download**: 
- Official Site: http://mpj-express.org/
- Direct Download: https://sourceforge.net/projects/mpjexpress/files/releases/mpj-v0_44.tar.gz/download
- GitHub: https://github.com/MpiJ/MPJ-Express

**Installation**:
```powershell
# 1. Extract MPJ Express
Expand-Archive mpj-v0_44.tar.gz -DestinationPath C:\MPJ

# 2. Set environment variables
[System.Environment]::SetEnvironmentVariable('MPJ_HOME', 'C:\MPJ\mpj-v0_44', 'Machine')
[System.Environment]::SetEnvironmentVariable('PATH', "$env:PATH;C:\MPJ\mpj-v0_44\bin", 'Machine')

# 3. Add to Java classpath
# Add C:\MPJ\mpj-v0_44\lib\mpj.jar to your project dependencies
```

**Maven Dependency** (if available):
```xml
<dependency>
    <groupId>mpjexpress</groupId>
    <artifactId>mpj</artifactId>
    <version>0.44</version>
</dependency>
```

### Option B: OpenMPI with Java Bindings
**Download**: 
- OpenMPI: https://www.open-mpi.org/software/ompi/v4.1/
- Windows Build: https://www.microsoft.com/en-us/download/details.aspx?id=100593

**Note**: OpenMPI Java bindings are deprecated since v4.0. MPJ Express is recommended for Java.

---

## 2. HDF5 Library

### Windows Binaries
**Purpose**: High-performance scientific data format for large datasets.

**Download**:
- Official: https://www.hdfgroup.org/downloads/hdf5/
- Direct (v1.14.3): https://support.hdfgroup.org/ftp/HDF5/releases/hdf5-1.14/hdf5-1.14.3/bin/windows/hdf5-1.14.3-2-win-vs2022_intel.zip

**Installation**:
```powershell
# 1. Extract to C:\HDF5
Expand-Archive hdf5-1.14.3-2-win-vs2022_intel.zip -DestinationPath C:\HDF5

# 2. Add to PATH
[System.Environment]::SetEnvironmentVariable('PATH', "$env:PATH;C:\HDF5\bin", 'Machine')
[System.Environment]::SetEnvironmentVariable('HDF5_DIR', 'C:\HDF5', 'Machine')

# 3. Verify installation
C:\HDF5\bin\h5dump.exe --version
```

**Required DLLs**:
- `hdf5.dll`
- `hdf5_hl.dll`
- `zlib.dll` (included)

---

## 3. CFITSIO Library

### Windows Binaries
**Purpose**: FITS (Flexible Image Transport System) file format for astronomy data.

**Download**:
- Official: https://heasarc.gsfc.nasa.gov/fitsio/
- Windows Build: https://heasarc.gsfc.nasa.gov/fitsio/fitsio.html#windows

**Pre-built Binary**:
```powershell
# Download from NASA HEASARC
Invoke-WebRequest -Uri "https://heasarc.gsfc.nasa.gov/fitsio/cfitsio-4.3.0-win64.zip" -OutFile cfitsio.zip

# Extract
Expand-Archive cfitsio.zip -DestinationPath C:\cfitsio

# Add to PATH
[System.Environment]::SetEnvironmentVariable('PATH', "$env:PATH;C:\cfitsio\bin", 'Machine')
```

**Required DLL**: `cfitsio.dll`

---

## 4. FFTW3 Library

### Windows Binaries
**Purpose**: Fast Fourier Transform library for signal processing.

**Download**:
- Official: http://www.fftw.org/install/windows.html
- Direct (v3.3.10): http://www.fftw.org/fftw-3.3.10-dll64.zip

**Installation**:
```powershell
# 1. Download and extract
Invoke-WebRequest -Uri "http://www.fftw.org/fftw-3.3.10-dll64.zip" -OutFile fftw3.zip
Expand-Archive fftw3.zip -DestinationPath C:\fftw3

# 2. Generate import libraries (if needed)
cd C:\fftw3
lib /def:libfftw3-3.def /out:libfftw3-3.lib /machine:x64
lib /def:libfftw3f-3.def /out:libfftw3f-3.lib /machine:x64
lib /def:libfftw3l-3.def /out:libfftw3l-3.lib /machine:x64

# 3. Add to PATH
[System.Environment]::SetEnvironmentVariable('PATH', "$env:PATH;C:\fftw3", 'Machine')
```

**Required DLLs**:
- `libfftw3-3.dll` (double precision)
- `libfftw3f-3.dll` (single precision)
- `libfftw3l-3.dll` (long double precision)

---

## 5. Bullet Physics (Custom C-API Wrapper)

### Building the Wrapper
**Purpose**: Physics simulation for N-body collision detection.

**Note**: Episteme requires a custom C-API wrapper around Bullet Physics. Here's how to create it:

**Download Bullet Physics**:
```powershell
# Clone Bullet3 repository
git clone https://github.com/bulletphysics/bullet3.git C:\bullet3
cd C:\bullet3

# Build with CMake
mkdir build
cd build
cmake .. -DBUILD_SHARED_LIBS=ON -DUSE_DOUBLE_PRECISION=ON
cmake --build . --config Release
```

**Create C-API Wrapper** (`bullet_capi.h`):
```c
// Save as C:\bullet3\bullet_capi\bullet_capi.h
#ifndef BULLET_CAPI_H
#define BULLET_CAPI_H

#ifdef __cplusplus
extern "C" {
#endif

// Sphere collision detection
int bt_detect_sphere_collisions(
    const double* positions,  // [x0,y0,z0,x1,y1,z1,...]
    const double* radii,      // [r0,r1,...]
    int n,                    // number of spheres
    int* collisions           // output pairs [idA,idB,...]
);

// Collision resolution
void bt_resolve_sphere_collisions(
    double* positions,
    double* velocities,
    const double* masses,
    int n,
    const int* collisions,
    int numCollisions
);

#ifdef __cplusplus
}
#endif

#endif // BULLET_CAPI_H
```

**Implementation** (`bullet_capi.cpp`):
```cpp
// Save as C:\bullet3\bullet_capi\bullet_capi.cpp
#include "bullet_capi.h"
#include "btBulletDynamicsCommon.h"
#include <vector>

extern "C" {

int bt_detect_sphere_collisions(
    const double* positions,
    const double* radii,
    int n,
    int* collisions
) {
    btDefaultCollisionConfiguration* config = new btDefaultCollisionConfiguration();
    btCollisionDispatcher* dispatcher = new btCollisionDispatcher(config);
    btBroadphaseInterface* broadphase = new btDbvtBroadphase();
    
    btDiscreteDynamicsWorld* world = new btDiscreteDynamicsWorld(
        dispatcher, broadphase, nullptr, config);
    
    std::vector<btCollisionObject*> objects;
    
    // Create sphere collision objects
    for (int i = 0; i < n; i++) {
        btSphereShape* sphere = new btSphereShape(radii[i]);
        btCollisionObject* obj = new btCollisionObject();
        obj->setCollisionShape(sphere);
        obj->setWorldTransform(btTransform(
            btQuaternion(0,0,0,1),
            btVector3(positions[i*3], positions[i*3+1], positions[i*3+2])
        ));
        world->addCollisionObject(obj);
        objects.push_back(obj);
    }
    
    // Perform collision detection
    world->performDiscreteCollisionDetection();
    
    int numCollisions = 0;
    int numManifolds = dispatcher->getNumManifolds();
    
    for (int i = 0; i < numManifolds; i++) {
        btPersistentManifold* manifold = dispatcher->getManifoldByIndexInternal(i);
        if (manifold->getNumContacts() > 0) {
            const btCollisionObject* objA = manifold->getBody0();
            const btCollisionObject* objB = manifold->getBody1();
            
            // Find indices
            int idA = -1, idB = -1;
            for (int j = 0; j < n; j++) {
                if (objects[j] == objA) idA = j;
                if (objects[j] == objB) idB = j;
            }
            
            if (idA >= 0 && idB >= 0) {
                collisions[numCollisions * 2] = idA;
                collisions[numCollisions * 2 + 1] = idB;
                numCollisions++;
            }
        }
    }
    
    // Cleanup
    for (auto obj : objects) {
        delete obj->getCollisionShape();
        delete obj;
    }
    delete world;
    delete broadphase;
    delete dispatcher;
    delete config;
    
    return numCollisions;
}

void bt_resolve_sphere_collisions(
    double* positions,
    double* velocities,
    const double* masses,
    int n,
    const int* collisions,
    int numCollisions
) {
    // Simple elastic collision resolution
    for (int i = 0; i < numCollisions; i++) {
        int idA = collisions[i * 2];
        int idB = collisions[i * 2 + 1];
        
        // Compute relative velocity and normal
        btVector3 vA(velocities[idA*3], velocities[idA*3+1], velocities[idA*3+2]);
        btVector3 vB(velocities[idB*3], velocities[idB*3+1], velocities[idB*3+2]);
        btVector3 pA(positions[idA*3], positions[idA*3+1], positions[idA*3+2]);
        btVector3 pB(positions[idB*3], positions[idB*3+1], positions[idB*3+2]);
        
        btVector3 normal = (pB - pA).normalized();
        btVector3 vRel = vA - vB;
        
        double vn = vRel.dot(normal);
        if (vn > 0) continue; // Moving apart
        
        // Elastic collision impulse
        double mA = masses[idA];
        double mB = masses[idB];
        double j = -(1.0 + 1.0) * vn / (1.0/mA + 1.0/mB); // restitution = 1.0
        
        btVector3 impulse = j * normal;
        vA += impulse / mA;
        vB -= impulse / mB;
        
        // Update velocities
        velocities[idA*3] = vA.x();
        velocities[idA*3+1] = vA.y();
        velocities[idA*3+2] = vA.z();
        velocities[idB*3] = vB.x();
        velocities[idB*3+1] = vB.y();
        velocities[idB*3+2] = vB.z();
    }
}

} // extern "C"
```

**Build the Wrapper**:
```powershell
# Create CMakeLists.txt for the wrapper
cd C:\bullet3\bullet_capi

# CMakeLists.txt content:
@"
cmake_minimum_required(VERSION 3.10)
project(bullet_capi)

set(CMAKE_CXX_STANDARD 11)

find_package(Bullet REQUIRED)

add_library(bullet_capi SHARED bullet_capi.cpp)
target_include_directories(bullet_capi PRIVATE ${BULLET_INCLUDE_DIRS})
target_link_libraries(bullet_capi ${BULLET_LIBRARIES})
"@ | Out-File CMakeLists.txt

# Build
mkdir build
cd build
cmake .. -DCMAKE_PREFIX_PATH=C:\bullet3\build
cmake --build . --config Release

# Copy DLL to system path
Copy-Item Release\bullet_capi.dll C:\Windows\System32\
```

---

## 6. OpenBLAS (Alternative to Intel MKL)

### Windows Binaries
**Purpose**: High-performance BLAS (Basic Linear Algebra Subprograms) library.

**Download**:
- Official: https://www.openblas.net/
- Pre-built: https://github.com/xianyi/OpenBLAS/releases/latest

**Installation**:
```powershell
# Download latest release
Invoke-WebRequest -Uri "https://github.com/xianyi/OpenBLAS/releases/download/v0.3.25/OpenBLAS-0.3.25-x64.zip" -OutFile openblas.zip

# Extract
Expand-Archive openblas.zip -DestinationPath C:\OpenBLAS

# Add to PATH
[System.Environment]::SetEnvironmentVariable('PATH', "$env:PATH;C:\OpenBLAS\bin", 'Machine')
```

**Required DLL**: `libopenblas.dll`

---

## Verification Script

Save as `verify_native_libs.ps1`:

```powershell
# Episteme Native Libraries Verification Script

Write-Host "=== Episteme HPC Native Libraries Verification ===" -ForegroundColor Cyan

$libraries = @(
    @{Name="HDF5"; DLL="hdf5.dll"; Path="C:\HDF5\bin"},
    @{Name="CFITSIO"; DLL="cfitsio.dll"; Path="C:\cfitsio\bin"},
    @{Name="FFTW3"; DLL="libfftw3-3.dll"; Path="C:\fftw3"},
    @{Name="OpenBLAS"; DLL="libopenblas.dll"; Path="C:\OpenBLAS\bin"},
    @{Name="Bullet C-API"; DLL="bullet_capi.dll"; Path="C:\bullet3\bullet_capi\build\Release"}
)

foreach ($lib in $libraries) {
    $fullPath = Join-Path $lib.Path $lib.DLL
    if (Test-Path $fullPath) {
        Write-Host "✓ $($lib.Name): FOUND at $fullPath" -ForegroundColor Green
    } else {
        Write-Host "✗ $($lib.Name): NOT FOUND (expected at $fullPath)" -ForegroundColor Red
    }
}

# Check MPJ Express
if (Test-Path "C:\MPJ\mpj-v0_44\lib\mpj.jar") {
    Write-Host "✓ MPJ Express: FOUND" -ForegroundColor Green
} else {
    Write-Host "✗ MPJ Express: NOT FOUND" -ForegroundColor Red
}

Write-Host "`n=== PATH Environment Variable ===" -ForegroundColor Cyan
Write-Host $env:PATH -Separator "`n"
```

Run with: `.\verify_native_libs.ps1`

---

## Alternative: Using Package Managers

### Chocolatey (Windows)
```powershell
# Install Chocolatey if not already installed
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install libraries
choco install hdf5 -y
choco install fftw -y
```

### vcpkg (Cross-platform C++ Package Manager)
```powershell
# Clone vcpkg
git clone https://github.com/Microsoft/vcpkg.git C:\vcpkg
cd C:\vcpkg
.\bootstrap-vcpkg.bat

# Install libraries
.\vcpkg install hdf5:x64-windows
.\vcpkg install fftw3:x64-windows
.\vcpkg install bullet3:x64-windows
.\vcpkg install openblas:x64-windows
```

---

## Troubleshooting

### DLL Not Found Errors
1. Verify DLL is in PATH: `where.exe <dll_name>`
2. Check dependencies: `dumpbin /dependents <dll_path>`
3. Use Dependency Walker: https://www.dependencywalker.com/

### Java Cannot Find Native Library
1. Add to `java.library.path`:
   ```powershell
   java -Djava.library.path="C:\HDF5\bin;C:\fftw3;C:\OpenBLAS\bin" -jar your-app.jar
   ```

2. Or set in JVM arguments:
   ```xml
   <configuration>
     <argLine>-Djava.library.path=C:\HDF5\bin;C:\fftw3;C:\OpenBLAS\bin</argLine>
   </configuration>
   ```

### MPI Initialization Fails
1. Ensure `MPJ_HOME` is set correctly
2. Check `mpj.jar` is in classpath
3. Run MPJ test: `java -jar C:\MPJ\mpj-v0_44\lib\starter.jar`

---

## Summary

**Required Downloads**:
1. MPJ Express: http://mpj-express.org/ (or SourceForge)
2. HDF5: https://www.hdfgroup.org/downloads/hdf5/
3. CFITSIO: https://heasarc.gsfc.nasa.gov/fitsio/
4. FFTW3: http://www.fftw.org/install/windows.html
5. Bullet3: https://github.com/bulletphysics/bullet3 (build from source)
6. OpenBLAS: https://github.com/xianyi/OpenBLAS/releases

**Installation Time**: ~30-60 minutes for all libraries

**Disk Space**: ~2 GB total

---

**Last Updated**: 2026-01-30  
**Tested On**: Windows 11, Java 21, Maven 3.9.5
