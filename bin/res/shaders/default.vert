#version 330 core

layout(location = 0) in vec3 a_pos;
layout(location = 1) in vec2 a_texture_coord;

out vec2 a_tex_coord;

uniform mat4 u_mvp;

void main() {
    gl_Position = u_mvp * vec4(a_pos, 1.0f);
    a_tex_coord = a_texture_coord;
}
