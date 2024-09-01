package kafka_producer

import (
	"encoding/json"
	"log"
	"sync"

	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/configs"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/modals"
)

type KafkaProducer struct {
	config *configs.Config
}

var lock = &sync.Mutex{}
var producerInstance *KafkaProducer

func GetProducerInstance() *KafkaProducer {
	if producerInstance == nil {
		lock.Lock()
		defer lock.Unlock()
		if producerInstance == nil {
			producerInstance = &KafkaProducer{}
			producerInstance.initialize()
		}
	}
	return producerInstance
}

func (producer *KafkaProducer) initialize() {
	producer.config = configs.GetInstance()
}

func (producer *KafkaProducer) PublishResponse(message *modals.KafkaResponse) {
	kafkaConfig := producer.config.KafkaConfig

	p, err := producer.connectProducer()
	panicError(err)
	defer p.Close()

	jsonValue, err := json.Marshal(message)
	if err != nil {
		log.Println(err)
	} else {
		err = p.Produce(&kafka.Message{
			TopicPartition: kafka.TopicPartition{Topic: &kafkaConfig.ProducerTopic, Partition: kafka.PartitionAny},
			Value:          jsonValue,
		}, nil)

		if err != nil {
			log.Println(err)
		}
	}
}

func (producer *KafkaProducer) connectProducer() (*kafka.Producer, error) {
	kafkaConfig := producer.config.KafkaConfig
	conn, err := kafka.NewProducer(&kafka.ConfigMap{
		"bootstrap.servers": kafkaConfig.KafkaServerAddress,
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
