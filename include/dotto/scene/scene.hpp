#pragma once

#include "../pch.h"

#include "../ui/rect.hpp"
#include "../input.hpp"
#include "menu.hpp"

namespace dotto::scene
{
    std::vector<ui::rect*>* live_meshes;

    void init()
    {
        menu::init();
        menu::set();
        input::mouse_handle = &(menu::mouse_handle);
        input::key_handle = &(menu::key_handle);
        live_meshes = &(menu::meshes);
    }

    void clean()
    {
        menu::clean();
    }
}
