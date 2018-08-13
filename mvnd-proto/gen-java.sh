#!/bin/bash

PATH=$PATH:$GOPATH/bin/ 

protoc -I=src/main/proto \
--java_out=src/main/java \
src/main/proto/mvnd.proto

protoc -I=src/main/proto \
--plugin=protoc-gen-grpc-java=bin/protoc-gen-grpc-java \
--grpc-java_out=src/main/java \
src/main/proto/mvnd.proto
