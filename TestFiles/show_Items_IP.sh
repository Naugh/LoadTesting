sudo docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' itemsAPI
read
