#include <dotto/camera.hpp>

/* Static declarations. */
dotto::clip_plane dotto::camera::clips = {
    10.0f, 10.0f, 10.0f, 10.0f, 0.1f, 100.0f
};

float dotto::camera::aspect = 16.0f / 9.0f;
float dotto::camera::fov = 70.f;
glm::fvec3 dotto::camera::position(0.0f, 0.0f, 10.0f);
const glm::fvec3 dotto::camera::rotation(1.0f, 1.0f, 1.0f);
const glm::fvec3 dotto::camera::scale(1.0f, 1.0f, 1.0f);