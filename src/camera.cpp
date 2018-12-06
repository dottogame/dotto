#include <dotto/camera.hpp>

/* Static declarations. */
dotto::clip_plane dotto::camera::clips = {
    10.0f, 10.0f, 10.0f, 10.0f, 0.1f, 10.0f
};

float dotto::camera::aspect = 16.0f / 9.0f;
float dotto::camera::fov = 70.f;
glm::fvec3 dotto::camera::position(4.0f, 3.0f, 3.0f);
const glm::fvec3 dotto::camera::rotation(1.0f, 1.0f, 1.0f);
const glm::fvec3 dotto::camera::scale(1.0f, 1.0f, 1.0f);