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
            this->swap(other);
        }

        /* Deconstructs this model. */
        virtual ~model() {
        }

        /* Copy-swap idiom assignment operator. */
        model& operator=(model other) {
            this->swap(other);
            return *this;
        }

        /* Swaps one model with the other. */
        void swap(model& other) {
            // ADL
            using std::swap;

            // swap
            swap(mesh, other.mesh);
            swap(transform, other.transform);
        }
    };
}