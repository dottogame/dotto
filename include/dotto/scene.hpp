#pragma once
#include "pch.h"
#include "model.hpp"
#include "camera.hpp"

namespace dotto {
    /* This designates a scene object which is responsible for loading in scene
     * assets.
     */
    struct scene {
        /* The objects in the scene. */
        std::vector<model*> objects;

        /* Creates a new scene. */
        scene() {
            camera::transform.set_position(0.0f, 0.0f, 5.0f);
            glm::fmat4 projection = camera::projection();
            glm::fmat4 view = camera::looking_at();

            for (auto& mod : objects) {
                mod->mesh.shader.set_uniform(uniform::fmat4, "u_proj", &projection[0][0]);
                mod->mesh.shader.set_uniform(uniform::fmat4, "u_view", &view[0][0]);
            }
        }

        /* Copy constructor. */
        scene(const scene& other) :
            objects(other.objects)
        {
        }

        /* Move constructor. */
        scene(scene&& other) {
            std::swap(objects, other.objects);
        }

        /* Deconstructs this scene object. */
        virtual ~scene() = default;

        /* Renders each object. */
        void render() {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            for (auto& mod : objects) {
                glm::fmat4 _model = mod->transform;
                mod->mesh.shader.bind();

                GLuint a_pos = mod->mesh.shader.get_attrib("a_pos");
                GLuint a_col = mod->mesh.shader.get_attrib("a_col");
                // GLuint a_uvs = mod->mesh.shader.get_attrib("a_uvs");

                mod->mesh.array.attrib_pointer(
                    a_pos,
                    3,
                    GL_FLOAT,
                    7 * sizeof(GLfloat),
                    nullptr
                );

                mod->mesh.array.attrib_pointer(
                    a_col,
                    4,
                    GL_FLOAT,
                    7 * sizeof(GLfloat),
                    (void*)(3 * sizeof(GLfloat))
                );

                // mod->mesh.array.attrib_pointer(
                //     a_uvs,
                //     2,
                //     GL_FLOAT,
                //     9 * sizeof(GLfloat),
                //     (void*)(5 * sizeof(GLfloat))
                // );

                mod->mesh.shader.set_uniform(
                    uniform::fmat4,
                    "u_model",
                    &_model[0][0]
                );

                mod->mesh.array.bind();
                mod->mesh.array.enable_attrib(a_pos, true);
                mod->mesh.array.enable_attrib(a_col, true);
                mod->mesh.indices.bind();

                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, nullptr);

                mod->mesh.array.enable_attrib(a_pos, false);
                mod->mesh.array.enable_attrib(a_col, false);
            }
        }
    };
}
