package main

import (
	"github.com/mohit-cse/MyLink-Backend/NotificationService/kafka_consumer"
)

func main() {
	kafkaConsumer := kafka_consumer.KafkaConsumer{}
	kafkaConsumer.Initialize()
	kafkaConsumer.StartKafkaSubscriber()
}
