#include <dotto/shading/program.h>

// Constructs a default shader.
dotto::program::program() :
    m_program(-1)
{
}

// Copy constructor.
dotto::program::program(const dotto::program& other) :
    m_program(other.m_program),
    m_shaders(other.m_shaders),
    m_attribs(other.m_attribs),
    m_uniforms(other.m_uniforms)
{
}

// Move constructor.
dotto::program::program(dotto::program&& other) :
    m_program(-1)
{
    std::swap(m_program, other.m_program);
    std::swap(m_shaders, other.m_shaders);
    std::swap(m_attribs, other.m_attribs);
    std::swap(m_uniforms, other.m_uniforms);
}

// Copy-swap idiom assignment operator.
dotto::program& dotto::program::operator=(dotto::program other) {
    std::swap(m_program, other.m_program);
    std::swap(m_shaders, other.m_shaders);
    std::swap(m_attribs, other.m_attribs);
    std::swap(m_uniforms, other.m_uniforms);
    return *this;
}

// Deconstructs this program.
dotto::program::~program() {
    for (dotto::shader& shdr : m_shaders)
        glDeleteShader(shdr);

    glDeleteProgram(m_program);
}

// Implicit cast to GLuint.
dotto::program::operator const GLuint() {
    return m_program;
}

// Adds a shader to this program.
void dotto::program::add_shader(shader _shader) {
    m_shaders.emplace_back(std::move(_shader));
}

// Binds this program.
void dotto::program::bind() {
    glUseProgram(m_program);
}

// Links this shader program.
bool dotto::program::link() {
    m_program = glCreateProgram();

    for (dotto::shader& shdr : m_shaders) {
        if (!shdr.valid()) return false;

        glAttachShader(m_program, shdr);
    }

    GLint linked = 0;
    glLinkProgram(m_program);
    glGetProgramiv(m_program, GL_LINK_STATUS, &linked);

    if (!linked) {
        GLint max_length = 0;
        glGetProgramiv(m_program, GL_INFO_LOG_LENGTH, &max_length);

        std::vector<GLchar> err(max_length);
        glGetProgramInfoLog(m_program, max_length, &max_length, &err[0]);

        std::cerr << &err[0] << "\n";

        for (dotto::shader& shdr : m_shaders) {
            glDetachShader(m_program, shdr);
            glDeleteShader(shdr);
            glDeleteProgram(m_program);
        }

        return false;
    }

    for (dotto::shader& shdr : m_shaders)
        glDetachShader(m_program, shdr);

    return true;
}

// Validates this shader program.
bool dotto::program::valid() {
    GLint valid = 0;
    glValidateProgram(m_program);
    glGetProgramiv(m_program, GL_VALIDATE_STATUS, &valid);

    if (!valid) {
        GLint max_length = 0;
        glGetProgramiv(m_program, GL_INFO_LOG_LENGTH, &max_length);

        std::vector<GLchar> err(max_length);
        glGetProgramInfoLog(m_program, max_length, &max_length, &err[0]);

        std::cerr << &err[0] << "\n";

        for (dotto::shader& shdr : m_shaders) {
            glDetachShader(m_program, shdr);
            glDeleteShader(shdr);
            glDeleteProgram(m_program);
        }

        return false;
    }

    return true;
}

// Gets the attribute with the given name.
GLuint dotto::program::get_attrib(const char* attrib) {
    GLuint res = -1;
    const auto& itr = m_attribs.find(attrib);

    if (itr == m_attribs.end()) {
        res = glGetAttribLocation(m_program, attrib);
        m_attribs.emplace(std::make_pair(attrib, res));
    } else {
        res = itr->second;
    }

    return res;
}

// Gets the uniform with the given name.
GLuint dotto::program::get_uniform(const char* attrib) {
    GLuint res = -1;
    const auto& itr = m_uniforms.find(attrib);

    if (itr == m_uniforms.end()) {
        res = glGetAttribLocation(m_program, attrib);
        m_uniforms.emplace(std::make_pair(attrib, res));
    } else {
        res = itr->second;
    }

    return res;
}