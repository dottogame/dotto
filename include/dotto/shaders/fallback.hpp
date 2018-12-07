#pragma once
#include "../program.hpp"

namespace dotto::shaders {
    /* This is the fallback shader, it should be used no other shader is
     * provided.
     */
    class fallback final : public program {

        /* Copy constructor. */
        fallback(const fallback& other) = default;

        /* Move constructor. */
        fallback(fallback&& other) = default;

    public:
        /* Constructs a new fallback program. */
        fallback() {
            shader vert("res/shaders/default.vert", GL_VERTEX_SHADER);
            shader frag("res/shaders/default.frag", GL_FRAGMENT_SHADER);

            if (!vert.valid() || !frag.valid()) {
                std::cerr << "[Dotto] : Shader compilation failed!\n";
                return;
            }

            add_shader(std::move(vert));
            add_shader(std::move(frag));
            link();

            if (!valid()) {
                std::cerr << "[Dotto] : Shader program link failed!\n";
                return;
            }
        }

        /* Deconstructs this fallback. */
        ~fallback() = default;
    };
}
