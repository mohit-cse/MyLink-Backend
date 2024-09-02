package configs

import (
	"os"
	"sync"
)

type GRPCConfig struct {
	GRPCServer string
}

var grpcLock = &sync.Mutex{}
var grpcConfig *GRPCConfig

func GetGRPCConfig() *GRPCConfig {
	if grpcConfig == nil {
		grpcLock.Lock()
		defer grpcLock.Unlock()
		if grpcConfig == nil {
			grpcConfig = &GRPCConfig{}
			grpcConfig.loadConfig()
		}
	}
	return grpcConfig
}

func (config *GRPCConfig) loadConfig() {
	value, present := os.LookupEnv("GRPC_SERVER")
	if !present {
		panic("Env variable `GRPC_SERVER` is not set")
	}
	config.GRPCServer = value
}
