#version 330 core

layout(location = 0) in vec3 vertexPosition_modelspace;
layout(location = 1) in vec2 texture_coord;

out vec2 tex_coord;

uniform mat4 MVP;

void main(){
    // Output position of the vertex, in clip space : MVP * position
    gl_Position =  MVP * vec4(vertexPosition_modelspace, 1);
    tex_coord = texture_coord;
}
