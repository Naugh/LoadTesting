#!/bin/bash

sudo docker rm -f loadtestingAPI
sudo docker run --name=loadtestingAPI --net="host" -p 8080:8080 naughtyth/loadtesting

read
