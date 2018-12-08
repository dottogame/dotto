#pragma once
#include "shader.hpp"

namespace dotto {
    /* This class is designated to handle any and all shader related code. */
    class program final {
        /* The id for this shader program. */
        GLint m_program;

        /* The ids for the shaders used by this program. */
        std::vector<shader> m_shaders;

        /* A map of the attributes available or used by this shader. */
        std::unordered_map<const char*, GLint> m_attribs;

        /* A map of the uniforms available or used by this shader. */
        std::unordered_map<const char*, GLint> m_uniforms;

    public:
        // Constructs a default shader.
        program() :
            m_program(-1),
            m_shaders(),
            m_attribs(),
            m_uniforms()
        {
        }

        // Copy constructor.
        program(const dotto::program& other) :
            m_program(other.m_program),
            m_shaders(other.m_shaders),
            m_attribs(other.m_attribs),
            m_uniforms(other.m_uniforms)
        {
        }

        // Move constructor.
        program(dotto::program&& other) :
            m_program(-1)
        {
            this->swap(other);
        }

        // Copy-swap idiom assignment operator.
        dotto::program& operator=(dotto::program other) {
            this->swap(other);
            return *this;
        }

        // Deconstructs this program.
        ~program() {
            for (dotto::shader& shdr : m_shaders)
                glDeleteShader(shdr);

            if (m_program != -1)
                glDeleteProgram(m_program);
        }

        // Implicit cast to GLuint.
        inline operator GLuint() {
            return m_program;
        }

        // Adds a shader to this program.
        inline void add_shader(shader _shader) {
            m_shaders.emplace_back(std::move(_shader));
        }

        // Binds this program.
        inline void bind() {
            glUseProgram(m_program);
        }

        // Links this shader program.
        inline bool link() {
            m_program = glCreateProgram();

            for (dotto::shader& shdr : m_shaders) {
                if (!shdr.valid()) return false;

                glAttachShader(m_program, shdr);
            }

            GLint linked = 0;
            glLinkProgram(m_program);
            glGetProgramiv(m_program, GL_LINK_STATUS, &linked);

            if (!linked) {
                GLint max_length = 0;
                glGetProgramiv(m_program, GL_INFO_LOG_LENGTH, &max_length);

                std::vector<GLchar> err(max_length);
                glGetProgramInfoLog(m_program, max_length, &max_length, &err[0]);

                std::cerr << &err[0] << "\n";

                for (dotto::shader& shdr : m_shaders) {
                    glDetachShader(m_program, shdr);
                    glDeleteShader(shdr);
                    glDeleteProgram(m_program);
                }

                return false;
            }

            for (dotto::shader& shdr : m_shaders)
                glDetachShader(m_program, shdr);

            return true;
        }

        // Validates this shader program.
        inline bool valid() {
            GLint valid = 0;
            glValidateProgram(m_program);
            glGetProgramiv(m_program, GL_VALIDATE_STATUS, &valid);

            if (!valid) {
                GLint max_length = 0;
                glGetProgramiv(m_program, GL_INFO_LOG_LENGTH, &max_length);

                std::vector<GLchar> err(max_length);
                glGetProgramInfoLog(m_program, max_length, &max_length, &err[0]);

                std::cerr << &err[0] << "\n";

                for (dotto::shader& shdr : m_shaders) {
                    glDetachShader(m_program, shdr);
                    glDeleteShader(shdr);
                    glDeleteProgram(m_program);
                }

                return false;
            }

            return true;
        }

        // Gets the attribute with the given name.
        inline GLint get_attrib(const char* attrib) {
            GLint res = -1;
            const auto& itr = m_attribs.find(attrib);

            if (itr == m_attribs.end()) {
                res = glGetAttribLocation(m_program, attrib);
                m_attribs.emplace(std::make_pair(attrib, res));
            } else {
                res = itr->second;
            }

            return res;
        }

        // Gets the uniform with the given name.
        inline GLint get_uniform(const char* attrib) {
            GLint res = -1;
            const auto& itr = m_uniforms.find(attrib);

            if (itr == m_uniforms.end()) {
                res = glGetUniformLocation(m_program, attrib);
                m_uniforms.emplace(std::make_pair(attrib, res));
            } else {
                res = itr->second;
            }

            return res;
        }

        /* Sets a uniform of a given type. */
        template<typename T>
        inline void set_uniform(const uniform&& type, const GLchar* name, const T* data) {
            switch (type) {
            case uniform::fmat4: {
                GLint loc = get_uniform(name);
                glUniformMatrix4fv(loc, 1, false, data);
                break;
            }
            }
        }

        /* Swaps one program with the other. */
        void swap(program& other) {
            // ADL
            using std::swap;

            // Swap members.
            swap(m_program, other.m_program);
            swap(m_shaders, other.m_shaders);
            swap(m_attribs, other.m_attribs);
            swap(m_uniforms, other.m_uniforms);
        }
    };
}