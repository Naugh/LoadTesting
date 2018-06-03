#!/bin/bash

sudo docker rmi loadtestingAPI
sudo docker build . -t loadtesting

read
