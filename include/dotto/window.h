#ifndef _DOTTO_WINDOW_HEADER
#define _DOTTO_WINDOW_HEADER
#include "pch.h"

namespace dotto {
    /* This class is designated for building the GLFWwindow required to run the
     * dotto client.
     */
    class window final {
        /* Tells if glfw has been initialized. */
        static bool m_initialized;

        /* The underlying window that represents this window. */
        GLFWwindow* m_window;

        /* The original width of this window after going fullscreen. */
        int m_original_width;

        /* The original height of this window after going fullscreen. */
        int m_original_height;

    public:
        /* Constructs a new window. */
        explicit window(bool fullscreen);

        /* Disallow copy construction. */
        window(const window& other) = delete;

        /* Disallow move construction. */
        window(window&& other) = delete;

        /* Deconstructs this window. */
        ~window();

        /* Casts this window to a GLFWwindow. */
        explicit operator GLFWwindow*();

        /* Closes this window. */
        void close();

        /* Hides this window. */
        void hide();

        /* Checks if this window is open. */
        bool is_open();

        /* Makes this the current window being rendered. */
        void make_current();

        /* Polls the events for this window. */
        void poll_events();

        /* Positions this window at the given position. */
        void position_at(const int& x_pos, const int& y_pos);

        /* Sets the size of this window. */
        void resize_to(const int& width, const int& height);

        /* Shows this window. */
        void show();

        /* Swaps the buffers of this window. */
        void swap_buffers();
    };
}

#endif
