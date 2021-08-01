FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine

LABEL github="https://github.com/anjeyy/coding-challenge"
LABEL maintainer="anjeyy"
LABEL email="andjelko.perisic@gmail.com"

WORKDIR /app

# https://github.com/mhart/alpine-node/issues/48#issuecomment-370171836
RUN addgroup -g 1000 -S anjeyy && \
    adduser -u 1000 -S anjeyy -G anjeyy && \
    chmod 777 -R /tmp && \
    chown -R anjeyy:anjeyy /app

USER anjeyy

COPY target/travel-distance*.jar /app/travel-distance.jar

CMD java -jar /app/travel-distance.jar /var/graph-file.graph
