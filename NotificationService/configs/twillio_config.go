package configs

import (
	"os"
	"sync"
)

type TwillioConfig struct {
	ACCOUNT_SID string
	AUTH_TOKEN  string
	NUMBER      string
}

var twillioLock = &sync.Mutex{}
var twillioConfig *TwillioConfig

func GetTwillioConfig() *TwillioConfig {
	if twillioConfig == nil {
		twillioLock.Lock()
		defer twillioLock.Unlock()
		if twillioConfig == nil {
			twillioConfig = &TwillioConfig{}
			twillioConfig.loadConfig()
		}
	}
	return twillioConfig
}

func (config *TwillioConfig) loadConfig() {
	value, present := os.LookupEnv("TWILLIO_ACCOUNT_SID")
	if !present {
		panic("Env variable `TWILLIO_ACCOUNT_SID` is not set")
	}
	config.ACCOUNT_SID = value
	value, present = os.LookupEnv("TWILLIO_AUTH_TOKEN")
	if !present {
		panic("Env variable `TWILLIO_AUTH_TOKEN` is not set")
	}
	config.AUTH_TOKEN = value
	value, present = os.LookupEnv("TWILLIO_NUMBER")
	if !present {
		panic("Env variable `TWILLIO_NUMBER` is not set")
	}
	config.NUMBER = value
}
