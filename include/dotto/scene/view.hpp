#pragma once

#include "../pch.h"

#include "../program.hpp"
#include "../ui/texture.hpp"
#include "../ui/rect.hpp"
#include "../audio/audio.hpp"
#include "../input.hpp"
#include "../globals.hpp"
#include "../ui/asset.hpp"
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
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    should_exit = true;

                else if (key == GLFW_KEY_ENTER && action == GLFW_RELEASE)
                {
                    live_meshes = &(stage::meshes);
                    scene = SCENE_STAGE;
                }

                break;

            case SCENE_STAGE:
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                {
                    live_meshes = &(menu::meshes);
                    scene = SCENE_MENU;
                }

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

        // load all assets
        ui::rect* wallpaper = ui::asset::load("data\\wallpapers\\techpro_miku");

        // distribute rect mesh pointers to their scene mesh groups
        menu::meshes.push_back(wallpaper);
    }

    void clean()
    {
        menu::clean();
    }
}
