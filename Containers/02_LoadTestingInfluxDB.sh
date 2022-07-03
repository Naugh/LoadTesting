#!/bin/bash

sudo docker run --rm --name=loadtestingInfluxDB2 -p 8086:8086 -e DOCKER_INFLUXDB_INIT_MODE=setup -e DOCKER_INFLUXDB_INIT_USERNAME=user -e DOCKER_INFLUXDB_INIT_PASSWORD=password -e DOCKER_INFLUXDB_INIT_ORG=urjc -e DOCKER_INFLUXDB_INIT_BUCKET=jmeter -e DOCKER_INFLUXDB_INIT_ADMIN_TOKEN=jmetertoken influxdb:2.0.9
#sudo docker run --rm --name=loadtestingInfluxDB2 --net="host" -e DOCKER_INFLUXDB_INIT_MODE=setup -e DOCKER_INFLUXDB_INIT_USERNAME=user -e DOCKER_INFLUXDB_INIT_PASSWORD=password -e DOCKER_INFLUXDB_INIT_ORG=urjc -e DOCKER_INFLUXDB_INIT_BUCKET=jmeter -e DOCKER_INFLUXDB_INIT_ADMIN_TOKEN=jmetertoken influxdb:2.0.9

read
