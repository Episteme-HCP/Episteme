cmake_minimum_required(VERSION 3.10)
project(episteme-vision LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Enable aggressive optimization and SIMD auto-vectorization
if(MSVC)
    add_compile_options(/O2 /Oi /Ot /arch:AVX2 /fp:fast)
else()
    add_compile_options(-O3 -mavx2 -mfma -ffast-math)
endif()

# We keep the name episteme-native for the main native library to avoid breaking Java side
add_library(episteme-native SHARED process_image.cpp)

if(WIN32)
    set_target_properties(episteme-native PROPERTIES
        PREFIX ""
        SUFFIX ".dll"
    )
endif()
