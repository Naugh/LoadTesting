#!/bin/bash

sudo docker rmi loadtesting
sudo docker build /home/naugh/GitHub/LoadTesting/DockerImages/API -t loadtesting

read
