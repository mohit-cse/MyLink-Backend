package configs

import (
	"os"
	"strconv"
	"sync"
)

type SMTPConfig struct {
	SMTP_SERVER   string
	SMTP_PORT     int
	SMTP_SENDER   string
	SMTP_USER     string
	SMTP_PASSWORD string
}

var smtpLock = &sync.Mutex{}
var smtpConfig *SMTPConfig

func GetSMTPConfig() *SMTPConfig {
	if smtpConfig == nil {
		smtpLock.Lock()
		defer smtpLock.Unlock()
		if smtpConfig == nil {
			smtpConfig = &SMTPConfig{}
			smtpConfig.loadConfig()
		}
	}
	return smtpConfig
}

func (config *SMTPConfig) loadConfig() {
	value, present := os.LookupEnv("SMTP_SERVER")
	if !present {
		panic("Env variable `SMTP_SERVER` is not set")
	}
	config.SMTP_SERVER = value
	value, present = os.LookupEnv("SMTP_PORT")
	if !present {
		panic("Env variable `SMTP_PORT` is not set")
	}
	port, err := strconv.Atoi(value)
	if err != nil {
		panic("Value of `SMTP_PORT` is not valid port")
	}
	config.SMTP_PORT = port
	value, present = os.LookupEnv("SMTP_SENDER")
	if !present {
		panic("Env variable `SMTP_SENDER` is not set")
	}
	config.SMTP_SENDER = value
	value, present = os.LookupEnv("SMTP_USER")
	if !present {
		panic("Env variable `SMTP_USER` is not set")
	}
	config.SMTP_USER = value
	value, present = os.LookupEnv("SMTP_PASSWORD")
	if !present {
		panic("Env variable `SMTP_PASSWORD` is not set")
	}
	config.SMTP_PASSWORD = value
}
