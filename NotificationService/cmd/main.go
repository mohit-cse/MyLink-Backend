package main

import (
	"github.com/mohit-cse/MyLink-Backend/NotificationService/controllers"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/kafka_consumer"
)

func main() {
	kafkaConsumer := kafka_consumer.KafkaConsumer{}
	kafkaConsumer.Initialize()
	go kafkaConsumer.StartKafkaSubscriber()
	restServer := controllers.NotificationController{}
	restServer.Initialize()
	go restServer.StartServer()
	select {}
}
