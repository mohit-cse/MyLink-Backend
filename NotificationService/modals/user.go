package modals

type UserDetails struct {
	Name         string `json:"name"`
	EmailAddress string `json:"email"`
	CountryCode  string `json:"country_code"`
	Phone        string `json:"phone"`
}
