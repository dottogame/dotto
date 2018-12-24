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
        bool exists(std::string path)
        {
            FILE * pFile;
            pFile = fopen(path.c_str(), "r");
            bool exist = (pFile != NULL);
            fclose(pFile);
            return exist;
        }

        std::string make_relative(std::string path)
        {
            return get_working_directory() + path;
        }

        bool to_string(std::string& target, std::string path)
        {
            std::cout << "Reading file: " << path << std::endl;

            std::FILE *fp = std::fopen(path.c_str(), "rb");
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

    std::string strip_extension(std::string path)
    {
        size_t index = path.find_last_of(".");
        if (index != std::string::npos) return path.substr(0, index);
        else return path;
    }

    void fetch_contents(std::vector<std::string>& list, std::string path)
    {
        DIR *dir;
        struct dirent *ent;
        if ((dir = opendir(file::make_relative(path).c_str())) != NULL)
        {
            while ((ent = readdir (dir)) != NULL)
                if (strcmp(ent->d_name, ".") && strcmp(ent->d_name, ".."))
                    list.push_back(strip_extension(ent->d_name));

            closedir(dir);
        }
        else
            std::cout << "[IO ERROR] Failed to open directory: " << path << std::endl;
    }

    void init()
    {
        mkdir(file::make_relative("/logs/").c_str(), 0755);
        mkdir(file::make_relative("/skins/").c_str(), 0755);
        mkdir(file::make_relative("/data/").c_str(), 0755);
        mkdir(file::make_relative("/data/wallpapers/").c_str(), 0755);
    }
}
