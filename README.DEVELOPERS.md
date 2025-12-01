# Developer README

## Build the Docker image

For **Unix based systems** and **Windows Powershell**:
```shell
./gradlew clean assemble && docker build -t specmatic-order-inventory-wsdl-java .
```

For **Windows Command Prompt**:
```shell
gradlew clean assemble && docker build -t specmatic-order-inventory-wsdl-java .
```
