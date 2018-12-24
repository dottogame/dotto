#pragma once

#include "io.hpp"

namespace dotto::pipeline
{
    bool program_is_okay(GLuint program_id)
    {
        GLint Result = GL_FALSE;
        int InfoLogLength;
        glGetProgramiv(program_id, GL_LINK_STATUS, &Result);
        glGetProgramiv(program_id, GL_INFO_LOG_LENGTH, &InfoLogLength);
        if (InfoLogLength > 1)
        {
            std::vector<char> ProgramErrorMessage(InfoLogLength+1);
            glGetProgramInfoLog(program_id, InfoLogLength, NULL, &ProgramErrorMessage[0]);
            printf("%s\n", &ProgramErrorMessage[0]);
            return false;
        }

        return true;
    }

    // returns if true if error
    bool load_shader(GLuint shader_id, const char* path)
    {
        std::string shader_code;
        const char* full_path = io::file::make_relative(path).c_str();

        if (!io::file::to_string(shader_code, full_path)) return true;

        char const* src_ptr = shader_code.c_str();

        glShaderSource(shader_id, 1, &src_ptr, NULL);
        glCompileShader(shader_id);

        return false;
    }

    bool shader_is_okay(GLuint shader_id)
    {
        GLint Result = GL_FALSE;
        int InfoLogLength;
        glGetShaderiv(shader_id, GL_COMPILE_STATUS, &Result);
        glGetShaderiv(shader_id, GL_INFO_LOG_LENGTH, &InfoLogLength);
        // TODO check if this test is right
        if (InfoLogLength > 1)
        {
            std::vector<char> VertexShaderErrorMessage(InfoLogLength + 1);
            glGetShaderInfoLog(shader_id, InfoLogLength, NULL, &VertexShaderErrorMessage[0]);
            printf("%s\n", &VertexShaderErrorMessage[0]);
            return false;
        }

        return true;
    }

    GLuint create_program(const char* vertex_file_path, const char* fragment_file_path)
    {
        // Create the shaders
        GLuint VertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        GLuint FragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

        // Load & compile shaders
        if(load_shader(VertexShaderID, vertex_file_path))
            std::cout << "error loading vert shader" << std::endl;

        if(load_shader(FragmentShaderID, fragment_file_path))
            std::cout << "error loading frag shader" << std::endl;

        // Check shaders
        if(!shader_is_okay(VertexShaderID)) std::cout << "oof. vert shader is not okay." << std::endl;
        if(!shader_is_okay(FragmentShaderID)) std::cout << "oof. frag shader is not okay." << std::endl;

        // Link the program
        printf("Linking program\n");
        GLuint program_id = glCreateProgram();
        glAttachShader(program_id, VertexShaderID);
        glAttachShader(program_id, FragmentShaderID);
        glLinkProgram(program_id);

        // Check the program
        if (!program_is_okay(program_id)) std::cout << "oof. program is not okay." << std::endl;

        glDetachShader(program_id, VertexShaderID);
        glDetachShader(program_id, FragmentShaderID);

        // delete the shaders
        glDeleteShader(VertexShaderID);
        glDeleteShader(FragmentShaderID);

        return program_id;
    }
}
