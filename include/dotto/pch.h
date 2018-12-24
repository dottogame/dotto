#ifndef _DOTTO_PRECOMPILED_HEADER
#define _DOTTO_PRECOMPILED_HEADER
#include <chrono>
#include <fstream>
#include <iostream>
#include <memory>
#include <string>
#include <thread>
#include <vector>
#include <sstream>
#include <iomanip>
#include <lodepng.h>
#include <gl/glew.h>
#include <glfw/glfw3.h>
#include <glm/glm.hpp>
#include <glm/ext.hpp>
#include <glm/gtc/matrix_transform.hpp>

#ifdef linux
#include <sys/stat.h>
#endif

#ifdef _WIN32

#include <direct.h>
#include <tronkko/dirent.h>

#define UNICODE
#define STRICT

#ifndef __MINGW32__
#define NOMINMAX
#endif // __MINGW32__

#include <Windows.h>

#undef UNICODE
#undef STRICT
#ifndef __MINGW32__
#undef NOMINMAX
#endif // __MINGW32__
#endif // _WIN32
#endif // _DOTTO_PRECOMPILED_HEADER
