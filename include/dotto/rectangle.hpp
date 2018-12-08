#pragma once
#include "model.hpp"

namespace dotto {
    /* Test object. */
    struct rectangle final : public model {
        /* Constructor. */
        rectangle() {
            // Rectangle vertices.
            GLfloat vertices[28] = {
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // 0.0f, 0.5f,
                 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // 0.0f, 0.3f,
                 0.5f,  0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // 0.7f, 0.1f,
                -0.5f,  0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, // 0.9f, 1.0f
            };

            // Rectangle indices.
            GLuint indices[6] = {
                0, 1, 2,
                2, 3, 0
            };

            mesh.array.glify();
            mesh.array.bind();
            mesh.vertices.set_type(GL_ARRAY_BUFFER);
            mesh.vertices.push_all(vertices, 28);
            mesh.indices.set_type(GL_ELEMENT_ARRAY_BUFFER);
            mesh.indices.push_all(indices, 6);
            mesh.vertices.glify();
            mesh.indices.glify();

            transform.set_position(glm::fvec3(0.0f, 0.0f, 0.0f));
            transform.set_rotation(glm::fvec3(0.0f, 0.0f, 0.0f));
            transform.set_scale(glm::fvec3(1.0f, 1.0f, 1.0f));
        }

        /* Disallow copy of this test object. */
        rectangle(const rectangle& other) = delete;

        /* Disallow move of this test object. */
        rectangle(rectangle&& other) = delete;

        /* Deconstructor. */
        ~rectangle() {
        }
    };
}
