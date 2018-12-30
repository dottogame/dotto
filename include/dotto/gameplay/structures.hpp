#pragma once

#include <glm/glm.hpp>
#include "../pch.h"

#define ELEMENT_TYPE_DT_NOTE 0
#define ELEMENT_TYPE_SLID_NOTE 1
#define ELEMENT_TYPE_POS 2

#define ELEMENT_TWEEN_LINEAR 0
#define ELEMENT_TWEEN_SERPENT 1
#define ELEMENT_TWEEN_LOGARITHMIC 2
#define ELEMENT_TWEEN_EXPONENTIAL 3

namespace dotto::gameplay
{
    struct element
    {
        unsigned type : 2;
        unsigned ring : 3;
        unsigned tween : 3;
        unsigned rotation : 8;
        unsigned screen_time : 8;
        unsigned speed : 8;
        unsigned timestamp : 32;
    };

    // should be 2 bytes (a short) (we only use 12)
    // to get degrees of rotation do rotation * 1.40625
    struct position {

    };

    struct meta_data
    {
        int version;
        int start_offset;

        float difficulty;

        std::string song;
        std::string edition;
        std::string source;

        std::vector<std::string> producers;
        std::vector<std::string> mappers;

        std::vector<element*> elements;
    };
}
