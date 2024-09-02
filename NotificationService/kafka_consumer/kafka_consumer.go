package kafka_consumer

import (
	"encoding/json"
	"log"

	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/configs"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/kafka_producer"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/modals"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/services"
)

type KafkaConsumer struct {
	kafkaConfig *configs.KafkaConfig
}

func (consumer *KafkaConsumer) Initialize() {
	consumer.kafkaConfig = configs.GetKafkaConfig()
}

func (consumer *KafkaConsumer) StartKafkaSubscriber() {
	kafkaConfig := consumer.kafkaConfig
	twillio := services.Twillio{}
	twillio.Initialize()
	kafkaProducer := kafka_producer.GetProducerInstance()

	con, err := consumer.connectConsumer()
	panicError(err)
	defer con.Close()

	con.SubscribeTopics([]string{kafkaConfig.ConsumerTopic}, nil)
	for {
		msg, err := con.ReadMessage(-1)
		if err != nil {
			log.Println(err)
			continue
		}
		var kafkaMessage modals.KafkaMessage
		err = json.Unmarshal(msg.Value, &kafkaMessage)
		if err != nil {
			log.Println(err)
			continue
		}

		message := &services.Message{KafkaMessage: &kafkaMessage, Twillio: &twillio, Producer: kafkaProducer}
		go message.DeliverMessage()
	}
}

func (consumer *KafkaConsumer) connectConsumer() (*kafka.Consumer, error) {
	kafkaConfig := consumer.kafkaConfig
	conn, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": kafkaConfig.KafkaServerAddress,
		"group.id":          kafkaConfig.ConsumerGroup,
		"auto.offset.reset": "earliest",
	})

	if err != nil {
		return nil, err
	}

	return conn, nil
}

func panicError(err error) {
	if err != nil {
		panic(err)
	}
}
