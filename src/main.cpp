#include <pch.h>
#include <dotto.h>

#ifdef _WIN32
std::string get_working_directory() {
    HMODULE hModule = GetModuleHandleW(NULL);
    char path[MAX_PATH];
    GetModuleFileNameA(hModule, path, MAX_PATH);
    std::string str(path);

    return str.substr(0, str.find_last_of("\\/")) + "\\";
}
#endif

#ifdef linux
std::string get_working_directory() {
    ssize_t count = readlink("/proc/self/exe", result, PATH_MAX);
    const char *path;

    if (count != -1) path = dirname(result);
    else std::cout << "Error calling readlink!" << std::endl;

    std::string str(path);

    return str.substr(0, str.length() - 11);
}
#endif

bool to_string(std::string& target, const char *filename) {
    std::string path(get_working_directory() + filename);

    std::cout << "Reading file: " << path << std::endl;

    std::FILE *fp = std::fopen(path.c_str(), "rb");
    if (fp)
    {
        std::fseek(fp, 0, SEEK_END);
        target.resize(std::ftell(fp));
        std::rewind(fp);
        std::fread(&target[0], 1, target.size(), fp);
        std::fclose(fp);
        return true;
    }

    return false;
}

int main(int argc, char** argv) {
    /* Initialize GLFW. */
    if (!glfwInit()) {
        std::cerr << "Failed to initialize GLFW.\n";
        return EXIT_FAILURE;
    }

    // Request OpenGL core version 4.4
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 4);

    // Create window.
    GLFWmonitor* primary    = glfwGetPrimaryMonitor();
    const GLFWvidmode* mode = glfwGetVideoMode(primary);
    GLFWwindow* window      = glfwCreateWindow(
        640, // mode->width,
        480, // mode->height,
        "Dotto",
        NULL, // primary,
        NULL
    );

    // Check if Window exists.
    if (!window) {
        std::cerr << "Failed to create window.\n";
        return EXIT_FAILURE;
    }

    // Make window current.
    glfwMakeContextCurrent(window);

    // Initialize GLEW.
    if (glewInit() != GLEW_OK) {
        std::cerr << "Failed to initialize GLEW.\n";
        return EXIT_FAILURE;
    }

    // Triangle vertices.
    GLfloat vertices[21] = {
        -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
         0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
         0.0f,  0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f
    };

    // Vertex array object.
    GLuint vao = 0;
    glGenVertexArrays(1, &vao);
    glBindVertexArray(vao);

    // Vertex buffer object.
    GLuint vbo = 0;
    glGenBuffers(1, &vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    // Create, compile, and link shaders.
    GLint is_compiled = 0;
    GLuint vert_shader = glCreateShader(GL_VERTEX_SHADER);
    std::string vert_source_str;
    to_string(vert_source_str, "res/shaders/default.vert");
    const GLchar* const v_sauce = vert_source_str.c_str();
    const GLint v_length = vert_source_str.size();
    glShaderSource(vert_shader, 1, &v_sauce, &v_length);
    glCompileShader(vert_shader);
    glGetShaderiv(vert_shader, GL_COMPILE_STATUS, &is_compiled);
    
    if (!is_compiled) {
        GLint max_length = 0;
        glGetShaderiv(vert_shader, GL_INFO_LOG_LENGTH, &max_length);
        
        std::vector<GLchar> err(max_length);
        glGetShaderInfoLog(vert_shader, max_length, &max_length, &err[0]);
    
        std::cerr << &err[0] << '\n';
        return EXIT_FAILURE;
    }
    
    GLuint frag_shader = glCreateShader(GL_FRAGMENT_SHADER);
    std::string frag_source_str;
    to_string(frag_source_str, "res/shaders/default.frag");
    const GLchar* const f_sauce = frag_source_str.c_str();
    const GLint f_length = frag_source_str.size();
    glShaderSource(frag_shader, 1, &f_sauce, &f_length);
    glCompileShader(frag_shader);
    glGetShaderiv(frag_shader, GL_COMPILE_STATUS, &is_compiled);
    
    if (!is_compiled) {
        GLint max_length = 0;
        glGetShaderiv(frag_shader, GL_INFO_LOG_LENGTH, &max_length);
    
        std::vector<GLchar> err(max_length);
        glGetShaderInfoLog(frag_shader, max_length, &max_length, &err[0]);
    
        std::cerr << &err[0] << "\n";
        return EXIT_FAILURE;
    }
    
    GLuint program = glCreateProgram();
    glAttachShader(program, vert_shader);
    glAttachShader(program, frag_shader);
    glLinkProgram(program);
    
    GLint is_linked = 0;
    glGetProgramiv(program, GL_LINK_STATUS, &is_linked);
    
    if (!is_linked) {
        GLint max_length = 0;
        glGetProgramiv(program, GL_INFO_LOG_LENGTH, &max_length);
    
        std::vector<GLchar> err(max_length);
        glGetProgramInfoLog(program, max_length, &max_length, &err[0]);
    
        std::cerr << &err[0] << "\n";
    
        glDeleteProgram(program);
        glDeleteShader(vert_shader);
        glDeleteShader(frag_shader);
        return EXIT_FAILURE;
    }
    
    glValidateProgram(program);
    GLint is_valid = 0;
    glGetProgramiv(program, GL_VALIDATE_STATUS, &is_valid);
    
    if (!is_valid) {
        GLint max_length = 0;
        glGetProgramiv(program, GL_INFO_LOG_LENGTH, &max_length);
    
        std::vector<GLchar> err(max_length);
        glGetProgramInfoLog(program, max_length, &max_length, &err[0]);
    
        std::cerr << &err[0] << "\n";
    
        glDeleteProgram(program);
        glDeleteShader(vert_shader);
        glDeleteShader(frag_shader);
        return EXIT_FAILURE;
    }

    // Detach shaders
    glDetachShader(program, vert_shader);
    glDetachShader(program, frag_shader);
    glUseProgram(program);

    GLuint a_pos = glGetAttribLocation(program, "a_pos");
    GLuint a_col = glGetAttribLocation(program, "a_col");
    glVertexAttribPointer(a_pos, 3, GL_FLOAT, false, 7 * sizeof(GLfloat), nullptr);
    glVertexAttribPointer(a_col, 4, GL_FLOAT, false, 7 * sizeof(GLfloat), (void*)(3 * sizeof(GLfloat)));

    // Perform window actions.
    while (!glfwWindowShouldClose(window)) {
        // Check if we should escape the window.
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(window, true);

        // Clear and render.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(program);
        glBindVertexArray(vao);
        glEnableVertexAttribArray(a_pos);
        glEnableVertexAttribArray(a_col);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDisableVertexAttribArray(a_pos);
        glDisableVertexAttribArray(a_col);

        // Swap back and front buffer.
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    // Cleanup
    glDeleteBuffers(1, &vbo);
    glDeleteVertexArrays(1, &vao);
    glDeleteShader(vert_shader);
    glDeleteShader(frag_shader);
    glDeleteProgram(program);

    // Destroy window if it still exists.
    if (window)
        glfwDestroyWindow(window);

    // Terminate GLFW.
    glfwTerminate();

    return EXIT_SUCCESS;
}
