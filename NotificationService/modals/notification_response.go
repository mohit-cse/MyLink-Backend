package modals

type NotificationResponse struct {
	IsDelivered bool   `json:"is_delivered"`
	Channel     string `json:"channel"`
	Message     string `json:"message,omitempty"`
}
