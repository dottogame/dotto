#pragma once

#include "../pch.h"

#include "../program.hpp"
#include "../ui/texture.hpp"
#include "../ui/rect.hpp"
#include "../audio/audio.hpp"
#include "scene.hpp"
#include "../input.hpp"

namespace dotto::scene::stage
{
    void present();
}

namespace dotto::scene::menu
{
    std::vector<ui::rect*> meshes;

    audio::source* background;

    void mouse_handle(int button, int action, GLFWwindow* window)
    {

    }

    void key_handle(int key, int action, GLFWwindow* window)
    {
        if (key == GLFW_KEY_ENTER && action == GLFW_RELEASE)
        {
            stage::present();
        }
    }

    void present()
    {
        input::mouse_handle = &(mouse_handle);
        input::key_handle = &(key_handle);
        live_meshes = &(meshes);

        audio::play(background);
    }

    void init()
    {
        background = new audio::source("res\\audio\\Icecream Queen-02.mp3");

        GLuint default_shader = dotto::pipeline::create_program(
            "res\\shaders\\default.vert",
            "res\\shaders\\default.frag"
        );

        ui::texture* tex_back = new ui::texture("res\\graphics\\back.png");
        ui::texture* tex_logo = new ui::texture("res\\graphics\\dotto_logo.png");

        ui::rect* rect_back = new ui::rect(default_shader, tex_back);
        ui::rect* rect_logo = new ui::rect(default_shader, tex_logo);

        meshes.push_back(rect_back);
        meshes.push_back(rect_logo);
    }

    void clean()
    {
        for (auto mesh : meshes) delete mesh;
    }
}

#include "stage.hpp"
