cmake_minimum_required(VERSION 3.4.1)
add_library(
    serial_port
    SHARED
    SerialPort.c
    SerialPort.h
)
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        serial_port

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )