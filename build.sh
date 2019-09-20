debug=false
release=false
options=""
for option in $@
do
  case "$option" in
    "-d")
      debug=true
      if [ "$release" == "true" ]; then
        echo -e "\e[31mMust describe either debug or release building not both.\e[0m"
        exit 1
      fi

      if [ ! -d "debug" ]; then
        echo -e "\e[33mDebug directory missing, adding directory...\e[0m"
        mkdir debug
      fi

      options="$options -DCMAKE_BUILD_TYPE=Debug -DBENCHMARK_ENABLE_TESTING=OFF"
    ;;
    "-r")
      release=true
      if [ "$debug" == "true" ]; then
        echo -e "\e[31mMust describe either debug or release building not both.\e[0m"
        exit 1
      fi

      if [ ! -d "release" ]; then
        echo -e "\e[33mRelease directory missing, adding directory...\e[0m"
        mkdir release
      fi

      options="$options -DCMAKE_BUILD_TYPE=Release"
    ;;
    "-q")
      options="$options -DBUILD_QUIET=ON"
    ;;
  esac
done

if [[ "$debug" == "false" && "$release" == "false" ]]; then
  echo -e "\e[31mNo build type specified, defaulting to debug.\e[0m"
  options="$options -DCMAKE_BUILD_TYPE=Debug"
  debug=true

  if [ ! -d "debug" ]; then
    echo -e "\e[31mDebug directory missing, adding directory...\e[0m"
    mkdir debug
  fi
fi

if [ "$debug" == "true" ]; then
  cd debug
    cmake -G "Unix Makefiles" .. $options
    cmake --build . -- -j"$(($(nproc)/2+1))"
  cd ../
elif [ "$release" == "true" ]; then
  cd release
    cmake -G "Unix Makefiles" .. $options
    cmake --build . -- -j"$(($(nproc)/2+1))"
  cd ../
fi
