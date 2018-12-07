#version 330

// The color from our vertex.
in vec4 f_col;
// in vec2 f_uvs;

// Our final color.
out vec4 final_color;

// Main.
void main() {
	final_color = f_col;
}
