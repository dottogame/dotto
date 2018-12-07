#pragma once
#include "pch.h"
#include "../stb/stb_image.h"

namespace dotto {
	/* This class designates what can be considered a texture. */
	class texture final {
		/* Texture width. */
		GLuint m_width;

		/* Texture height. */
		GLuint m_height;

		/* Texture bits per pixel. */
		GLuint bpp;

		/* Texture location pointer to start. */
		GLuint* m_loc_start;

		/* Texture location pointer to end. */
		GLuint* m_loc_end;

	public:
		/* Constructs a new texture. */
		texture(const char* path) {
			// stbi_load(path,);
		}
	};
}