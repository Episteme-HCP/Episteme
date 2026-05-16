cmake_minimum_required(VERSION 3.10)
project(miniaudio LANGUAGES C)

# Enable aggressive optimization
if(MSVC)
    add_compile_options(/O2 /Oi /Ot /fp:fast)
else()
    add_compile_options(-O3 -ffast-math -fPIC)
endif()

# Build miniaudio as a shared library
# We use miniaudio.c which should have #define MINIAUDIO_IMPLEMENTATION
add_library(miniaudio SHARED miniaudio.c)

if(WIN32)
    set_target_properties(miniaudio PROPERTIES
        PREFIX ""
        SUFFIX ".dll"
        WINDOWS_EXPORT_ALL_SYMBOLS ON
    )
    # On Windows, miniaudio might need some system libraries
    target_link_libraries(miniaudio PRIVATE kernel32 user32 advapi32 shell32 ole32 winmm)
else()
    target_link_libraries(miniaudio PRIVATE pthread dl m)
endif()
