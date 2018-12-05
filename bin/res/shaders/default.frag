#version 430

// The color from our vertex.
in vec4 f_col;

// Our final color.
out vec4 final_color;

// Main.
void main() {
	final_color = f_col;
}
