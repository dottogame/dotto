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

    // The rendering queue for the game.
    std::vector<dotto::model> rendering_queue;

    // Rectangle.
    dotto::rectangle rect;
    dotto::rectangle rect2;

    rect.transform.set_position(glm::fvec3(1.0f, 1.0f, 0.0));
    rect.transform.set_rotation(glm::fvec3(0.0f , 0.0f, 0.0f));

    rendering_queue.emplace_back(std::move(rect));
    rendering_queue.emplace_back(std::move(rect2));

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
    dotto::camera::transform.set_position(0, 0, 5.0f);
    glm::fmat4 projection   = dotto::camera::projection();
    glm::fmat4 view         = dotto::camera::looking_at();
    prog.set_uniform(dotto::uniform::fmat4, "u_proj", &projection[0][0]);
    prog.set_uniform(dotto::uniform::fmat4, "u_view", &view[0][0]);

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

        for (auto& mod : rendering_queue) {
            glm::fmat4 _model = mod.transform;
            prog.set_uniform(dotto::uniform::fmat4, "u_model", &_model[0][0]);

            mod.mesh.array.bind();
            mod.mesh.array.enable_attrib(a_pos, true);
            mod.mesh.array.enable_attrib(a_col, true);
            mod.mesh.indices.bind();

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, nullptr);

            mod.mesh.array.enable_attrib(a_pos, false);
            mod.mesh.array.enable_attrib(a_col, false);
        }

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
