# Use Linux Alpine
FROM alpine

# Install java and docker
RUN apk add --update --no-cache openjdk17-jre

#Get API
ADD items-0.0.1-SNAPSHOT.jar items-0.0.1-SNAPSHOT.jar

EXPOSE 8080

#Start API
CMD java -jar items-0.0.1-SNAPSHOT.jar
