cmake_minimum_required(VERSION 3.9.2)
project(Dotto VERSION 1.0.0 LANGUAGES CXX)

option(BUILD_QUIET "Makes sure the compiler shuts up :)" OFF)

message(STATUS "Building Dotto")
message(STATUS "Option: BUILD_QUIET set to \"${BUILD_QUIET}\"")

set(CMAKE_CXX_FLAGS_DEBUG   "${CMAKE_CXX_FLAGS_DEBUG} -D_DEBUG=1 -g -march=native -std=c++17 -Wpedantic -Wall -Wextra -Werror=implicit-function-declaration")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -D_DEBUG=0 -march=native -std=c++17 -pipe -O3 -Wpedantic -Wall -Wextra -Werror=implicit-function-declaration")
set(QUIET_FLAGS "-Wno-unused-parameter -Wno-unused-variable -Wno-switch")

if(BUILD_QUIET)
  set(CMAKE_CXX_FLAGS_DEBUG   "${CMAKE_CXX_FLAGS_DEBUG} ${QUIET_FLAGS}")
  set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} ${QUIET_FLAGS}")
endif()

if(CMAKE_BUILD_TYPE MATCHES Debug)
  message(STATUS "Building Dotto Debug Profile")
  message(STATUS "Compiling with flags: ${CMAKE_CXX_FLAGS_DEBUG}")
elseif(CMAKE_BUILD_TYPE MATCHES Release)
  message(STATUS "Building Dotto Debug Profile")
  message(STATUS "Compiling with flags: ${CMAKE_CXX_FLAGS_RELEASE}")
endif()

message(STATUS "Checking for Vulkan SDK")

if(DEFINED ENV{VK_SDK_PATH})
  set(ENV{TEMP_VK_SDK_PATH} $ENV{VK_SDK_PATH})
elseif(DEFINED ENV{VULKAN_SDK})
  set(ENV{TEMP_VK_SDK_PATH} $ENV{VULKAN_SDK})
else()
  message(FATAL_ERROR "Vulkan SDK not found!")
endif()

message(STATUS "Vulkan SDK path found: $ENV{TEMP_VK_SDK_PATH}")
add_subdirectory(source)

if(CMAKE_BUILD_TYPE MATCHES Debug)
  enable_testing()
  add_subdirectory(tests)
  add_subdirectory(benchmarks)
endif()

unset(ENV{TEMP_VK_SDK_PATH})
