include_directories(../include)
link_directories(../lib)

set(BUILD_FILES entry.cpp)

if(CMAKE_BUILD_TYPE MATCHES Debug)
  set(EXE_NAME "D_Dotto${PROJECT_VERSION}")
else()
  set(EXE_NAME "Dotto${PROJECT_VERISON}")
endif()

add_executable(${EXE_NAME} ${BUILD_FILES})
set_target_properties(${EXE_NAME} PROPERTIES RUNTIME_OUTPUT_DIRECTORY ../../bin)
target_link_libraries(${EXE_NAME} PUBLIC glfw3)
