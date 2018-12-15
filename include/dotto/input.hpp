#pragma once

#include "pch.h"
#include "globals.hpp"

namespace dotto::input
{
    void (*mouse_handle)(int, int, GLFWwindow*);
    void (*key_handle)(int, int, GLFWwindow*);

    void key_callback(GLFWwindow* window, int key, int scancode, int action, int mods)
    {
        if (key_handle != NULL) key_handle(key, action, window);
    }

    void mouse_button_callback(GLFWwindow* window, int button, int action, int mods)
    {
        if (mouse_handle != NULL) mouse_handle(button, action, window);
    }

    void init(GLFWwindow* window)
    {
        glfwSetKeyCallback(window, key_callback);
        glfwSetMouseButtonCallback(window, mouse_button_callback);
    }

    /*
        Changes coordinates from Viewport Coordinates (0,0 as top left) to
        Cartesian coordinate system (0,0 in the bottom left) and makes both axis
        go from 0 to 1 instead of 0 to width/height.
    */
    void normalize_coords(double& x, double& y)
    {
        x /= D_WIDTH;
        y = -(y - D_HEIGHT) / D_HEIGHT;
    }
}
