#pragma once

#include "../pch.h"
#include "texture.hpp"

namespace dotto::ui
{
    static const GLfloat rect_verts[] = {
       -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
        1.0f, -1.0f, 0.0f, 1.0f, 0.0f,
        1.0f,  1.0f, 0.0f, 1.0f, 1.0f,
       -1.0f,  1.0f, 0.0f, 0.0f, 1.0f
    };

    static const GLuint rect_indices[] = {0, 1, 2, 0, 2, 3};

    struct rect
    {
    private:
        GLuint prog_id;
        GLuint vertexbuffer;
        GLuint indexbuffer;

        glm::mat4 get_model_matrix()
        {
            return transform * rotation * scale;
        }

    public:
        glm::mat4 transform = glm::mat4(1.0f);
        glm::mat4 rotation = glm::mat4(1.0f);
        glm::mat4 scale = glm::mat4(1.0f);

        texture* tex;

        rect(GLuint program_id, texture* texture)
            : prog_id(program_id)
            , tex(texture)
        {
            // put default rect data in vertex buffer
            glGenBuffers(1, &vertexbuffer);
            glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
            glBufferData(GL_ARRAY_BUFFER, sizeof(rect_verts), rect_verts, GL_STATIC_DRAW);

            // put default rect data in index buffere
            glGenBuffers(1, &indexbuffer);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexbuffer);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, 6 * sizeof(unsigned int), rect_indices, GL_STATIC_DRAW);
        }

        void render(glm::mat4 projection_matrix, glm::mat4 view_matrix)
        {
            // pre compute
            glm::mat4 mvp = projection_matrix * view_matrix * get_model_matrix();

            // render
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);

            glVertexAttribPointer(
                0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(GLfloat),
                (void*) 0
            );

            glVertexAttribPointer(
                1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(GLfloat),
                (void*)(3 * sizeof(GLfloat))
            );

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexbuffer);

            glUseProgram(prog_id);
            tex->bind();

            // pass mvp
            GLuint MatrixID = glGetUniformLocation(prog_id, "MVP");
            glUniformMatrix4fv(MatrixID, 1, GL_FALSE, &mvp[0][0]);

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
        }
    };
}
