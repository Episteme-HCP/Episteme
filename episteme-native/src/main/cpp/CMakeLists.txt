cmake_minimum_required(VERSION 3.10)
project(episteme-native VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Enable aggressive optimization and SIMD auto-vectorization
if(MSVC)
    add_compile_options(/O2 /Oi /Ot /arch:AVX2 /fp:fast)
else()
    add_compile_options(-O3 -mavx2 -mfma -ffast-math)
endif()

add_library(episteme-native SHARED vision/process_image.cpp)

if(WIN32)
    set_target_properties(episteme-native PROPERTIES
        PREFIX ""
        SUFFIX ".dll"
    )
endif()

# Output directly to the libs directory for the benchmarker to find it
install(TARGETS episteme-native DESTINATION ${CMAKE_SOURCE_DIR}/../../../libs)
