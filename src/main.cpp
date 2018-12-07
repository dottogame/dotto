#include "dotto/pch.h"
#include "dotto/rectangle.hpp"
#include "dotto/scene.hpp"
#include "dotto/window.hpp"

// Prints GL errors
void gl_debug_callback(
    GLenum source,
    GLenum type,
    GLuint id,
    GLenum severity,
    GLsizei length,
    const GLchar *message,
    const void *userParam
) {
    if (severity == GL_DEBUG_SEVERITY_NOTIFICATION)
        return;

    std::cerr << "[OPENGL:(" << source << ", " << type << ", " << id << ", "
        << severity << ")]: " << message << "\n";
}

int main(int argc, char** argv) {
    // Create window.
    dotto::window wnd(false);
    wnd.make_current();
    wnd.show();

    // Initialize GLEW.
    if (glewInit() != GLEW_OK) {
        std::cerr << "Failed to initialize GLEW.\n";
        return EXIT_FAILURE;
    }

    glEnable(GL_DEBUG_OUTPUT);
    glDebugMessageCallback(gl_debug_callback, nullptr);

    // Creates a new scene.
    dotto::scene intro_scene;
    intro_scene.objects.emplace_back(new dotto::rectangle);
    intro_scene.objects.emplace_back(new dotto::rectangle);

    // For timings.
    using namespace std::chrono_literals;
    using clock = std::chrono::high_resolution_clock;
    constexpr std::chrono::nanoseconds timestep(16ms);
    float delta_time;

    // Perform window actions.
    while (wnd.is_open()) {
        // Start clock.
        auto start = clock::now();

        // Check if we should escape the window.
        if (glfwGetKey((GLFWwindow*)wnd, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose((GLFWwindow*)wnd, true);

        // Clear and render.
        intro_scene.render();

        // Swap back and front buffer.
        wnd.swap_buffers();
        wnd.poll_events();

        // Calculate Delta time.
        auto end = clock::now();
        auto length_ns = end - start;
        auto wait_ns = timestep - length_ns;
        std::this_thread::sleep_for(wait_ns);
        delta_time = (wait_ns + length_ns).count() / 1000000000.0;
    }

    return EXIT_SUCCESS;
}
