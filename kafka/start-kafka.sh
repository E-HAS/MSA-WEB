#!/bin/bash
set -e

echo "Starting Zookeeper..."
$KAFKA_HOME/bin/zookeeper-server-start.sh -daemon $KAFKA_HOME/config/zookeeper.properties

# Zookeeper 기동 대기
sleep 5

echo "Starting Kafka Broker..."
$KAFKA_HOME/bin/kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties

# Kafka 기동 대기
sleep 10

echo "Creating topics..."
/usr/local/bin/create-topics.sh

echo "Kafka is running..."
tail -f /dev/null