FROM amazoncorretto:25.0.2-alpine

RUN apk add --no-cache git curl bash jq && \
    rm -rf /var/cache/apk/*

SHELL ["/bin/bash", "-c"]

WORKDIR /app

EXPOSE 8095

COPY build/libs/specmatic-inventory-api-wsdl-java-1.0.jar /app/inventory-api.jar

CMD java -jar inventory-api.jar
