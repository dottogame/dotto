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
        std::vector<program> shaders;

        /* Constructs a new mesh. */
        mesh() :
            array(),
            vertices(GL_ARRAY_BUFFER),
            indices(GL_ELEMENT_ARRAY_BUFFER),
            shaders()
        {
        }

        /* Deconstructs this mesh. */
        ~mesh() {
        }

        /* Swaps this mesh with the other. */
        void swap(mesh& other) {
            array.swap(other.array);
            vertices.swap(other.vertices);
            indices.swap(other.indices);
            std::swap(shaders, other.shaders);
        }
    };
}
