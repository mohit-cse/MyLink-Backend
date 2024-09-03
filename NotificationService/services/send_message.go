package services

import (
	"log"

	"github.com/mohit-cse/MyLink-Backend/NotificationService/kafka_producer"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/modals"
)

type Message struct {
	KafkaMessage *modals.KafkaMessage
	Twillio      *Twillio
	Producer     *kafka_producer.KafkaProducer
}

func (message *Message) DeliverMessage() (*[]modals.NotificationResponse, string) {
	failed := false
	success := false
	KafkaMessage := message.KafkaMessage
	response := []modals.NotificationResponse{}
	for _, channel := range KafkaMessage.Channel {
		switch channel {
		case "email":
			isDelivered := message.sendMessageOverEmail()
			response = append(response, modals.NotificationResponse{Channel: channel, IsDelivered: isDelivered})
			if isDelivered {
				success = true
			} else {
				failed = true
			}
		case "phone":
			isDelivered := message.sendMessageOverSMS()
			response = append(response, modals.NotificationResponse{Channel: channel, IsDelivered: isDelivered})
			if isDelivered {
				success = true
			} else {
				failed = true
			}
		}
	}
	var status string
	if success && failed {
		status = "partially_delivered"
	} else if success && !failed {
		status = "success"
	} else {
		status = "failed"
	}
	return &response, status
}

func (message *Message) sendMessageOverSMS() bool {
	kafkaMessage := message.KafkaMessage
	var userDetails *modals.UserDetails
	if (modals.UserDetails{} == kafkaMessage.UserDetails) {
		var isResponse bool
		userDetails, isResponse = GetUserDetails(kafkaMessage.UserId)
		if !isResponse {
			message.Producer.PublishResponse(&modals.KafkaResponse{ID: message.KafkaMessage.ID, IsDelivered: false, Channel: "phone"})
			log.Println("SMS failed for message: " + message.KafkaMessage.ID)
			return false
		}
	} else {
		userDetails = &kafkaMessage.UserDetails
	}
	countryCode := userDetails.CountryCode
	phone := userDetails.Phone
	status := message.Twillio.sendSMS(&modals.SMS{Message: kafkaMessage.PhoneMessage, CountryCode: countryCode, Phone: phone})
	if status {
		log.Println("SMS sent for message: " + message.KafkaMessage.ID)
	} else {
		log.Println("SMS failed for message: " + message.KafkaMessage.ID)
	}
	if message.Producer != nil {
		message.Producer.PublishResponse(&modals.KafkaResponse{ID: message.KafkaMessage.ID, IsDelivered: status, Channel: "phone"})
	}
	return status
}

func (message *Message) sendMessageOverEmail() bool {
	kafkaMessage := message.KafkaMessage
	var userDetails *modals.UserDetails
	if (modals.UserDetails{} == kafkaMessage.UserDetails) {
		var isResponse bool
		userDetails, isResponse = GetUserDetails(kafkaMessage.UserId)
		if !isResponse {
			message.Producer.PublishResponse(&modals.KafkaResponse{ID: message.KafkaMessage.ID, IsDelivered: false, Channel: "email"})
			log.Println("Email failed for message: " + message.KafkaMessage.ID)
			return false
		}
	} else {
		userDetails = &kafkaMessage.UserDetails
	}
	emailAddress := userDetails.EmailAddress
	status := message.Twillio.sendMail(&modals.Email{Subject: kafkaMessage.EmailSubject, Message: kafkaMessage.EmailMessage, EmailAddress: emailAddress})
	if status {
		log.Println("Email sent for message: " + message.KafkaMessage.ID)
	} else {
		log.Println("Email failed for message: " + message.KafkaMessage.ID)
	}
	if message.Producer != nil {
		message.Producer.PublishResponse(&modals.KafkaResponse{ID: message.KafkaMessage.ID, IsDelivered: status, Channel: "email"})
	}
	return status
}
