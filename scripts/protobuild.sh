protoc --cpp_out=./ src/dotto.proto

echo "Please move the header from src/ to include/dotto/"
echo "Also, change the header include in dotto.pb.cc to point new location"
