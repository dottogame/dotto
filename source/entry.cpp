#include <dotto/application.hpp>
#include <dotto/graphics/renderer.hpp>

namespace Dotto {
  int main(const std::vector<const char*>&);
}

int Dotto::main(const std::vector<const char*>& arguments) {
  if (!glfwInit()) {
    std::cerr << "Dotto: Failed to initialize window service.\n";
    std::exit(1);
  }

  Application* appPtr = Application::getInstance();
  Application& app    = *appPtr;
  GLFWwindow*  wnd    = Application::getWindow();

  Graphics::VulkanRenderer renderer;

  glfwShowWindow(wnd);
  while (!glfwWindowShouldClose(wnd)) {
    glfwPollEvents();
  }

  glfwTerminate();
  return EXIT_SUCCESS;
}

int main(int argc, char** argv) {
  return Dotto::main(std::vector<const char*>(&argv[0], &argv[argc]));
}
