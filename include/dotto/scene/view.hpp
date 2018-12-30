#pragma once

#include "../pch.h"

#include "../program.hpp"
#include "../ui/texture.hpp"
#include "../ui/rect.hpp"
#include "../audio/audio.hpp"
#include "../input.hpp"
#include "../globals.hpp"
#include "../ui/asset.hpp"
#include "../util/console.hpp"
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

        // load assets
        std::vector<std::string> wallpapers;
        io::fetch_contents(wallpapers, "data\\wallpapers");

        console::log("Scanned for wallpapers. Found: ");
        for (auto item : wallpapers) console::log(item);
        if (wallpapers.size() == 0) console::warn("NO WALLPAPERS FOUND.");
        console::log("---- end list ----");

        std::string wallpaper_path = std::string(
            "data\\wallpapers\\"
        ) + wallpapers.at(rand() % wallpapers.size());

        ui::rect* wallpaper = ui::asset::load(wallpaper_path);
        ui::rect* logo = ui::asset::load("res\\graphics\\logo");
        ui::rect* rewind = ui::asset::load("res\\graphics\\menu\\rewind");
        ui::rect* list = ui::asset::load("res\\graphics\\menu\\list");
        ui::rect* play = ui::asset::load("res\\graphics\\menu\\play");
        ui::rect* fast_forward = ui::asset::load("res\\graphics\\menu\\fast_forward");

        // adjust assets
        logo->scale *= 0.1f;
        logo->position.y = -0.75f;

        rewind->scale *= 0.035f;
        rewind->position.x = -0.25f;
        rewind->position.y = -0.55f;

        list->scale *= 0.035f;
        list->position.x = -0.08f;
        list->position.y = -0.55f;

        play->scale *= 0.035f;
        play->position.x = 0.08f;
        play->position.y = -0.55f;

        fast_forward->scale *= 0.035f;
        fast_forward->position.x = 0.25f;
        fast_forward->position.y = -0.55f;

        // distribute rect mesh pointers to their scene mesh groups
        menu::meshes.push_back(wallpaper);
        menu::meshes.push_back(logo);

        menu::meshes.push_back(rewind);
        menu::meshes.push_back(list);
        menu::meshes.push_back(play);
        menu::meshes.push_back(fast_forward);
    }

    void clean()
    {
        menu::clean();
    }
}
