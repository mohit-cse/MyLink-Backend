package modals

type NotificationRequest struct {
	Channel      []string    `json:"channel"`
	EmailSubject string      `json:"email_subject,omitempty"`
	PhoneMessage string      `json:"phone_message,omitempty"`
	EmailMessage string      `json:"email_message,omitempty"`
	UserDetails  UserDetails `json:"user_details,omitempty"`
}
