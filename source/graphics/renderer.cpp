#include <array>
#include <cstring>
#include <iostream>
#include <dotto/application.hpp>
#include <dotto/graphics/renderer.hpp>

static const std::array<const char*, 1> validationLayers = { "VK_LAYER_KHRONOS_validation" };
static const std::array<const char*, 1> deviceExtensions = { "VK_KHR_swapchain"            };

static VKAPI_ATTR VkBool32 VKAPI_CALL debugCallback(
        VkDebugUtilsMessageSeverityFlagBitsEXT,
        VkDebugUtilsMessageTypeFlagsEXT,
  const VkDebugUtilsMessengerCallbackDataEXT* pCallbackData,
        void*
) {
  std::cerr << "Validation Layer: " << pCallbackData->pMessage << "\n" << std::flush;
  return false;
}

static bool reportError(VkResult result) {
  switch (result) {
    case VK_SUCCESS: return false;
  }

  return true;
}

static bool hasValidationLayerSupport() {
  auto layerCount = 0u;
  vkEnumerateInstanceLayerProperties(&layerCount, nullptr);

  auto  size            = sizeof(VkLayerProperties) * layerCount;
  auto* availableLayers = reinterpret_cast<VkLayerProperties*>(alloca(size));
  vkEnumerateInstanceLayerProperties(&layerCount, &availableLayers[0]);

  for (const char* layerName: validationLayers)
    for (size_t index = 0; index < layerCount; index++)
      if (strcmp(layerName, availableLayers[index].layerName) == 0)
        return true;
  return false;
}

Dotto::Graphics::VulkanRenderer::~VulkanRenderer() {
  terminate(); }
Dotto::Graphics::VulkanRenderer::VulkanRenderer() {
  initialize(); }
Dotto::Graphics::VulkanRenderer::VulkanRenderer(const Dotto::Graphics::VulkanRenderer& other) {
  initialize(); }
Dotto::Graphics::VulkanRenderer::VulkanRenderer(Dotto::Graphics::VulkanRenderer&& other) noexcept:
  mDriverInstance(std::move(other.mDriverInstance)) {}

Dotto::Graphics::VulkanRenderer& Dotto::Graphics::VulkanRenderer::operator=(const Dotto::Graphics::VulkanRenderer& other) {
  if (&other != this) {
    initialize();
  }

  return *this;
}

Dotto::Graphics::VulkanRenderer& Dotto::Graphics::VulkanRenderer::operator=(Dotto::Graphics::VulkanRenderer&& other) noexcept {
  std::swap(mDriverInstance, other.mDriverInstance);
  return *this;
}

void Dotto::Graphics::VulkanRenderer::initialize() {
  createInstance();
}

void Dotto::Graphics::VulkanRenderer::terminate() {
#if _DEBUG
  DestroyDebugUtilsMessengerEXT(mDriverInstance, mDebugMessenger, nullptr);
#endif /* _DEBUG */
  vkDestroyInstance(mDriverInstance, nullptr);
}

void Dotto::Graphics::VulkanRenderer::createInstance() {
#if _DEBUG
  if (!hasValidationLayerSupport()) {
    std::cerr << "Validation layer support requested, but not available.\n";
    std::exit(EXIT_FAILURE);
  }
#endif /* _DEBUG */

  const auto& appName    = Application::name;
  const auto& appVersion = Application::version;
        auto  version    = VK_MAKE_VERSION(appVersion.major, appVersion.minor, appVersion.patch);

  auto extensionCount     = 0u;
  auto requiredExtensions = std::array<const char*, 3>({
    "VK_KHR_surface",

  #ifdef _WIN32
    "VK_KHR_win32_surface"
  #endif /* _WIN32 */
  });

#if _DEBUG
  extensionCount        = 3;
  requiredExtensions[2] = "VK_EXT_debug_utils";
#else
  extensionCount = 2;
#endif /* _DEBUG */

  VkApplicationInfo
    applicationInfo                    = {};
    applicationInfo.sType              = VK_STRUCTURE_TYPE_APPLICATION_INFO;
    applicationInfo.pApplicationName   = appName.data();
    applicationInfo.applicationVersion = version;
    applicationInfo.pEngineName        = appName.data();
    applicationInfo.engineVersion      = version;
    applicationInfo.apiVersion         = VK_API_VERSION_1_0;

#if _DEBUG
  VkDebugUtilsMessengerCreateInfoEXT
    debugUtilsMessengerCreateInfo                 = {};
    debugUtilsMessengerCreateInfo.sType           = VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT;
    debugUtilsMessengerCreateInfo.messageSeverity = VK_DEBUG_UTILS_MESSAGE_SEVERITY_VERBOSE_BIT_EXT |
                                                    VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT |
                                                    VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT;
    debugUtilsMessengerCreateInfo.messageType     = VK_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT    |
                                                    VK_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT |
                                                    VK_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT;
    debugUtilsMessengerCreateInfo.pfnUserCallback = debugCallback;
#endif /* _DEBUG */

  VkInstanceCreateInfo
    instanceCreateInfo                         = {};
    instanceCreateInfo.sType                   = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
    instanceCreateInfo.pApplicationInfo        = &applicationInfo;
    instanceCreateInfo.enabledExtensionCount   = extensionCount;
    instanceCreateInfo.ppEnabledExtensionNames = requiredExtensions.data();

#if _DEBUG
    instanceCreateInfo.enabledLayerCount   = 1;
    instanceCreateInfo.ppEnabledLayerNames = validationLayers.data();
    instanceCreateInfo.pNext               = &debugUtilsMessengerCreateInfo;
#else
    instanceCreateInfo.enableLayerCount = 0;
    instanceCreateInfo.pNext            = nullptr;
#endif /* _DEBUG */

  VkResult result = vkCreateInstance(&instanceCreateInfo, nullptr, &mDriverInstance);
  if (result != VK_SUCCESS) {
    if (bool fatal = reportError(result)) std::exit(fatal);
  }

#if _DEBUG
  if (!DestroyDebugUtilsMessengerEXT) {
    DestroyDebugUtilsMessengerEXT = reinterpret_cast<PFN_vkDestroyDebugUtilsMessengerEXT>(
      reinterpret_cast<void(*)()>(vkGetInstanceProcAddr(mDriverInstance, "vkDestroyDebugUtilsMessengerEXT"))
    );

    if (!DestroyDebugUtilsMessengerEXT)
      throw std::runtime_error("Failed to get function pointer: \"vkDestroyDebugUtilsMessengerEXT\".");
  }

  if (!CreateDebugUtilsMessengerEXT) {
    CreateDebugUtilsMessengerEXT = reinterpret_cast<PFN_vkCreateDebugUtilsMessengerEXT>(
      reinterpret_cast<void(*)()>(vkGetInstanceProcAddr(mDriverInstance, "vkCreateDebugUtilsMessengerEXT"))
    );

    if (!CreateDebugUtilsMessengerEXT)
      throw std::runtime_error("Failed to get function pointer: \"vkCreateDebugUtilsMessengerEXT\".");
  }

  result = CreateDebugUtilsMessengerEXT(mDriverInstance, &debugUtilsMessengerCreateInfo, nullptr, &mDebugMessenger);
  if (result != VK_SUCCESS)
    if (bool fatal = reportError(result)) std::exit(fatal);
#endif /* _DEBUG */
}
