#!/bin/bash

sudo docker rmi loadtestingAPI
sudo docker build /home/naugh/GitHub/LoadTesting/DockerImages/API -t loadtestingAPI

read
