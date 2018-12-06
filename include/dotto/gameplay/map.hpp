#pragma once

#include "../pch.h"
#include "../io.hpp"
#include "structures.hpp"
#include "parser1.hpp"

namespace dotto::gameplay
{
    class map
    {
    public:
        meta_data* meta_data;

        element* (*parse_data)(std::string data_line);
        meta_data* (*parse_meta)(io::file::line_reader* reader);

    private:
        io::file::line_reader* reader;

        int m_parsers[1] = {
            &meta_parser1
        };

        int d_parsers[1] = {
            &data_parser1
        };
    public:

        map(const char *path)
        {
            producers = new std::vector<std::string>();
            mappers = new std::vector<std::string>();

            reader = new io::file::line_reader(path);
            // read map version
            if (reader->next()) version = std::stoi(reader->line);
            else std::cout << "Failed to read version from " << path << std::endl;

            // configure parser
            if(version > m_parsers.length || version == 0)
            {
                cout << "Invalid map version " << version << std::endl;
            }
            else
            {
                parse_meta = m_parsers[version - 1];
                parse_data = d_parsers[version - 1];
            }

            // parse meta
            parse_meta();
        }
    };
}
