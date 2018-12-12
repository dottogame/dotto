#version 330 core

out vec4 a_color;

in vec2 a_tex_coord;

uniform sampler2D u_texture;

void main()
{
    vec4 tex_color = texture(u_texture, a_tex_coord);
    if (tex_color.a < 0.1) discard;
    a_color = tex_color;
}
