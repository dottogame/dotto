#pragma once

#define SCENE_MENU 0
#define SCENE_STAGE 1

#include "../pch.h"

#include "../ui/rect.hpp"
#include "../input.hpp"

namespace dotto::scene
{
    int scene;

    std::vector<ui::rect*>* live_meshes;
}
