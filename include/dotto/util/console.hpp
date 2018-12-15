#pragma once

#include "../pch.h"

#include "../io.hpp"

namespace dotto::console
{
     // log levels
    constexpr int LL_INFO = 0;
    constexpr int LL_WARNING = 1;
    constexpr int LL_ERROR = 2;
    // NOTE: ANY NUMBER ABOVE 2 MEANS NO LOGGING

    int log_level = LL_INFO;

    // data for writing to file
    bool file = false;

    std::ofstream outfile;

    // returns current timestamp as string
    std::string timestamp_str()
    {
        using namespace std::chrono;
        milliseconds ms = duration_cast<milliseconds>(
            system_clock::now().time_since_epoch()
        );

        return std::to_string(ms.count());
    }

    // enables file logging
    void enable_file()
    {
        file = true;
        std::string path = std::string("/logs/") + timestamp_str() + ".txt";
        outfile.open(
            io::file::make_relative(path.c_str()).c_str(),
            std::ios_base::app
        );
    }

    // configures log level
    void set_log_level(int level)
    {
        log_level = level;
    }

    void print(std::string label, std::string msg)
    {
        // compile message to go out
        std::stringstream out_ss;
        out_ss << "[" << timestamp_str() << "]" << label << " " << msg;
        std::string out = out_ss.str();

        // send it away!
        if (file) outfile << out << std::endl;
        std::cout << out << std::endl << std::flush;
    }

    void log(std::string msg)
    {
        if (log_level > LL_INFO) return;

        print("[INFO]", msg);
    }

    void warn(std::string msg)
    {
        if (log_level > LL_WARNING) return;

        print("[WARN]", msg);
    }

    void err(std::string msg)
    {
        if (log_level > LL_ERROR) return;

        print("[ERROR]", msg);
    }

    void clean()
    {
        file = false;
        outfile.close();
    }
}
