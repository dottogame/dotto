#pragma once
#include "buffer.hpp"
#include "array.hpp"
#include "program.hpp"

namespace dotto {
    /* Constructs a mesh for the game, specifically, dotto should generate
     * rectangular meshes to store images in.
     */
    struct mesh {
        /* The mesh vertex array, for multiple mesh parts, makes a connected
         * mesh.
         */
        array array;

        /* The mesh vertex data. */
        buffer<GLfloat> vertices;

        /* The mesh indice data. */
        buffer<GLuint> indices;

        /* The currently attached shaders to this mesh. */
        program shader;

        /* Constructs a new mesh. */
        mesh() = default;

        /* Copy constructor. */
        mesh(const mesh& other) :
            array(other.array),
            vertices(other.vertices),
            indices(other.indices),
            shader(other.shader)
        {
        }

        /* Move constructor. */
        mesh(mesh&& other) {
            this->swap(other);
        }

        /* Deconstructs this mesh. */
        ~mesh() {
        }

        /* Copy-swap idiom assignment operator. */
        mesh& operator=(mesh other) {
            this->swap(other);
            return *this;
        }

        /* Swaps one mesh with another. */
        void swap(mesh& other) {
            // ADL
            using std::swap;

            // swap
            swap(array, other.array);
            swap(vertices, other.vertices);
            swap(indices, other.indices);
            swap(shader, other.shader);
        }
    };
}
