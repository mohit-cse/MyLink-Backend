package configs

import (
	"os"
)

type twillioConfig struct {
	ACCOUNT_SID string
	AUTH_TOKEN  string
	NUMBER      string
}

func (config *twillioConfig) loadConfig() {
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
