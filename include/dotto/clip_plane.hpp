#pragma once
#include "pch.h"

namespace dotto {
    /* This designates the clipping plane for the camera. */
    struct clip_plane final {
        /* The left plane of the clip. */
        float _left;

        /* The right plane of the clip. */
        float _right;

        /* The top plane of the clip. */
        float _top;

        /* The bottom plane of the clip. */
        float _bottom;

        /* The near plane of the clip. */
        float _near;

        /* The far plane of the clip. */
        float _far;

        /* Constructs a default clipping plane. */
        clip_plane() = default;

        /* Full constructor. */
        clip_plane(
            const float& __left,
            const float& __right,
            const float& __top,
            const float& __bottom,
            const float& __near,
            const float& __far
        ) :
            _left(__left),
            _right(__right),
            _top(__top),
            _bottom(__bottom),
            _near(__near),
            _far(__far)
        {
        }

        /* Copy constructor. */
        clip_plane(const clip_plane& other) :
            _left(other._left),
            _right(other._right),
            _top(other._top),
            _bottom(other._bottom),
            _near(other._near),
            _far(other._far)
        {
        }

        /* Move constructor. */
        clip_plane(clip_plane&& other) {
            this->swap(other);
        }

        /* Deconstructs this clipping plane. */
        ~clip_plane() = default;

        /* Copy-swap idiom assignment operator. */
        clip_plane& operator=(clip_plane other) {
            this->swap(other);
            return *this;
        }

        /* Swaps this clip_plane with the other. */
        void swap(clip_plane& other) {
            // ADL
            using std::swap;

            // swap
            swap(_left, other._left);
            swap(_right, other._right);
            swap(_top, other._top);
            swap(_bottom, other._bottom);
            swap(_near, other._near);
            swap(_far, other._far);
        }
    };
}
