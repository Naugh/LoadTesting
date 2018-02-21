# Use Linux Alpine
FROM alpine

# Install java and docker
RUN apk add --update --no-cache openjdk8-jre-base docker

#Get API
#ADD LoadTestingAPI-0.0.1-SNAPSHOT.jar LoadTestingAPI.jar
ADD https://raw.githubusercontent.com/Naugh/LoadTesting/master/DockerImages/API/LoadTestingAPI.jar LoadTestingAPI.jar

#Set docker host
ENV DOCKER_HOST tcp://0.0.0.0:2375

EXPOSE 8080

#Start API
#CMD ["java", " -jar", "LoadTestingAPI.jar"]
CMD java -jar LoadTestingAPI.jar