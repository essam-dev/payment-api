FROM openjdk:21-slim

ARG module

ENV MODULE=${module}

WORKDIR "/app"

COPY ./target/${MODULE} .

CMD java -jar ${MODULE}