#pragma once

#include "pch.h"

#ifdef _WIN32
#define mkdir(dir, mode) _mkdir(dir)
#endif

namespace dotto::io
{
    #ifdef _WIN32
    std::string get_working_directory()
    {
        HMODULE hModule = GetModuleHandleA(NULL);
        char path[MAX_PATH];
        GetModuleFileNameA(hModule, path, MAX_PATH);
        std::string str(path);

        return str.substr(0, str.find_last_of("\\/")) + "\\";
    }
    #endif

    #ifdef linux
    std::string get_working_directory()
    {
        ssize_t count = readlink("/proc/self/exe", result, PATH_MAX);
        const char *path;

        if (count != -1) path = dirname(result);
        else std::cout << "Error calling readlink!" << std::endl;

        std::string str(path);

        return str.substr(0, str.length() - 11);
    }
    #endif

    namespace file
    {
        std::string make_relative(const char* path)
        {
            return get_working_directory() + path;
        }

        bool to_string(std::string& target, const char* path)
        {
            std::string full_path(get_working_directory() + path);

            std::cout << "Reading file: " << full_path << std::endl;

            std::FILE *fp = std::fopen(full_path.c_str(), "rb");
            if (fp)
            {
                std::fseek(fp, 0, SEEK_END);
                target.resize(std::ftell(fp));
                std::rewind(fp);
                std::fread(&target[0], 1, target.size(), fp);
                std::fclose(fp);
                return true;
            }

            return false;
        }

        /**
            Clean, abstracted interface for reading file line by line.
            Yeah, it basically abstracts nothing. Whatever. Puts it all in
            one object at least.
        */
        struct line_reader
        {
            std::string line;

            std::ifstream* in;

            line_reader(const char *path)
            {
                std::string full_path(get_working_directory() + path);
                std::cout << "Reading file line-by-line: " << full_path << std::endl;
                in = new std::ifstream(full_path);
                if (!in)
                {
                    std::cerr << "Cannot open input file: " << path << std::endl;
                    return;
                }
            }

            bool next()
            {
                if (std::getline(*in, line)) return true;
                return false;
            }

            ~line_reader()
            {
                delete in;
            }
        };
    }

    void init()
    {
        mkdir(file::make_relative("/logs/").c_str(), 0755);
        mkdir(file::make_relative("/skins/").c_str(), 0755);
        mkdir(file::make_relative("/data/").c_str(), 0755);
    }
}
