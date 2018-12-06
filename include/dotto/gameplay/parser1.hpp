#pragma once

#include "../pch.h"
#include "structures.hpp"

namespace dotto::gameplay
{
    namespace parser1
    {
        void read_list(std::vector<std::string>& vec)
        {
            while ((pos = reader->line.find(",")) != std::string::npos) {
                vec.push(reader->line.substr(0, pos));
                reader->line.erase(0, pos + 1);
            }

            // push what remains
            vec.push(reader->line);
        }
    }

    element* data_parser1(std::string data_line)
    {
        element* elem = new element();
        elem->type = std::stoi(data_line[0]);
        elem->timestamp = std::stoi(data_line.substr(1, 11));
        // TODO parse location
        if (elem->type == ELEMENT_TYPE_POS)
        {
            // TODO parse target location
        }
        return elem;
    }

    meta_data* meta_parser1(io::file::line_reader* reader)
    {
        meta_data* m_data = new meta_data();

        size_t pos;

        std::cout << "Parsing with version 1 parser: " << path << std::endl;

        // read map song title
        if (reader->next()) m_data->song = reader->line;
        else std::cout << "Failed to read song title from " << path << std::endl;

        // read map edition title
        if (reader->next()) m_data->edition = reader->line;
        else std::cout << "Failed to read edition title from " << path << std::endl;

        // read map start offset
        if (reader->next()) m_data->start_offset = std::stoi(reader->line);
        else std::cout << "Failed to read start offset from " << path << std::endl;

        // read map difficulty
        if (reader->next()) m_data->difficulty = std::stof(reader->line);
        else std::cout << "Failed to read difficulty from " << path << std::endl;

        // read producers
        parser1::read_list(m_data->producers);

        // read mappers
        parser1::read_list(m_data->mappers);

        return m_data;
    }
}
