#pragma once
#include <vulkan/vulkan.h>

namespace Dotto::Graphics {
  struct IRenderer;
  struct OpenGLRenderer; // For later.
  struct VulkanRenderer;
}

/* Responsible for configuring render settings and building the command buffer to be used to render
 * given bound objects with.
 */
struct Dotto::Graphics::IRenderer {
  virtual void initialize() = 0;
  virtual void terminate()  = 0;
};

struct Dotto::Graphics::VulkanRenderer final: Dotto::Graphics::IRenderer {
 ~VulkanRenderer();
  VulkanRenderer();
  VulkanRenderer(const VulkanRenderer&);
  VulkanRenderer(VulkanRenderer&&) noexcept;
  VulkanRenderer& operator=(const VulkanRenderer&);
  VulkanRenderer& operator=(VulkanRenderer&&) noexcept;

  void initialize() override;
  void terminate()  override;

private:
  void createInstance();

private:
  VkInstance mDriverInstance = nullptr;

#if _DEBUG
  VkDebugUtilsMessengerEXT            mDebugMessenger               = nullptr;
  PFN_vkCreateDebugUtilsMessengerEXT  CreateDebugUtilsMessengerEXT  = nullptr;
  PFN_vkDestroyDebugUtilsMessengerEXT DestroyDebugUtilsMessengerEXT = nullptr;
#endif /* _DEBUG */
};
