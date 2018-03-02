FROM naughtyth/jmeter-base


CMD jmeter -n -t /files/test.jmx -o /files -R$IPSLAVES

# Ports to be exposed from the container for JMeter Master
EXPOSE 60000
