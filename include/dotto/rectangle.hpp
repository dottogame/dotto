#pragma once

#include "model.hpp"
#include "shaders/fallback.hpp"

namespace dotto {
    /* Actual object. */
    struct rectangle final : public model {
        /*
            Sets the coordsof a rectangle vertex array.
        */
        void set_coords(GLfloat verts[], const texture& tex)
        {
            // TEPMORARY.
            GLfloat width = tex.width() / 1280;
            GLfloat height = tex.height() / 720;

            verts[0 ] = -width;
            verts[1 ] = -height;

            verts[9 ] =  width;
            verts[10] = -height;

            verts[18] =  width;
            verts[19] =  height;

            verts[27] = -width;
            verts[28] =  height;
        }

        /*
            Sets the tint of a rectangle vertex array. Iterates row by row,
            and then sets the r, g, b and a value by value.
        */
        void set_tint (GLfloat verts[], GLfloat colors[])
        {
            // the row of the array we're on
            for (int row = 0; row < 4; row++)
                // the index we're on in the colors
                for (int c_component = 0; c_component < 4; c_component++)
                    verts[(row * 9) + 3 + c_component] = colors[c_component];
        }

        /* Constructor. */
        rectangle(/* const texture& tex*/) {
            // Rectangle vertices.
            // coords                    colors                  tex coords
            GLfloat vertices[] = {
                -0.5f, -0.5f, 0.0f,    1.0f, 1.0f, 1.0f, 1.0f,    // 0.0f, 0.0f,
                 0.5f, -0.5f, 0.0f,    1.0f, 1.0f, 1.0f, 1.0f,    // 1.0f, 0.0f,
                 0.5f,  0.5f, 0.0f,    1.0f, 1.0f, 1.0f, 1.0f,    // 1.0f, 1.0f,
                -0.5f,  0.5f, 0.0f,    1.0f, 1.0f, 1.0f, 1.0f,    // 0.0f, 1.0f
            };

            // set_coords(vertices, tex);
            // set_tint(vertices, new float[4]{ 1.0f, 0.0f, 0.0f, 1.0f });

            // set_texture
            // Rectangle indices.
            GLuint indices[6] = {
                0, 1, 2,
                2, 3, 0
            };

            mesh.shader = std::move(shaders::fallback());
            mesh.array.bind();
            mesh.vertices.push_all(vertices, sizeof(vertices) / sizeof(GLfloat));
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
