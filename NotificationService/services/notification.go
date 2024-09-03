package services

import (
	"strings"

	"github.com/mohit-cse/MyLink-Backend/NotificationService/modals"
)

type NotificationService struct {
	twillio         *Twillio
	allowedChannels map[string]bool
}

func (service *NotificationService) Initialize() {
	service.twillio = &Twillio{}
	service.twillio.Initialize()
	service.allowedChannels = map[string]bool{"email": true, "phone": true}
}

func (service *NotificationService) SendNotification(request *modals.NotificationRequest) (*[]modals.NotificationResponse, string) {
	kafkaMessage := modals.KafkaMessage{Channel: request.Channel, EmailSubject: request.EmailSubject, EmailMessage: request.EmailMessage, PhoneMessage: request.PhoneMessage, UserDetails: request.UserDetails}
	message := &Message{KafkaMessage: &kafkaMessage, Twillio: service.twillio}
	response, status := message.DeliverMessage()
	return response, status
}

func (service *NotificationService) ValidateRequest(request *modals.NotificationRequest) (bool, string) {
	isValid, message := service.validateChannel(request)
	if !isValid {
		return isValid, message
	}
	isValid, message = service.validateUserDetails(request)
	if !isValid {
		return isValid, message
	}
	isValid, message = service.validateEmail(request)
	if !isValid {
		return isValid, message
	}
	isValid, message = service.validatePhone(request)
	if !isValid {
		return isValid, message
	}
	return true, ""
}

func (service *NotificationService) validateUserDetails(request *modals.NotificationRequest) (bool, string) {
	if (request.UserDetails == modals.UserDetails{}) {
		return false, "Missing `user_details`"
	}
	if request.UserDetails.Name == "" {
		return false, "Missing `user_details.name`"
	}
	return true, ""
}

func (service *NotificationService) validatePhone(request *modals.NotificationRequest) (bool, string) {
	missingParams := []string{}
	for _, channel := range request.Channel {
		if channel == "phone" {
			if request.PhoneMessage == "" {
				missingParams = append(missingParams, "`phone_message`")
			}
			if request.UserDetails.Phone == "" {
				missingParams = append(missingParams, "`user_details.phone`")
			}
			if len(missingParams) != 0 {
				message := strings.Join(missingParams, ",")
				message = "Following parameters are missing: [" + message + "]"
				return false, message
			}
		}
	}
	return true, ""
}

func (service *NotificationService) validateEmail(request *modals.NotificationRequest) (bool, string) {
	missingParams := []string{}
	for _, channel := range request.Channel {
		if channel == "email" {
			if request.EmailSubject == "" {
				missingParams = append(missingParams, "`email_subject`")
			}
			if request.EmailMessage == "" {
				missingParams = append(missingParams, "`email_message`")
			}
			if request.UserDetails.EmailAddress == "" {
				missingParams = append(missingParams, "`user_details.email`")
			}
			if len(missingParams) != 0 {
				message := strings.Join(missingParams, ",")
				message = "Following parameters are missing: [" + message + "]"
				return false, message
			}
		}
	}
	return true, ""
}

func (service *NotificationService) validateChannel(request *modals.NotificationRequest) (bool, string) {
	invalidChannel := []string{}
	for _, channel := range request.Channel {
		if !service.allowedChannels[channel] {
			invalidChannel = append(invalidChannel, channel)
		}
	}
	if len(invalidChannel) != 0 {
		message := strings.Join(invalidChannel, ",")
		message = "Following channels are invalid: [" + message + "]"
		return false, message
	}
	return true, ""
}
