#ifndef _DOTTO_PROGRAM_HEADER
#define _DOTTO_PROGRAM_HEADER
#include "shader.h"

namespace dotto {
    /* This class is designated to handle any and all shader related code. */
    class program final {
        /* The id for this shader program. */
        GLuint m_program;

        /* The ids for the shaders used by this program. */
        std::vector<shader> m_shaders;

        /* A map of the attributes available or used by this shader. */
        std::unordered_map<std::string, GLint> m_attribs;

        /* A map of the uniforms available or used by this shader. */
        std::unordered_map<std::string, GLint> m_uniforms;

    public:
        /* Constructs a default program. */
        program();

        /* Copy constructor. */
        program(const program& other);

        /* Move constructor. */
        program(program&& other);

        /* Deconstructs this program. */
        ~program();

        /* Copy-swap idiom assignment operator. */
        program& operator=(program other);

        /* Implicit cast to GLuint. */
        operator const GLuint();

        /* Adds a shader to this program. */
        void add_shader(shader _shader);

        /* Binds this program. */
        void bind();

        /* Gets the attribute with the given name. */
        GLuint get_attrib(const char* attrib);

        /* Gets the uniform with the given name. */
        GLuint get_uniform(const char* attrib);

        /* Links this shader program. */
        bool link();

        /* Validates this shader program. */
        bool valid();
    };
}

#endif
