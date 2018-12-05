#include "dotto/pch.h"
#include "dotto/window.hpp"
#include "dotto/program.hpp"

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

    // Triangle vertices.
    GLfloat vertices[21] = {
        -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
         0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
         0.0f,  0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f
    };

    // Vertex array object.
    GLuint vao = 0;
    glGenVertexArrays(1, &vao);
    glBindVertexArray(vao);

    // Vertex buffer object.
    GLuint vbo = 0;
    glGenBuffers(1, &vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    // Create, compile, and link shaders.
    dotto::shader vert("res/shaders/default.vert", GL_VERTEX_SHADER);
    dotto::shader frag("res/shaders/default.frag", GL_FRAGMENT_SHADER);

    if (!vert.valid() || !frag.valid()) {
        std::cerr << "[Dotto] : Shader compilation failed!\n";
        return EXIT_FAILURE;
    }

    dotto::program prog;
    prog.add_shader(std::move(vert));
    prog.add_shader(std::move(frag));
    prog.link();

    if (!prog.valid()) {
        std::cerr << "[Dotto] : Shader program link failed!\n";
        return EXIT_FAILURE;
    }

    prog.bind();

    GLuint a_pos = prog.get_attrib("a_pos");
    GLuint a_col = prog.get_attrib("a_col");
    glVertexAttribPointer(a_pos, 3, GL_FLOAT, false, 7 * sizeof(GLfloat), nullptr);
    glVertexAttribPointer(a_col, 4, GL_FLOAT, false, 7 * sizeof(GLfloat), (void*)(3 * sizeof(GLfloat)));

    // Perform window actions.
    while (wnd.is_open()) {
        // Check if we should escape the window.
        if (glfwGetKey((GLFWwindow*)wnd, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose((GLFWwindow*)wnd, true);

        // Clear and render.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prog.bind();
        glBindVertexArray(vao);
        glEnableVertexAttribArray(a_pos);
        glEnableVertexAttribArray(a_col);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDisableVertexAttribArray(a_pos);
        glDisableVertexAttribArray(a_col);

        // Swap back and front buffer.
        wnd.swap_buffers();
        wnd.poll_events();
    }

    // Cleanup
    glDeleteBuffers(1, &vbo);
    glDeleteVertexArrays(1, &vao);

    return EXIT_SUCCESS;
}
