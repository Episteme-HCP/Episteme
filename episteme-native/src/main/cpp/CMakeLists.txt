cmake_minimum_required(VERSION 3.10)
project(episteme-native-root)

# Subdirectories for modular components
add_subdirectory(vision)
add_subdirectory(audio)

# Install rule for all native libraries
# This will copy everything to the libs directory
install(TARGETS episteme-native miniaudio DESTINATION ${CMAKE_SOURCE_DIR}/../../../libs)
