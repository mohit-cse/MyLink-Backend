package configs

import (
	"os"
	"strconv"
	"sync"
)

type RestConfig struct {
	ServerPort int
}

var restLock = &sync.Mutex{}
var restConfig *RestConfig

func GetRestConfig() *RestConfig {
	if restConfig == nil {
		restLock.Lock()
		defer restLock.Unlock()
		if restConfig == nil {
			restConfig = &RestConfig{}
			restConfig.loadConfig()
		}
	}
	return restConfig
}

func (config *RestConfig) loadConfig() {
	value, present := os.LookupEnv("SERVER_PORT")
	if !present {
		panic("Env variable `SERVER_PORT` is not set")
	}
	port, err := strconv.Atoi(value)
	if err != nil {
		panic("Value of `SERVER_PORT` is not valid port")
	}
	config.ServerPort = port
}
