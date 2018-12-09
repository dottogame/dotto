#version 330 core

out vec4 a_color;

in vec2 a_tex_coord;

uniform sampler2D u_texture;

void main()
{
    a_color = texture(u_texture, a_tex_coord);
}
