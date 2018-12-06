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
            m_id(UINT32_MAX)
        {
            std::swap(m_id, other.m_id);
        }

        /* Deconstructs this vertex array. */
        ~array() {
            glDeleteVertexArrays(1, &m_id);
        }

        /* Implicit cast to GLuint. */
        operator GLuint() {
            return m_id;
        }

        /* Attributes a pointer in the array to handle data a specific way. */
        inline void attrib_pointer(
            const GLuint& attr,
            const GLuint& length,
            const GLuint& type,
            const GLuint& stride,
            const void* offset
        ) {
            glVertexAttribPointer(attr, length, type, false, stride, offset);
        }

        /* Binds this vertex array to the target. */
        inline void bind() {
            glBindVertexArray(m_id);
        }

        /* Enables or disables an attribute. */
        inline void enable_attrib(const GLuint& attrib, const bool&& enbl) {
            if (enbl)
                glEnableVertexAttribArray(attrib);
            else
                glDisableVertexAttribArray(attrib);
        }
    };
}