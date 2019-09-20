#pragma once
#include <glfw/glfw3.h>

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
  VulkanRenderer(VulkanRenderer&&) noexcept;
  VulkanRenderer& operator=(VulkanRenderer&&) noexcept;

  explicit VulkanRenderer(GLFWwindow*);

  void initialize() override;
  void terminate()  override;

private:
  VulkanRenderer(const VulkanRenderer&)            = delete;
  VulkanRenderer& operator=(const VulkanRenderer&) = delete;

  void createInstance();
  void getWindowSurface();
  void pickPhysicalDevice();
  void createLogicalDevice();
  void createSwapChain();
  void createImageViews();

private:
  std::vector<VkImage>     mSwapChainImages;
  std::vector<VkImageView> mSwapChainImageViews;

  GLFWwindow*      mWindow         = nullptr;
  VkInstance       mDriverInstance = nullptr;
  VkSurfaceKHR     mWindowSurface  = nullptr;
  VkPhysicalDevice mPhysicalDevice = nullptr;
  VkDevice         mLogicalDevice  = nullptr;
  VkQueue          mGraphicsQueue  = nullptr;
  VkQueue          mPresentQueue   = nullptr;
  VkSwapchainKHR   mSwapChain      = nullptr;

#if _DEBUG
  VkDebugUtilsMessengerEXT            mDebugMessenger               = nullptr;
  PFN_vkCreateDebugUtilsMessengerEXT  CreateDebugUtilsMessengerEXT  = nullptr;
  PFN_vkDestroyDebugUtilsMessengerEXT DestroyDebugUtilsMessengerEXT = nullptr;
#endif /* _DEBUG */

  VkExtent2D mSwapChainExtent;
  VkFormat   mSwapChainImageFormat;
};
