#pragma once
#include "pch.h"

namespace dotto::model {
    /* This class designates a buffer for storing vertices or indices. */
    template<typename T>
    class buffer final {
        /* The type of this buffer. */
        GLuint m_type;

        /* The id for this buffer. */
        GLuint m_id;

        /* The vertices for this buffer. */
        std::vector<T> m_data;

        /* The draw type for this buffer. */
        GLuint m_draw;

    public:
        /* Constructs a new buffer of the given type. */
        buffer(const GLuint& type) :
            buffer(type, 4)
        {
        }

        /* Constructs a new buffer of the given type and size. */
        explicit buffer(const GLuint& type, const int& size) :
            m_type(type),
            m_id(0),
            m_data(size),
            m_draw(GL_STATIC_DRAW)
        {
            glGenBuffers(1, &m_id);
        }

        /* Copy constructor. */
        buffer(const buffer& other) :
            m_type(other.m_type),
            m_id(other.m_id),
            m_data(other.m_data),
            m_draw(other.m_draw)
        {
        }

        /* Move constructor. */
        buffer(buffer&& other) :
            m_id(0),
            m_data()
        {
            std::swap(m_type, other.m_type);
            std::swap(m_id, other.m_id);
            std::swap(m_data, other.m_data);
            std::swap(m_draw, other.m_draw);
        }

        /* Deconstructs this buffer. */
        ~buffer() {
            glDeleteBuffers(1, &m_id);
        }

        /* Binds this buffer. */
        inline void bind() {
            glBindBuffer(m_type, m_id);
        }

        /* Clears this buffer. */
        inline void clear() {
            m_data.clear();
        }

        /* Passes the data to OpenGL. */
        inline void glify() {
            glBindBuffer(m_type, m_id);
            glBufferData(m_type, m_data.size() * sizeof(T), m_data, m_draw);
        }

        /* Pushes all of the provided data. */
        inline void push_all(T* _data) {
            m_data.emplace_back(_data);
        }

        /* Gives this buffer a new peice of data. */
        inline void push_data(T _data) {
            m_data.emplace_back(_data);
        }
    };

    /* Specializations. */
    template class buffer<signed char>;
    template class buffer<unsigned char>;
    template class buffer<signed short>;
    template class buffer<unsigned short>;
    template class buffer<signed int>;
    template class buffer<unsigned int>;
    template class buffer<float>;
    template class buffer<double>;
}
