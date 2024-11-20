# Simulator project 
[![Java](https://img.shields.io/badge/Java-%23F7DF1E?style=for-the-badge&logo=java&logoColor=white&labelColor=0a0a0a)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white&labelColor=0a0a0a)](https://spring.io/)
[![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white&labelColor=0a0a0a)](https://kafka.apache.org/)

***
## Development
### Here some instructions you can use to test code while development
1. To check simulator_topic consuming in simulator enter:
```
kafka-console-producer --broker-list localhost:9092 --topic simulator_topic
```
This could help you to send messages directly by entering in console message content, for example:
```
{"buildingId":1,"floors":[{"floorNumber":1,"rooms":[{"roomId":101,"area":45.5,"windows":2,"doors":1,"sensors":[{"sensorId":1001,"sensorType":"Temperature","sensorValue":22.5,"thresholds":{"min":18.0,"max":25.0},"status":"Active"}]}]}]}
```
