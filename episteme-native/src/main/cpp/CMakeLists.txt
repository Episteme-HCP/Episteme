cmake_minimum_required(VERSION 3.10)
project(episteme-native-root LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_C_STANDARD 11)

# Optimization flags
if(MSVC)
    add_compile_options(/O2 /Oi /Ot /arch:AVX2 /fp:fast)
else()
    add_compile_options(-O3 -mavx2 -mfma -ffast-math -fPIC)
endif()

# Main consolidated library
add_library(episteme-native SHARED 
    vision/process_image.cpp
    physics/collision.cpp
)

if(WIN32)
    set_target_properties(episteme-native PROPERTIES
        PREFIX ""
        SUFFIX ".dll"
    )
endif()

# Audio library (separate for now as it's C)
add_subdirectory(audio)

# Install rule for ALL native libraries
install(TARGETS episteme-native miniaudio DESTINATION ${CMAKE_SOURCE_DIR}/../../../libs)
