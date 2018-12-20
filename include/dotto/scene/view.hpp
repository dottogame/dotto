#pragma once

#include "../pch.h"

#include "../program.hpp"
#include "../ui/texture.hpp"
#include "../ui/rect.hpp"
#include "../audio/audio.hpp"
#include "../input.hpp"
#include "launch.hpp"
#include "menu.hpp"
#include "stage.hpp"

#define SCENE_LAUNCH 0
#define SCENE_MENU 1
#define SCENE_STAGE 2

namespace dotto::view
{
    int scene = SCENE_MENU;

    std::vector<ui::rect*>* live_meshes;

    void mouse_handle(int button, int action, GLFWwindow* window)
    {
        switch (scene)
        {
            case SCENE_MENU:
                break;

            case SCENE_STAGE:
                break;
        }
    }

    void key_handle(int key, int action, GLFWwindow* window)
    {
        switch (scene)
        {
            case SCENE_MENU:
                if (key == GLFW_KEY_ENTER && action == GLFW_RELEASE)
                    live_meshes = &(stage::meshes);

                break;

            case SCENE_STAGE:
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    live_meshes = &(menu::meshes);

                break;
        }
    }

    void init()
    {
        // set input
        input::mouse_handle = &(mouse_handle);
        input::key_handle = &(key_handle);

        // start with menu as live
        live_meshes = &(menu::meshes);
    }

    void load()
    {
        // load all static audio
        menu::background = new audio::source("res\\audio\\Icecream Queen-02.mp3");

        // load all shaders
        GLuint default_shader = dotto::pipeline::create_program(
            "res\\shaders\\default.vert",
            "res\\shaders\\default.frag"
        );

        // load all graphics
        ui::texture* tex_back = new ui::texture("res\\graphics\\back.png");
        ui::texture* tex_logo = new ui::texture("res\\graphics\\dotto_logo.png");

        // construct all rectangles
        ui::rect* rect_back = new ui::rect(default_shader, tex_back);
        ui::rect* rect_logo = new ui::rect(default_shader, tex_logo);

        // distribute rect mesh pointers to their scene mesh groups
        menu::meshes.push_back(rect_back);
        menu::meshes.push_back(rect_logo);
    }

    void clean()
    {
        menu::clean();
    }
}
