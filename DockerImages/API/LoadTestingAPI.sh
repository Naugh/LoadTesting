#!/bin/bash

sudo docker run --name=loadtestingAPI --net="host" -p 8080:8080 naughtyth/loadtesting

read
