#pragma once

#include "../pch.h"

#define DR_MP3_IMPLEMENTATION
#include <drsoft/dr_mp3.h>
#define DR_WAV_IMPLEMENTATION
#include <drsoft/dr_wav.h>
#define DR_FLAC_IMPLEMENTATION
#include <drsoft/dr_flac.h>
#define MINI_AL_IMPLEMENTATION
#include <drsoft/mini_al.h>

#include "../io.hpp"
#include "../util/console.hpp"

namespace dotto::audio
{
    struct source
    {
    public:
        mal_decoder decoder;

        source(const char* path)
        {
            mal_result result = mal_decoder_init_file(
                io::file::make_relative(path).c_str(),
                NULL,
                &decoder
            );

            if (result != MAL_SUCCESS)
            {
                console::err("failed to load audio at path:");
                console::err(std::string(path));
                return;
            }
        }

        ~source()
        {
            mal_decoder_uninit(&decoder);
        }
    };

    std::vector<source*> sources;

    void play(source* source)
    {
        sources.push_back(source);
    }

    mal_uint32 on_send_frames_to_device(mal_device* pDevice, mal_uint32 frameCount, void* pSamples)
    {
        if (sources.size() == 0) return 0;

        float* buff = new float[sizeof(float) * frameCount];

        // read first
        mal_uint32 count = (mal_uint32) mal_decoder_read(&(sources.at(0)->decoder), frameCount, buff);
        for (size_t i = 0; i < count * 2; i++)
            ((float*) pSamples)[i] = buff[i];

        // add any others
        for (size_t r = 1; r < sources.size(); r++)
        {
            (mal_uint32) mal_decoder_read(&(sources.at(r)->decoder), frameCount, buff);
            for (size_t i = 0; i < count * 2; i++)
                ((float*) pSamples)[i] += buff[i];
        }

        return count;
    }

    mal_device device;

    void clean()
    {
        mal_device_uninit(&device);
    }

    int init()
    {
        console::log("initializing audio engine");
        mal_decoder decoder;
        mal_result result = mal_decoder_init_file(
            dotto::io::file::make_relative("res\\audio\\soraw.mp3").c_str(),
            NULL,
            &decoder
        );

        if (result != MAL_SUCCESS) {
            console::err("failed to load configurating audio file:");
            console::err("res\\audio\\soraw.mp3");
            return -2;
        }

        mal_device_config config = mal_device_config_init_playback(
            decoder.outputFormat,
            decoder.outputChannels,
            decoder.outputSampleRate,
            on_send_frames_to_device
        );

        if (mal_device_init(NULL, mal_device_type_playback, NULL, &config, &decoder, &device) != MAL_SUCCESS) {
            console::err("Failed to open playback device.\n");
            mal_decoder_uninit(&decoder);
            return -3;
        }

        if (mal_device_start(&device) != MAL_SUCCESS) {
            console::err("Failed to start playback device.\n");
            mal_device_uninit(&device);
            mal_decoder_uninit(&decoder);
            return -4;
        }

        return 0;
    }
}
