package configs

import (
	"os"
	"sync"
)

type KafkaConfig struct {
	ConsumerGroup      string
	ConsumerTopic      string
	ProducerTopic      string
	KafkaServerAddress string
}

var kafkaLock = &sync.Mutex{}
var kafkaConfig *KafkaConfig

func GetKafkaConfig() *KafkaConfig {
	if kafkaConfig == nil {
		kafkaLock.Lock()
		defer kafkaLock.Unlock()
		if kafkaConfig == nil {
			kafkaConfig = &KafkaConfig{}
			kafkaConfig.loadConfig()
		}
	}
	return kafkaConfig
}

func (config *KafkaConfig) loadConfig() {
	value, present := os.LookupEnv("KAFKA_CONSUMER_GROUP")
	if !present {
		panic("Env variable `KAFKA_CONSUMER_GROUP` is not set")
	}
	config.ConsumerGroup = value
	value, present = os.LookupEnv("KAFKA_CONSUMER_TOPIC")
	if !present {
		panic("Env variable `KAFKA_CONSUMER_TOPIC` is not set")
	}
	config.ConsumerTopic = value
	value, present = os.LookupEnv("KAFKA_PRODUCER_TOPIC")
	if !present {
		panic("Env variable `KAFKA_PRODUCER_TOPIC` is not set")
	}
	config.ProducerTopic = value
	value, present = os.LookupEnv("KAFKA_SERVER_ADDRESS")
	if !present {
		panic("Env variable `KAFKA_SERVER_ADDRESS` is not set")
	}
	config.KafkaServerAddress = value
}
