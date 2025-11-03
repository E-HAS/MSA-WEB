#!/bin/bash
KAFKA_HOME=/opt/kafka
BROKER=localhost:9092

# 생성할 토픽 목록
TOPICS=("serversMetrics" "contents" "users")

for topic in "${TOPICS[@]}"; do
  echo "Creating topic: $topic"
  $KAFKA_HOME/bin/kafka-topics.sh --create --if-not-exists \
    --bootstrap-server $BROKER \
    --replication-factor 1 \
    --partitions 3 \
    --topic $topic
done