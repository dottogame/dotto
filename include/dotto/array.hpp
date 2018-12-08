#pragma once
#include "pch.h"

namespace dotto {
    /* This class is designated for defining a vertex array that multiple vertex
     * buffers can be assigned to and controlled.
     */
    class array final {
        /* The id of this vertex array. */
        GLint m_id;

    public:
        /* Constructs a new vertex array. */
        array() :
            m_id(-1)
        {
        }

        /* Copy constructor. */
        array(const array& other) :
            m_id(other.m_id)
        {
        }

        /* Move constructor. */
        array(array&& other) :
            m_id(-1)
        {
            this->swap(other);
        }

        /* Deconstructs this vertex array. */
        ~array() {
            if (m_id != -1)
                glDeleteVertexArrays(1, (GLuint*)&m_id);
        }

        /* Copy-swap idiom assignment operator. */
        array& operator=(array other) {
            this->swap(other);
            return *this;
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

        /* Initializes a GL version of this class. */
        inline void glify() {
            if (m_id == -1)
                glGenVertexArrays(1, (GLuint*)&m_id);
        }

        /* Enables or disables an attribute. */
        inline void enable_attrib(const GLuint& attrib, const bool&& enbl) {
            if (enbl)
                glEnableVertexAttribArray(attrib);
            else
                glDisableVertexAttribArray(attrib);
        }

        /* Swaps one vertex array with the other. */
        void swap(array& other) {
            // ADL
            using std::swap;

            // swap
            swap(m_id, other.m_id);
        }
    };
}