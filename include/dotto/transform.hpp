#pragma once
#include "pch.h"

namespace dotto {
    /* Represents the m_position, rotation, and scale of something. */
    class transform final {
        /* The m_position of this model. */
        glm::fvec3 m_position;

        /* The rotation of this model. */
        glm::fquat m_rotation;

        /* The scale of this model. */
        glm::fvec3 m_scale;

        /* The representation of this tranform. */
        glm::fmat4 m_rep;

        /* Tells if this transform has changed. */
        bool m_changed;

    public:
        /* Creates a default transform. */
        transform() = default;

        /* Copy constructor. */
        transform(const transform& other) :
            m_position(other.m_position),
            m_rotation(other.m_rotation),
            m_scale(other.m_scale),
            m_changed(other.m_changed)
        {
        }

        /* Move constructor. */
        transform(transform&& other) {
            std::swap(m_position, other.m_position);
            std::swap(m_rotation, other.m_rotation);
            std::swap(m_scale, other.m_scale);
            std::swap(m_changed, other.m_changed);
        }

        /* Deconstructs this transform. */
        ~transform() = default;

        /* Returns the representation of this transform. */
        inline operator glm::fmat4() {
            return as_matrix();
        }

        /* Gets this transform as the underlying matrix. */
        inline glm::fmat4 as_matrix() {
            if (m_changed) {
                glm::fmat4 pmat = glm::translate(glm::fmat4(1.0f), m_position);
                glm::fmat4 rmat = glm::mat4_cast(m_rotation);
                glm::fmat4 smat = glm::scale(glm::fmat4(1.0f), m_scale);
                m_rep = pmat * rmat * smat;
                m_changed = false;
            }

            return m_rep;
        }

        /* Gets this transform's position. */
        inline glm::fvec3 position() {
            return m_position;
        }

        /* Gets this transform's rotation. */
        inline glm::fquat rotation() {
            return m_rotation;
        }

        /* Gets this transform's scale. */
        inline glm::fvec3 scale() {
            return m_scale;
        }

        /* Sets the position of this transform. */
        inline void set_position(
            const float& x_pos,
            const float& y_pos,
            const float& z_pos
        ) {
            m_position.x = x_pos;
            m_position.y = y_pos;
            m_position.z = z_pos;
            m_changed = true;
        }

        /* Sets the position of this transform. */
        inline void set_position(
            float&& x_pos,
            float&& y_pos,
            float&& z_pos
        ) {
            m_position.x = x_pos;
            m_position.y = y_pos;
            m_position.z = z_pos;
            m_changed = true;
        }

        /* Sets the position of this transform. */
        inline void set_position(const glm::fvec3& _position) {
            m_position = _position;
            m_changed = true;
        }

        /* Sets the position of this transfrom. */
        inline void set_position(glm::fvec3&& _position) {
            m_position = _position;
            m_changed = true;
        }

        /* Sets the rotation of this transform. */
        inline void set_rotation(
            const float& x_rot,
            const float& y_rot,
            const float& z_rot
        ) {
            m_rotation = glm::fquat(glm::fvec3(x_rot, y_rot, z_rot));
            m_changed = true;
        }

        /* Sets the rotation of this transform. */
        inline void set_rotation(
            float&& x_rot,
            float&& y_rot,
            float&& z_rot
        ) {
            m_rotation = glm::fquat(glm::fvec3(x_rot, y_rot, z_rot));
            m_changed = true;
        }

        /* Sets the rotation of this transform. */
        inline void set_rotation(const glm::fvec3& eulers) {
            m_rotation = glm::fquat(eulers);
            m_changed = true;
        }

        /* Sets the rotation of this transform. */
        inline void set_rotation(glm::fvec3&& eulers) {
            m_rotation = glm::fquat(eulers);
            m_changed = true;
        }

        /* Sets the rotation of this transform. */
        inline void set_rotation(const glm::fquat& _rotation) {
            m_rotation = _rotation;
            m_changed = true;
        }

        /* Sets the rotation of this transform. */
        inline void set_rotation(glm::fquat&& _rotation) {
            m_rotation = _rotation;
            m_changed = true;
        }

        /* Sets the m_position of this transform. */
        inline void set_scale(
            const float& x_pos,
            const float& y_pos,
            const float& z_pos
        ) {
            m_scale.x = x_pos;
            m_scale.y = y_pos;
            m_scale.z = z_pos;
            m_changed = true;
        }

        /* Sets the m_position of this transform. */
        inline void set_scale(
            float&& x_pos,
            float&& y_pos,
            float&& z_pos
        ) {
            m_scale.x = x_pos;
            m_scale.y = y_pos;
            m_scale.z = z_pos;
            m_changed = true;
        }

        /* Sets the m_position of this transform. */
        inline void set_scale(const glm::fvec3& _scale) {
            m_scale = _scale;
            m_changed = true;
        }

        /* Sets the m_position of this transfrom. */
        inline void set_scale(glm::fvec3&& _scale) {
            m_scale = _scale;
            m_changed = true;
        }

        /* Swaps this tranform with other. */
        inline void swap(transform& other) {
            std::swap(m_position, other.m_position);
            std::swap(m_rotation, other.m_rotation);
            std::swap(m_scale, other.m_scale);
            std::swap(m_changed, other.m_changed);
        }
    };
}