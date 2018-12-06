#pragma once
#include "pch.h"
#include "vector_constants.h"
#include "clip_plane.hpp"

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

        /* The position of this camera. */
        static glm::fvec3 position;

        /* The rotation of this camera. */
        const static glm::fvec3 rotation;

        /* The scale of this camera. */
        const static glm::fvec3 scale;

        /* Returns where this camera is looking. */
        static inline glm::fmat4 looking_at() {
            return glm::lookAt(position, ws_origin, ws_upward);
        }

        /* Builds the projection matrix and returns it. */
        static inline glm::fmat4 projection() {
            return glm::perspective(fov, aspect, clips._near, clips._far);
        }

        /* Builds and returns this object's transform matrix. */
        static inline glm::fmat4 transform() {
            glm::fmat4 tform(1.0f);
            tform *= glm::fvec4(scale, 1.0f);
            tform *= glm::fvec4(rotation, 1.0f);
            tform *= glm::fvec4(position, 1.0f);
            return tform;
        }
    };
}
