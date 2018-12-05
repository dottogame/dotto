#version 430

// Attributes.
in vec3 a_pos;
in vec4 a_col;

// Our fragment color.
out vec4 f_col;

// Main.
void main() {
	gl_Position = vec4(a_pos, 1.0f);
	f_col = a_col;
}
