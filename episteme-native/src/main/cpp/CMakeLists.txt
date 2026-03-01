cmake_minimum_required(VERSION 3.10)
project(episteme_vision VERSION 1.0)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Enable aggressive optimizations
if(MSVC)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /O2 /arch:AVX2 /fp:fast")
else()
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -march=native -ffast-math")
endif()

add_library(episteme_vision SHARED
    vision/process_image.cpp
)

# Export symbols on Windows
if(MSVC)
    set_target_properties(episteme_vision PROPERTIES WINDOWS_EXPORT_ALL_SYMBOLS ON)
endif()
