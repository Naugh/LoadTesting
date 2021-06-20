#!/bin/bash

#sudo docker rm -f loadtestingAPI
sudo docker run --rm --name=loadtestingAPI --net="host" naughtyth/loadtesting

read
