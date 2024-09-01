package configs

import (
	"sync"
)

var lock = &sync.Mutex{}

type Config struct {
	TwillioConfig *twillioConfig
	SMTPConfig    *smtpConfig
	KafkaConfig   *kafkaConfig
}

var configInstance *Config

func GetInstance() *Config {
	if configInstance == nil {
		lock.Lock()
		defer lock.Unlock()
		if configInstance == nil {
			configInstance = &Config{}
			configInstance.loadConfigs()
		}
	}
	return configInstance
}

func (config *Config) loadConfigs() {
	twillioConfig := twillioConfig{}
	twillioConfig.loadConfig()
	config.TwillioConfig = &twillioConfig
	smtpConfig := smtpConfig{}
	smtpConfig.loadConfig()
	config.SMTPConfig = &smtpConfig
	kafkaConfig := kafkaConfig{}
	kafkaConfig.loadConfig()
	config.KafkaConfig = &kafkaConfig
}
