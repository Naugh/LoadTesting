# Use Linux Alpine
FROM alpine

# JMeter version
ARG JMETER_VERSION=5.4.3

# Install few utilities
RUN apk add --update --no-cache openjdk13-jre

# Install JMeter
RUN   mkdir /jmeter \
      && cd /jmeter/ \
      && wget https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-$JMETER_VERSION.tgz \
      && tar -xzf apache-jmeter-$JMETER_VERSION.tgz \
      && rm apache-jmeter-$JMETER_VERSION.tgz \
      && cd /jmeter/apache-jmeter-$JMETER_VERSION/lib/ext \
      && wget https://github.com/mderevyankoaqa/jmeter-influxdb2-listener-plugin/releases/download/v1.5/jmeter-plugin-influxdb2-listener-1.5-all.jar

# Set JMeter Home
ENV JMETER_HOME /jmeter/apache-jmeter-$JMETER_VERSION/

# Add JMeter to the Path
ENV PATH $JMETER_HOME/bin:$PATH
