#include <dotto/application.hpp>

Dotto::Version::Version(uint32_t val): value(val) {}
Dotto::Version::Version(uint8_t maj, uint8_t min, uint16_t pat): major(maj), minor(min), patch(pat) {}
Dotto::Version::operator uint32_t() const noexcept { return value; }
Dotto::Application* Dotto::Application::getInstance() {
  if (!mInstance)
    mInstance = new Application();
  return mInstance;
}

GLFWwindow* Dotto::Application::getWindow() {
  return mWindow;
}

Dotto::Application::~Application() {
  glfwDestroyWindow(mWindow);
  delete mInstance;
}

Dotto::Application::Application() {
  glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
  glfwWindowHint(GLFW_RESIZABLE,  GLFW_FALSE);
  glfwWindowHint(GLFW_VISIBLE,    GLFW_FALSE);

        int32_t      count;
  const GLFWvidmode* mode   = glfwGetVideoMode(glfwGetPrimaryMonitor());
  const uint32_t     width  = mode->width;
  const uint32_t     height = mode->height;
  const uint32_t     xpos   = (width -  1280) / 2;
  const uint32_t     ypos   = (height -  720) / 2;

  mWindow = glfwCreateWindow(1280, 720, windowName.data(), nullptr, nullptr);
  glfwSetWindowPos(mWindow, xpos, ypos);
}
