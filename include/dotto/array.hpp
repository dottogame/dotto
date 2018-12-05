#pragma once
#include "pch.h"

namespace dotto::model {
    /* This class is designated for defining a vertex array that multiple vertex
     * buffers can be assigned to and controlled.
     */
    class array final {
        /* The id of this vertex array. */
        GLuint m_id;

    public:
        /* Constructs a new vertex array. */
        array() {
            glGenVertexArrays(1, &m_id);
        }

        /* Copy constructor. */
        array(const array& other) :
            m_id(other.m_id)
        {
        }

        /* Move constructor. */
        array(array&& other) :
            m_id(0)
        {
            std::swap(m_id, other.m_id);
        }

        /* Deconstructs this vertex array. */
        ~array() {
            glDeleteVertexArrays(1, &m_id);
        }

        /* Attributes a pointer in the array to handle data a specific way. */
        inline void attrib_pointer(
            const GLuint& attr,
            const GLuint& length,
            const GLuint& type,
            const GLuint& stride,
            const void* offset
        ) {
            glEnableVertexAttribArray(attr);
            glVertexAttribPointer(attr, length, type, false, stride, offset);
            glDisableVertexAttribArray(attr);
        }

        /* Binds this vertex array to the target. */
        inline void bind() {
            glBindVertexArray(m_id);
        }
    };
}