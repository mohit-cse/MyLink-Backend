package services

import (
	"log"

	"github.com/go-mail/mail"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/configs"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/modals"
	"github.com/twilio/twilio-go"
	openapi "github.com/twilio/twilio-go/rest/api/v2010"
)

type Twillio struct {
	client        *twilio.RestClient
	twillioConfig *configs.TwillioConfig
	smtpConfig    *configs.SMTPConfig
}

func (twillio *Twillio) Initialize() {
	twillio.twillioConfig = configs.GetTwillioConfig()
	twillio.smtpConfig = configs.GetSMTPConfig()
	twillioConfig := twillio.twillioConfig
	twillio.client = twilio.NewRestClientWithParams(twilio.ClientParams{Username: twillioConfig.ACCOUNT_SID,
		Password: twillioConfig.AUTH_TOKEN})
}

func (twillio *Twillio) sendMail(email *modals.Email) bool {
	smtpConfig := twillio.smtpConfig
	m := mail.NewMessage()
	m.SetHeader("From", smtpConfig.SMTP_SENDER)
	m.SetHeader("To", email.EmailAddress)
	m.SetHeader("Subject", email.Subject)
	m.SetBody("text/html", email.Message)
	d := mail.NewDialer(smtpConfig.SMTP_SERVER,
		smtpConfig.SMTP_PORT,
		smtpConfig.SMTP_USER,
		smtpConfig.SMTP_PASSWORD)
	if err := d.DialAndSend(m); err != nil {
		log.Println(err)
		return false
	}
	return true
}

func (twillio *Twillio) sendSMS(sms *modals.SMS) bool {
	twilioConfig := twillio.twillioConfig
	params := &openapi.CreateMessageParams{}
	params.SetTo(sms.CountryCode + sms.Phone)
	params.SetFrom(twilioConfig.NUMBER)
	params.SetBody(sms.Message)
	_, err := twillio.client.Api.CreateMessage(params)
	if err != nil {
		log.Println(err)
		return false
	}
	return true
}
