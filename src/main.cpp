#include <dotto.h>

int main(int argc, char** argv) {
    /* Initialize GLFW. */
    if (!glfwInit()) {
        std::cerr << "Failed to initialize GLFW.\n";
        return EXIT_FAILURE;
    }

    // Request OpenGL core version 4.4
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 4);

    // Create window.
    GLFWmonitor* primary    = glfwGetPrimaryMonitor();
    const GLFWvidmode* mode = glfwGetVideoMode(primary);
    GLFWwindow* window      = glfwCreateWindow(
        mode->width,
        mode->height,
        "Dotto",
        primary,
        NULL
    );

    // Check if Window exists.
    if (!window) {
        std::cerr << "Failed to create window.\n";
        return EXIT_FAILURE;
    }

    // Initialize GLEW.
    if (glewInit() != GLEW_OK) {
        std::cerr << "Failed to initialize GLEW.\n";
        return EXIT_FAILURE;
    }

    // Perform window actions.
    while (!glfwWindowShouldClose(window)) {
        glfwPollEvents();

        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(window, true);

        glfwSwapBuffers(window);
    }

    // Destroy window if it still exists.
    if (window)
        glfwDestroyWindow(window);

    // Terminate GLFW.
    glfwTerminate();

    return EXIT_SUCCESS;
}
