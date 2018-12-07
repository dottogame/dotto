#pragma once
#include "mesh.hpp"
#include "transform.hpp"

namespace dotto {
    /* Designates a model that can be transformed and effectively rendered. */
    struct model {
        /* The mesh of this model. */
        mesh mesh;

        /* The transform of this model. */
        transform transform;

        /* Constructs a model. */
        model() = default;

        /* Copy constructor. */
        model(const model& other) :
            mesh(other.mesh),
            transform(other.transform)
        {
        }

        /* Move constructor. */
        model(model&& other) {
            mesh.swap(other.mesh);
            transform.swap(other.transform);
        }

        /* Deconstructs this model. */
        virtual ~model() {
        }
    };
}