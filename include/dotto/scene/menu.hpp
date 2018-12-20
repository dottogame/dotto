#pragma once

#include "../pch.h"

#include "../ui/rect.hpp"
#include "../audio/audio.hpp"

namespace dotto::view::menu
{
    std::vector<ui::rect*> meshes;

    audio::source* background;

    void clean()
    {
        delete background;
    }
}
