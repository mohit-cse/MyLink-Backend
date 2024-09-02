package services

import (
	"context"
	"log"

	"github.com/mohit-cse/MyLink-Backend/NotificationService/configs"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/grpc/user_contact_details"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/modals"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

func GetUserDetails(userId string) (*modals.UserDetails, bool) {
	userDetails, err := getUserContactDetails(userId)
	if err != nil {
		return nil, false
	}
	return &modals.UserDetails{Name: userDetails.Name, EmailAddress: userDetails.Email, CountryCode: userDetails.CountryCode, Phone: userDetails.Phone}, true
}

func getUserContactDetails(userId string) (*user_contact_details.ContactDetailsResponse, error) {
	grpcConfig := configs.GetGRPCConfig()
	conn, err := grpc.NewClient(grpcConfig.GRPCServer, grpc.WithTransportCredentials(insecure.NewCredentials()))

	if err != nil {
		log.Println(err)
		return nil, err
	}
	defer conn.Close()

	client := user_contact_details.NewUserContactDetailsClient(conn)

	userDetails, err := client.GetUserContactDetails(context.Background(), &user_contact_details.ContactDetailsRequest{UserId: userId})

	if err != nil {
		log.Println(err)
		return nil, err
	}

	return userDetails, nil
}
