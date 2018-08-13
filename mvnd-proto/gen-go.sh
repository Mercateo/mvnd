#!/bin/bash

PATH=$PATH:$GOPATH/bin/ 
protoc -I=src/main/proto/ --go_out=plugins=grpc:target/go src/main/proto/mvnd.proto
