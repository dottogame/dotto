#pragma once

#include "../pch.h"

#include "../io.hpp"
#include "rect.hpp"
#include "texture.hpp"
#include "../program.hpp"

namespace dotto::ui::asset
{
    GLuint default_shader;

    void init()
    {
        srand (time(NULL));
        default_shader = dotto::pipeline::create_program(
            "res\\shaders\\default.vert",
            "res\\shaders\\default.frag"
        );
    }

    rect* load(std::string path)
    {
        // normal graphic
        std::string full_path(std::string(path) + ".png");
        if (io::file::exists(io::file::make_relative(full_path)))
        {
            texture* tex = new texture(full_path.c_str());

            return new rect(default_shader, tex);
        }
        // advanced graphic
        else
        {
            // detect what's available and use defaults otherwise
            std::string vert_shader("res\\shaders\\default.vert");
            std::string frag_shader("res\\shaders\\default.frag");
            std::string image("res\\graphics\\default_image.png");

            std::string v_path(std::string(path) + "\\shader.vert");
            if (io::file::exists(io::file::make_relative(v_path)))
                vert_shader = v_path;

            std::string f_path(std::string(path) + "\\shader.frag");
            if (io::file::exists(io::file::make_relative(f_path)))
                frag_shader = f_path;

            std::string i_path(std::string(path) + "\\image.png");
            if (io::file::exists(io::file::make_relative(i_path)))
                image = i_path;

            GLuint shader_program = dotto::pipeline::create_program(
                vert_shader,
                frag_shader
            );

            texture* tex = new texture(image.c_str());

            return new rect(shader_program, tex);
        }
    }
}
