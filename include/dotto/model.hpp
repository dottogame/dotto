#pragma once
#include "mesh.hpp"

namespace dotto {
    /* Designates a model that can be transformed and effectively rendered. */
    struct model {
        /* The mesh of this model. */
        mesh mesh;

        /* The position of this model. */
        glm::fvec3 position;

        /* The rotation of this model. */
        glm::fvec3 rotation;

        /* The scale of this model. */
        glm::fvec3 scale;

        /* Constructs a model. */
        model() :
            mesh(),
            position(),
            rotation(),
            scale()
        {
        }

        /* Copy constructor. */
        model(const model& other) :
            mesh(other.mesh),
            position(other.position),
            rotation(other.rotation),
            scale(other.scale)
        {
        }

        /* Move constructor. */
        model(model&& other) :
            mesh(),
            position(),
            rotation(),
            scale()
        {
            mesh.swap(other.mesh);
            std::swap(position, other.position);
            std::swap(rotation, other.rotation);
            std::swap(scale, other.scale);
        }

        /* Deconstructs this model. */
        virtual ~model() {
        }

        /* Returns this model's calculated transform. */
        glm::fmat4 transform() {
            glm::fmat4 tform(1.0f);
            glm::fmat4 smat = glm::scale(glm::fmat4(1.0f), scale);
            glm::fmat4 rmat = glm::rotate(glm::fmat4(1.0f), glm::radians(0.0f), rotation);
            glm::fmat4 pmat = glm::translate(glm::fmat4(1.0f), position);
            tform = pmat * rmat * smat;
            return tform;
        }
    };
}