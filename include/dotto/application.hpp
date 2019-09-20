#pragma once
#include <cstdint>
#include <iostream>
#include <memory>
#include <string_view>
#include <vector>
#include "glfw/glfw3.h"

namespace Dotto {
  struct Application;
  union  Version;
}

union Dotto::Version {
  Version(uint32_t);
  Version(uint8_t, uint8_t, uint16_t);
  operator uint32_t() const noexcept;

  uint32_t value;

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wpedantic"
  struct {
    uint8_t  major;
    uint8_t  minor;
    uint16_t patch;
  };
#pragma GCC diagnostic pop
};

struct Dotto::Application {
  static Application* getInstance();
  static GLFWwindow*  getWindow();

private:
 ~Application();
  Application();

public:
  inline static const std::string_view name       = "Dotto";
  inline static const std::string_view windowName = "Dotto";
  inline static const Version          version    = Version{ 1, 0, 0 };

private:
  inline static Application* mInstance = nullptr;
  inline static GLFWwindow*  mWindow   = nullptr;
};
