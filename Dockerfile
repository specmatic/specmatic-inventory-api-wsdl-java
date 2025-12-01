FROM amazoncorretto:21.0.8-alpine

RUN apk add --no-cache git curl bash jq && \
    rm -rf /var/cache/apk/*

SHELL ["/bin/bash", "-c"]

WORKDIR /app

EXPOSE 8090

COPY build/libs/specmatic-order-inventory-wsdl-java-1.0.jar /app/order-inventory.jar

CMD java -jar order-inventory.jar
