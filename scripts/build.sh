CYAN='\033[0;36m'
RED='\033[0;31m'
NC='\033[0m'

rm -rf bin/dotto.exe

clear
clear

g++ -x c++-header -o include/dotto/pch.h.gch -c src/pch.cpp -I include/

echo -e "${RED}NOT Building 32 bit...${NC}"
# g++ -DGLEW_STATIC src/* -I include/ -Llibs/x86/ -o bin/dotto -Wall -Wextra -pedantic -std=c++17 -m32 -lglfw3 -lglew32 -lws2_32 -lgdi32 -lopengl32

echo -e "${CYAN}Building 64 bit...${NC}"
g++ src/* -I include/ -Llibs/ -o bin/dotto -Wall -Wextra -pedantic -std=c++17 -m64 -DGLEW_STATIC -lglfw3 -lglew32 -lws2_32 -lgdi32 -lopengl32
