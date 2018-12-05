#include <dotto/window.h>

// Initialize m_initialized.
bool dotto::window::m_initialized = false;

// Constructs a new window.
dotto::window::window(bool fullscreen) :
    m_window(nullptr),
    m_original_width(1280),
    m_original_height(720)
{
    if (!m_initialized) {
        if (!glfwInit()) {
            std::cerr << "[Dotto] : Failed to initialize GLFW.\n";
            return;
        }

        m_initialized = true;
    }

    // Request OpenGL core version 4.4
    glfwWindowHint(GLFW_VISIBLE, false);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 4);

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
        m_window = glfwCreateWindow(1280, 720, "Dotto", NULL, NULL);

    // Check that window was created.
    if (!m_window) {
        std::cerr << "[Dotto] : Failed to create window.\n";
        return;
    }
}

// Deconstructs this window.
dotto::window::~window() {
    if (m_window)
        glfwDestroyWindow(m_window);

    glfwTerminate();
}

// Casts this window to a GLFWwindow*.
dotto::window::operator GLFWwindow*() {
    return m_window;
}

// Closes this window.
void dotto::window::close() {
    glfwSetWindowShouldClose(m_window, true);
}

// Hides this window.
void dotto::window::hide() {
    glfwHideWindow(m_window);
}

// Checks if this window is open.
bool dotto::window::is_open() {
    return !glfwWindowShouldClose(m_window);
}

// Makes this window current.
void dotto::window::make_current() {
    glfwMakeContextCurrent(m_window);
}

// Polls the event of this window.
void dotto::window::poll_events() {
    glfwPollEvents();
}

// Sets the position of this window.
void dotto::window::position_at(const int& x_pos, const int& y_pos) {
    glfwSetWindowPos(m_window, x_pos, y_pos);
}

// Sets the size of this window.
void dotto::window::resize_to(const int& width, const int& height) {
    glfwSetWindowSize(m_window, width, height);
}

// Shows this window.
void dotto::window::show() {
    glfwShowWindow(m_window);
}

// Swaps the buffers of this window.
void dotto::window::swap_buffers() {
    glfwSwapBuffers(m_window);
}
