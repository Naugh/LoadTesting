sudo docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' loadtestingInfluxDB2
read
