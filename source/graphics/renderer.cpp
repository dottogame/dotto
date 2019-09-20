#include <array>
#include <cassert>
#include <cstring>
#include <iostream>
#include <optional>
#include <set>
#include <dotto/application.hpp>

struct QueueFamilyIndices {
  std::optional<uint32_t> graphicsFamily;
  std::optional<uint32_t> computeFamily;
  std::optional<uint32_t> transferFamily;
  std::optional<uint32_t> sparseBindingFamily;
  std::optional<uint32_t> protectedFamily;

  // Modify this in the instance we need one or more of the other family types.
  bool isComplete() { return graphicsFamily.has_value(); }
};

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

// TODO: alter to account for other types of families.
static QueueFamilyIndices findQueueFamilies(VkPhysicalDevice device, VkSurfaceKHR surfaceToSupport) {
  auto indices     = QueueFamilyIndices{};
  auto familyCount = 0u;
  vkGetPhysicalDeviceQueueFamilyProperties(device, &familyCount, nullptr);

  auto  size          = sizeof(VkQueueFamilyProperties) * familyCount;
  auto* queueFamilies = reinterpret_cast<VkQueueFamilyProperties*>(alloca(size));
  vkGetPhysicalDeviceQueueFamilyProperties(device, &familyCount, queueFamilies);

  VkBool32 presentSupport = VK_FALSE;
  for (size_t index = 0; index < familyCount; index++) {
    const auto& family          = queueFamilies[index];
    const bool  hasManyQueues   = (0 < family.queueCount);
    const bool  isGraphicsQueue = (family.queueFlags & VK_QUEUE_GRAPHICS_BIT);
    vkGetPhysicalDeviceSurfaceSupportKHR(device, index, surfaceToSupport, &presentSupport);

    if (hasManyQueues && isGraphicsQueue) {
      indices.graphicsFamily = index;
      break;
    }
  }

  return indices;
}

static bool isDeviceSuitable(VkPhysicalDevice device, VkSurfaceKHR surfaceToSupport) {
  return findQueueFamilies(device, surfaceToSupport).isComplete();
}

Dotto::Graphics::VulkanRenderer::~VulkanRenderer() {
  terminate(); }
Dotto::Graphics::VulkanRenderer::VulkanRenderer() {
  initialize(); }
Dotto::Graphics::VulkanRenderer::VulkanRenderer(Dotto::Graphics::VulkanRenderer&& other) noexcept:
  mDriverInstance(std::move(other.mDriverInstance)) {}

Dotto::Graphics::VulkanRenderer& Dotto::Graphics::VulkanRenderer::operator=(Dotto::Graphics::VulkanRenderer&& other) noexcept {
  std::swap(mDriverInstance, other.mDriverInstance);
  return *this;
}

Dotto::Graphics::VulkanRenderer::VulkanRenderer(GLFWwindow* window) {
  mWindow = window;
  initialize();
}

void Dotto::Graphics::VulkanRenderer::initialize() {
  createInstance();
  getWindowSurface();
  pickPhysicalDevice();
  createLogicalDevice();
}

void Dotto::Graphics::VulkanRenderer::terminate() {
  vkDestroyDevice(mLogicalDevice, nullptr);

#if _DEBUG
  DestroyDebugUtilsMessengerEXT(mDriverInstance, mDebugMessenger, nullptr);
#endif /* _DEBUG */
  vkDestroySurfaceKHR(mDriverInstance, mWindowSurface, nullptr);
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
    instanceCreateInfo.enabledLayerCount = 0;
    instanceCreateInfo.pNext             = nullptr;
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

void Dotto::Graphics::VulkanRenderer::getWindowSurface() {
  VkResult result = glfwCreateWindowSurface(mDriverInstance, mWindow, nullptr, &mWindowSurface);
  if (result != VK_SUCCESS)
    if (bool fatal = reportError(result)) std::exit(fatal);
}

void Dotto::Graphics::VulkanRenderer::pickPhysicalDevice() {
  auto deviceCount = 0u;
  vkEnumeratePhysicalDevices(mDriverInstance, &deviceCount, nullptr);
  assert(deviceCount != 0);

  auto  size    = sizeof(VkPhysicalDevice) * deviceCount;
  auto* devices = reinterpret_cast<VkPhysicalDevice*>(alloca(size));
  vkEnumeratePhysicalDevices(mDriverInstance, &deviceCount, devices);
  for (size_t index = 0; index < deviceCount; index++) {
    const auto device = devices[index];
    if (isDeviceSuitable(device, mWindowSurface)) {
      mPhysicalDevice = device;
      return;
    }
  }

  std::cerr << "Failed to find a suitable GPU.\n";
  std::exit(EXIT_FAILURE);
}

// TODO: handle other queue families.
void Dotto::Graphics::VulkanRenderer::createLogicalDevice() {
  auto indices                = findQueueFamilies(mPhysicalDevice, mWindowSurface);
  auto queuePriority          = 1.0f;
  auto graphicsQueueIndex     = indices.graphicsFamily.value();
  auto deviceQueueCreateInfos = std::array<VkDeviceQueueCreateInfo, 1>();

  VkDeviceQueueCreateInfo
    deviceQueueCreateInfo                  = {};
    deviceQueueCreateInfo.sType            = VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO;
    deviceQueueCreateInfo.queueFamilyIndex = graphicsQueueIndex;
    deviceQueueCreateInfo.queueCount       = 1;
    deviceQueueCreateInfo.pQueuePriorities = &queuePriority;

  deviceQueueCreateInfos[0] = deviceQueueCreateInfo;

  VkPhysicalDeviceFeatures
    physicalDeviceFeatures = {};

  VkDeviceCreateInfo
    deviceCreateInfo                         = {};
    deviceCreateInfo.sType                   = VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO;
    deviceCreateInfo.queueCreateInfoCount    = 1;
    deviceCreateInfo.pQueueCreateInfos       = deviceQueueCreateInfos.data();
    deviceCreateInfo.pEnabledFeatures        = &physicalDeviceFeatures;
    deviceCreateInfo.enabledExtensionCount   = 1;
    deviceCreateInfo.ppEnabledExtensionNames = deviceExtensions.data();

#if _DEBUG
    deviceCreateInfo.enabledLayerCount   = 1;
    deviceCreateInfo.ppEnabledLayerNames = validationLayers.data();
#else
    deviceCreateInfo.enabledLayerCount = 0;
#endif /* _DEBUG */

  VkResult result = vkCreateDevice(mPhysicalDevice, &deviceCreateInfo, nullptr, &mLogicalDevice);
  if (result != VK_SUCCESS)
    if (bool fatal = reportError(result)) std::exit(fatal);

  vkGetDeviceQueue(mLogicalDevice, graphicsQueueIndex, 0, &mGraphicsQueue);
  vkGetDeviceQueue(mLogicalDevice, graphicsQueueIndex, 0, &mPresentQueue);
}
