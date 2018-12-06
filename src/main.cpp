#include "dotto/pch.h"
#include "dotto/camera.hpp"
#include "dotto/rectangle.hpp"
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

    // Rectangle.
    dotto::rectangle rect;

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
    rect.mesh.array.attrib_pointer(a_pos, 3, GL_FLOAT, 7 * sizeof(GLfloat), nullptr);
    rect.mesh.array.attrib_pointer(a_col, 4, GL_FLOAT, 7 * sizeof(GLfloat), (void*)(3 * sizeof(GLfloat)));

    // Temp cam
    glm::fmat4 projection   = dotto::camera::projection();
    glm::fmat4 view         = dotto::camera::looking_at();
    glm::fmat4 model        = rect.transform();
    prog.set_uniform(dotto::uniform::fmat4, "u_proj", &projection[0][0]);
    prog.set_uniform(dotto::uniform::fmat4, "u_view", &view[0][0]);
    prog.set_uniform(dotto::uniform::fmat4, "u_model", &model[0][0]);

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
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prog.bind();
        rect.mesh.array.bind();
        rect.mesh.array.enable_attrib(a_pos, true);
        rect.mesh.array.enable_attrib(a_col, true);
        rect.mesh.indices.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, nullptr);
        rect.mesh.array.enable_attrib(a_pos, false);
        rect.mesh.array.enable_attrib(a_col, false);

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
