include_directories(../include "$ENV{TEMP_VK_SDK_PATH}/Include")
link_directories(../lib "$ENV{TEMP_VK_SDK_PATH}/Lib")

set(BUILD_FILES application.cpp entry.cpp graphics/renderer.cpp)

if(CMAKE_BUILD_TYPE MATCHES Debug)
  set(EXE_NAME "D_Dotto${PROJECT_VERSION}")
else()
  set(EXE_NAME "Dotto${PROJECT_VERISON}")
endif()

add_executable(${EXE_NAME} ${BUILD_FILES})
set_target_properties(${EXE_NAME} PROPERTIES RUNTIME_OUTPUT_DIRECTORY ../../bin)
target_link_libraries(${EXE_NAME} PUBLIC glfw3 vulkan-1)
