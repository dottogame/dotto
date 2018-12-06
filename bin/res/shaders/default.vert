#version 330

// Attributes.
in vec3 a_pos;
in vec4 a_col;

// Things we need to make our mvp.
uniform mat4 u_proj;
uniform mat4 u_view;
uniform mat4 u_model;

// Our fragment color.
out vec4 f_col;

// Main.
void main() {
	mat4 mvp = u_proj * u_view * u_model;
	gl_Position = (mvp * vec4(a_pos, 1.0f));
	f_col = a_col;
}
