#include "dotto/pch.h"
#include "dotto/mesh.hpp"
#include "dotto/program.hpp"
#include "dotto/window.hpp"

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

    // Create mesh.
    dotto::mesh mesh;
    mesh.vertices.push_all(vertices);

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
    mesh.shaders.push_back(prog);

    GLuint a_pos = prog.get_attrib("a_pos");
    GLuint a_col = prog.get_attrib("a_col");
    mesh.array.attrib_pointer(a_pos, 3, GL_FLOAT, 7 * sizeof(GLfloat), nullptr);
    mesh.array.attrib_pointer(a_col, 4, GL_FLOAT, 7 * sizeof(GLfloat), (void*)(3 * sizeof(GLfloat)));

    // Perform window actions.
    while (wnd.is_open()) {
        // Check if we should escape the window.
        if (glfwGetKey((GLFWwindow*)wnd, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose((GLFWwindow*)wnd, true);

        // Clear and render.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prog.bind();
        mesh.array.bind();
        glEnableVertexAttribArray(a_pos);
        glEnableVertexAttribArray(a_col);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDisableVertexAttribArray(a_pos);
        glDisableVertexAttribArray(a_col);

        // Swap back and front buffer.
        wnd.swap_buffers();
        wnd.poll_events();
    }

    return EXIT_SUCCESS;
}
