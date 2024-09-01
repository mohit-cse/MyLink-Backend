package modals

type KafkaMessage struct {
	ID           string      `json:"id"`
	UserId       string      `json:"user_id"`
	Channel      []string    `json:"channel"`
	EventSource  string      `json:"event_source"`
	EmailSubject string      `json:"email_subject,omitempty"`
	PhoneMessage string      `json:"phone_message,omitempty"`
	EmailMessage string      `json:"email_message,omitempty"`
	UserDetails  UserDetails `json:"user_details,omitempty"`
}
