#pragma once
#include "pch.h"
#include "vector_constants.h"
#include "clip_plane.hpp"
#include "transform.hpp"

namespace dotto {
    /*
     */
    class camera final {
        /* Constructs a new camera instance. */
        camera() = default;

        /* Deconstructs a new camera instance. */
        ~camera() = default;

    public:
        /* The clipping plane for this camera. */
        static clip_plane clips;

        /* The aspect ratio of this camera. */
        static float aspect;

        /* The field of view of the camera. */
        static float fov;

        /* The transform of this camera. */
        static transform transform;

        /* Returns where this camera is looking. */
        static inline glm::fmat4 looking_at() {
            return glm::lookAt(transform.position(), ws_origin, ws_upward);
        }

        /* Builds the projection matrix and returns it. */
        static inline glm::fmat4 projection() {
            return glm::perspective(fov, aspect, clips._near, clips._far);
        }
    };
}
