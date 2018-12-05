#pragma once

#include "pch.h"

namespace dotto {
    /* This class is designated for building the GLFWwindow required to run the
     * dotto client.
     */
    class window final {
        /* Tells if glfw has been initialized. */
        bool m_initialized = false;

        /* The underlying window that represents this  */
        GLFWwindow* m_window;

        /* The original width of this window after going fullscreen. */
        int m_original_width;

        /* The original height of this window after going fullscreen. */
        int m_original_height;

    public:
        // Constructs a new window
        window(bool fullscreen) :
            m_window(nullptr),
            m_original_width(1280),
            m_original_height(720)
        {
            if (!m_initialized) {
                std::cout << "[Dotto] Initializing glfw" << std::endl;
                if (!glfwInit()) {
                    std::cerr << "[Dotto] : Failed to initialize GLFW.\n";
                    return;
                }

                m_initialized = true;
            }

            // Request OpenGL core version 4.4
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

            // The monitor we will be displaying to.
            GLFWmonitor* monitor    = glfwGetPrimaryMonitor();
            const GLFWvidmode* mode = glfwGetVideoMode(monitor);

            // Go fullscreen or windowed.
            if (fullscreen)
                m_window = glfwCreateWindow(
                    mode->width,
                    mode->height,
                    "Dotto",
                    monitor,
                    NULL
                );
            else
                m_window = glfwCreateWindow(1280, 720, "dotto", NULL, NULL);

            // Check that window was created.
            if (!m_window) {
                std::cerr << "[Dotto] : Failed to create \n";
                return;
            }
        }

        // Deconstructs this
        ~window() {
            if (m_window)
                glfwDestroyWindow(m_window);

            glfwTerminate();
        }

        // Casts this window to a GLFWwindow*.
        operator GLFWwindow*() {
            return m_window;
        }

        // Closes this
        void close() {
            glfwSetWindowShouldClose(m_window, true);
        }

        // Hides this
        void hide() {
            glfwHideWindow(m_window);
        }

        // Checks if this window is open.
        bool is_open() {
            return !glfwWindowShouldClose(m_window);
        }

        // Makes this window current.
        void make_current() {
            glfwMakeContextCurrent(m_window);
        }

        // Polls the event of this
        void poll_events() {
            glfwPollEvents();
        }

        // Sets the position of this
        void position_at(const int& x_pos, const int& y_pos) {
            glfwSetWindowPos(m_window, x_pos, y_pos);
        }

        // Sets the size of this
        void resize_to(const int& width, const int& height) {
            glfwSetWindowSize(m_window, width, height);
        }

        // Shows this
        void show() {
            glfwShowWindow(m_window);
        }

        // Swaps the buffers of this
        void swap_buffers() {
            glfwSwapBuffers(m_window);
        }
    };
}
