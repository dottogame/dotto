#version 330 core

out vec4 color;

in vec2 tex_coord;

uniform sampler2D texture;

void main()
{
    color = texture(texture, tex_coord);
}
