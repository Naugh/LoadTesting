#!/bin/bash
sudo docker rm -f $(sudo docker ps -a -q)
sudo docker volume rm $(sudo docker volume ls -q)

read -p "Press any key"
