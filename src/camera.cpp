#include <dotto/camera.hpp>

/* Static declarations. */
dotto::clip_plane dotto::camera::clips = {
    10.0f, 10.0f, 10.0f, 10.0f, 0.1f, 100.0f
};

float dotto::camera::aspect = 16.0f / 9.0f;
float dotto::camera::fov = 70.f;
dotto::transform dotto::camera::transform;