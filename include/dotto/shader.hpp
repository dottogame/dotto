#pragma once
#include "pch.h"
#include "io.hpp"
#include "uniform_types.h"

namespace dotto {
    /* This struct represents a single shader source file that will have been
     * compiled and is prepared for linking.
     */
    class shader final {
        /* The id of this shader. */
        GLint m_id;

        /* The source code for this shader. */
        std::string m_code;

    public:
        // Constructs a new shader from the source at the given path.
        shader(const std::string& path, const GLuint& type) :
            m_id(glCreateShader(type))
        {
            GLint compiled = 0;

            io::file::to_string(m_code, path.c_str());
            const GLchar* const source = m_code.c_str();
            const GLint length = m_code.size();
            glShaderSource(m_id, 1, &source, &length);
            glCompileShader(m_id);
            glGetShaderiv(m_id, GL_COMPILE_STATUS, &compiled);

            if (!compiled) {
                GLint max_length = 0;
                glGetShaderiv(m_id, GL_INFO_LOG_LENGTH, &max_length);

                std::vector<GLchar> err(max_length);
                glGetShaderInfoLog(m_id, max_length, &max_length, &err[0]);

                std::cerr << "[OpenGL] : " << &err[0] << "\n";

                // Force no link.
                m_id = -1;
            }
        }

        // Copy constructor.
        shader(const dotto::shader& other) :
            m_id(other.m_id),
            m_code(other.m_code)
        {
        }

        // Move constructor.
        shader(dotto::shader&& other) {
            this->swap(other);
        }

        // Deconstructs this shader.
        ~shader() {
        }

        // Copy-swap idiom assignment operator.
        inline shader& operator=(dotto::shader other) {
            this->swap(other);
            return *this;
        }

        // Implicit GLuint cast operator.
        inline operator GLuint() {
            return m_id;
        }

        // Checks that this shader is valid.
        inline bool valid() {
            return m_id != -1;
        }

        /* Swaps one shader with the other. */
        void swap(shader& other) {
            // ADL
            using std::swap;

            // swap
            swap(m_id, other.m_id);
            swap(m_code, other.m_code);
        }
    };
}
