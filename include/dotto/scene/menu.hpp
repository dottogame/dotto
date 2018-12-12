#pragma once

#include "../pch.h"

#include "../program.hpp"
#include "../ui/texture.hpp"
#include "../ui/rect.hpp"
#include "../audio/audio.hpp"

namespace dotto::scene::menu
{
    std::vector<ui::rect*> meshes;

    audio::source* background;

    void init()
    {
        background = new audio::source("res\\audio\\soraw.mp3");

        GLuint default_shader = dotto::pipeline::create_program(
            "res\\shaders\\default.vert",
            "res\\shaders\\default.frag"
        );

        ui::texture* tex = new ui::texture("res\\graphics\\konata.png");
        
        ui::rect* testo = new ui::rect(default_shader, tex);

        meshes.push_back(testo);
    }

    void set()
    {
        audio::play(background);
    }

    void clean()
    {
        for (auto mesh : meshes) delete mesh;
    }
}
