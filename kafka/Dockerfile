# 베이스 이미지
FROM openjdk:17-jdk-slim

# Kafka 버전
ENV KAFKA_VERSION=3.8.0
ENV SCALA_VERSION=2.13
ENV KAFKA_HOME=/opt/kafka

# 유틸 설치
RUN apt-get update && apt-get install -y wget bash netcat && rm -rf /var/lib/apt/lists/*

# Kafka 다운로드 및 압축 해제
RUN wget https://downloads.apache.org/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz \
    && tar -xzf kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz -C /opt \
    && mv /opt/kafka_${SCALA_VERSION}-${KAFKA_VERSION} $KAFKA_HOME \
    && rm kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz

WORKDIR $KAFKA_HOME

# 포트
EXPOSE 9092

# 설정 파일 복사 (선택)
COPY server.properties $KAFKA_HOME/config/server.properties

# 토픽 자동 생성 스크립트 복사
COPY create-topics.sh /usr/local/bin/create-topics.sh
RUN chmod +755 /usr/local/bin/create-topics.sh

# 실행 스크립트: Zookeeper + Kafka + 토픽 생성
COPY start-kafka.sh /usr/local/bin/start-kafka.sh
RUN chmod +755 /usr/local/bin/start-kafka.sh

ENTRYPOINT ["/usr/local/bin/start-kafka.sh"]