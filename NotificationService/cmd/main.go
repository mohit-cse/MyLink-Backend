package main

import (
	"github.com/mohit-cse/MyLink-Backend/NotificationService/controllers"
	_ "github.com/mohit-cse/MyLink-Backend/NotificationService/docs"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/kafka_consumer"
)

// @title 	Notification Service
// @version	1.0
// @description Service for delivering notifications
func main() {
	kafkaConsumer := kafka_consumer.KafkaConsumer{}
	kafkaConsumer.Initialize()
	go kafkaConsumer.StartKafkaSubscriber()
	restServer := controllers.NotificationController{}
	restServer.Initialize()
	go restServer.StartServer()
	select {}
}
