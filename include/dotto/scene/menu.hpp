#pragma once

#include "../pch.h"

#include "../program.hpp"
#include "../ui/rect.hpp"

namespace dotto::scene::menu
{
    std::vector<ui::rect*> meshes;

    void init()
    {
        GLuint prog_id = dotto::pipeline::create_program(
            "res\\shaders\\default.vert",
            "res\\shaders\\default.frag"
        );

        dotto::ui::texture* tex = new dotto::ui::texture("res\\graphics\\konata.png");
        dotto::ui::rect* testo = new dotto::ui::rect(prog_id, tex);


        meshes.push_back(testo);
    }
}
