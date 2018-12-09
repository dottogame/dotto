#pragma once

#include <lodepng.h>

#include "../pch.h"
#include "../io.hpp"

namespace dotto::ui
{
    struct texture
    {
        unsigned int width;
        unsigned int height;
        unsigned int channels;

        std::vector<unsigned char> data;

        GLuint id;

        void bind()
        {
            glBindTexture(GL_TEXTURE_2D, id);
        }

        texture(const char* path)
        {
            unsigned error = lodepng::decode(
                data,
                width,
                height,
                io::file::make_relative(path).c_str()
            );

            if (error)
                std::cout << "decoder error " << error << ": " << lodepng_error_text(error) << std::endl;

            glGenTextures(1, &id);
            glBindTexture(GL_TEXTURE_2D, id); // bind()
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, &data[0]);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        }

        ~texture()
        {
            data.clear();
        }
    };
}
