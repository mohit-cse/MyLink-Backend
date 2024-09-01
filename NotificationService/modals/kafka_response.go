package modals

type KafkaResponse struct {
	ID          string `json:"id"`
	IsDelivered bool   `json:"is_delivered"`
	Channel     string `json:"channel"`
}
