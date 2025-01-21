
# Set up Kafka using Docker Compose, which makes it easy to run both Kafka and its dependency ZooKeeper.



```yaml
# docker-compose.yml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "22181:2181"
    networks:
      - kafka-net

  kafka:
    image: confluentinc/cp-kafka:latest
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - "29092:29092"
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    networks:
      - kafka-net

  kafdrop:
    image: obsidiandynamics/kafdrop:latest
    container_name: kafdrop
    depends_on:
      - kafka
    ports:
      - "9000:9000"
    environment:
      KAFKA_BROKERCONNECT: kafka:29092
    networks:
      - kafka-net

networks:
  kafka-net:
    driver: bridge

```

Here's how to use this setup:

1. **Install Docker and Docker Compose** if you haven't already.

2. **Create the docker-compose.yml** file and save it with the content above.

3. **Start the containers**:
```bash
docker-compose up -d
```

4. **Verify the containers are running**:
```bash
docker-compose ps
```

This setup includes:
- ZooKeeper (required for Kafka)
- Kafka broker
- Kafdrop (a web UI for monitoring Kafka)

You can access:
- Kafka at `localhost:9092` (from your Spring Boot application)
- Kafdrop UI at `http://localhost:9000` (to monitor topics, consumers, etc.)

To test if Kafka is working:

1. **Create a topic** (replace 'message-topic' with your topic name):
```bash
docker-compose exec kafka kafka-topics --create --topic message-topic --bootstrap-server kafka:29092 --replication-factor 1 --partitions 1
```

2. **List topics**:
```bash
docker-compose exec kafka kafka-topics --list --bootstrap-server kafka:29092
```

3. **Send a test message**:
```bash
docker-compose exec kafka kafka-console-producer --topic message-topic --bootstrap-server kafka:29092
> Hello World
(Press Ctrl+D to exit)
```

4. **Read messages**:
```bash
docker-compose exec kafka kafka-console-consumer --topic message-topic --from-beginning --bootstrap-server kafka:29092
```

To stop the containers:
```bash
docker-compose down
```

For your Spring Boot application, update the `application.properties`:
```properties
spring.kafka.bootstrap-servers=localhost:9092
```

Common troubleshooting:
1. If you get connection refused errors, ensure all containers are running:
   ```bash
   docker-compose ps
   ```
2. If topics aren't visible, check Kafdrop UI at http://localhost:9000
3. If Spring Boot can't connect, verify the bootstrap servers property is correct



