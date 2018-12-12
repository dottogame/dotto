#pragma once

#include "../pch.h"

#include "../ui/rect.hpp"
#include "menu.hpp"

namespace dotto::scene
{
    std::vector<ui::rect*>* live_meshes;

    void init()
    {
        menu::init();

        // live_meshes = &(menu::meshes);
    }
}
