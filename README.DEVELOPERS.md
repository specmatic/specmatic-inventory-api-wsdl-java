# Developer README

## Build the Docker image

For **Unix based systems** and **Windows Powershell**:

```shell
# Build the application
./gradlew clean assemble
```

```shell
# Build the Docker image
docker build -t specmatic/specmatic-inventory-wsdl-java .
```

For **Windows Command Prompt**:

```shell
# Build the application
gradlew clean assemble
```

```shell
# Build the Docker image
docker build -t specmatic/specmatic-inventory-wsdl-java .
```
