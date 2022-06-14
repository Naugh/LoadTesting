# Use Grafana Open Source
FROM grafana/grafana-oss:8.2.3

#Provising Dashboards and Database
RUN    cd /etc/grafana/provisioning/datasources \
    && wget https://raw.githubusercontent.com/Naugh/LoadTesting/master/DockerImages/grafana/Loadtesting_Influxdb2.yaml \
    && cd /etc/grafana/provisioning/dashboards \
    && wget https://raw.githubusercontent.com/Naugh/LoadTesting/master/DockerImages/grafana/Loadtesting_dashboard.json \
    && wget https://raw.githubusercontent.com/Naugh/LoadTesting/master/DockerImages/grafana/Loadtesting_dashboard.yaml
 
#Set Influxdb2 IP
ENV INFLUXDB2_URL http://localhost:8086
