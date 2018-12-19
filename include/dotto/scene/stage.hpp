#pragma once

#include "../pch.h"

#include "../program.hpp"
#include "../ui/texture.hpp"
#include "../ui/rect.hpp"
#include "../audio/audio.hpp"
#include "scene.hpp"
#include "../input.hpp"

namespace dotto::scene::menu
{
    void present();
}

namespace dotto::scene::stage
{
    std::vector<ui::rect*> meshes;

    void mouse_handle(int button, int action, GLFWwindow* window)
    {

    }

    void key_handle(int key, int action, GLFWwindow* window)
    {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        {
            menu::present();
        }
    }

    void present()
    {
        input::mouse_handle = &(mouse_handle);
        input::key_handle = &(key_handle);
        live_meshes = &(meshes);
    }

    void init()
    {
        GLuint default_shader = dotto::pipeline::create_program(
            "res\\shaders\\default.vert",
            "res\\shaders\\default.frag"
        );
    }

    void clean()
    {
        for (auto mesh : meshes) delete mesh;
    }
}

#include "menu.hpp"
