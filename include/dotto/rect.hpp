#include "pch.h"

namespace dotto::ui
{
    static const GLfloat rect_verts[] = {
       -1.0f, -1.0f, 0.0f,
        1.0f, -1.0f, 0.0f,
        1.0f,  1.0f, 0.0f,
       -1.0f,  1.0f, 0.0f
    };

    static const GLuint rect_indices[] = {0, 1, 2, 0, 2, 3};

    struct rect
    {
        GLuint prog_id;
        GLuint vertexbuffer;
        GLuint elementbuffer;

        glm::mat4 model_matrix = glm::mat4(1.0f);

        rect(GLuint program_id)
        {
            prog_id = program_id;

            // put default rect data in vertex buffer
            glGenBuffers(1, &vertexbuffer);
            glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
            glBufferData(GL_ARRAY_BUFFER, sizeof(rect_verts), rect_verts, GL_STATIC_DRAW);

            // put default rect data in index buffere
            glGenBuffers(1, &elementbuffer);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, 6 * sizeof(unsigned int), rect_indices, GL_STATIC_DRAW);
        }

        void render(glm::mat4 view_projection_matrix)
        {
            // pre compute
            glm::mat4 mvp = view_projection_matrix * model_matrix;

            // render
            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
            glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, (void*) 0);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementbuffer);

            glUseProgram(prog_id);
            // pass mvp
            GLuint MatrixID = glGetUniformLocation(prog_id, "MVP");
            glUniformMatrix4fv(MatrixID, 1, GL_FALSE, &mvp[0][0]);

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void*) 0);

            glDisableVertexAttribArray(0);
        }
    };
}
