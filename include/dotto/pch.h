#ifndef _DOTTO_PRECOMPILED_HEADER
#define _DOTTO_PRECOMPILED_HEADER
#include <fstream>
#include <iostream>
#include <memory>
#include <string>
#include <unordered_map>
#include <vector>
#include "gl/glew.h"
#include "glfw/glfw3.h"
#include "dotto.pb.h"

#ifdef _WIN32
#define UNICODE

#ifdef __MINGW32__
#define NOMINMAX
#endif

#define STRICT
#define VC_EXTRALEAN
#define WIN32_LEAN_AND_MEAN
#include <Windows.h>
#undef UNICODE
#undef NOMINMAX
#undef STRICT
#undef VC_EXTRALEAN
#undef WIN32_LEAN_AND_MEAN
#endif

#endif
