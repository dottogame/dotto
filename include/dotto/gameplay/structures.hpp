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
        int type;
        int tween;
        
        time_t timestamp;
        time_t screen_time; // approach rate

        float speed;

        glm::fvec2 location;
        glm::fvec2 target;
    };

    struct meta_data
    {
        int version;
        int start_offset;

        float difficulty;

        std::string song;
        std::string edition;

        std::vector<std::string> producers;
        std::vector<std::string> mappers;

        std::vector<element*> elements;
    };
}
