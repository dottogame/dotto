#ifndef _DOTTO_SHADER_HEADER
#define _DOTTO_SHADER_HEADER
#include "../pch.h"

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
        /* Constructs a new shader from the source at the given path. */
        explicit shader(const std::string& path, const GLuint& type);

        /* Copy constructor. */
        shader(const shader& other);

        /* Move constructor. */
        shader(shader&& other);

        /* Deconstructs this shader. */
        ~shader();

        /* Copy-swap idiom assignment operator. */
        shader& operator=(shader other);

        /* Implicit GLuint cast operator. */
        operator const GLuint();

        /* Checks that this shader is valid. */
        bool valid();
    };
}

#endif