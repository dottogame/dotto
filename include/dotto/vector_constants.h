#pragma once
#include "pch.h"

namespace dotto {
    /* World space constant for the backward direction. */
    const glm::fvec3 ws_backward(0.0f, 0.0f, 1.0f);

    /* World space constant for the downward direction. */
    const glm::fvec3 ws_downward(0.0f, -1.0f, 0.0f);

    /* World space constant for the forward direction. */
    const glm::fvec3 ws_forward(0.0f, 0.0f, -1.0f);

    /* World space constant for the leftward direction. */
    const glm::fvec3 ws_leftward(-1.0f, 0.0f, 0.0f);

    /* World space constant for the origin point. */
    const glm::fvec3 ws_origin(0.0f, 0.0f, 0.0f);

    /* World space constant for the rightward direction. */
    const glm::fvec3 ws_rightward(1.0f, 0.0f, 0.0f);

    /* World space constant for the upward direction. */
    const glm::fvec3 ws_upward(0.0f, 1.0f, 0.0f);
}
