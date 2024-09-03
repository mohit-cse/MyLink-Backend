package controllers

import (
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/configs"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/modals"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/services"
)

type NotificationController struct {
	config  *configs.RestConfig
	router  *mux.Router
	service *services.NotificationService
}

type loggingResponseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (l *loggingResponseWriter) WriteHeader(statusCode int) {
	l.statusCode = statusCode
	l.ResponseWriter.WriteHeader(statusCode)
}

func (cont *NotificationController) Initialize() {
	cont.router = cont.createRouter()
	cont.config = configs.GetRestConfig()
	cont.service = &services.NotificationService{}
	cont.service.Initialize()
}

func (cont *NotificationController) StartServer() {
	log.Println("Started HTTP Server")
	err := http.ListenAndServe(":"+strconv.Itoa(cont.config.ServerPort), cont.logRequest(cont.router))
	if errors.Is(err, http.ErrServerClosed) {
		log.Println("Server Closed")
		panic(err)
	} else if err != nil {
		panic(err)
	}
}

func (cont *NotificationController) logRequest(handler http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		l := &loggingResponseWriter{ResponseWriter: w}
		handler.ServeHTTP(l, r)
		log.Printf("%s %s %s %s\n", r.RemoteAddr, r.Method, strconv.Itoa(l.statusCode), r.URL)
	})
}

func (cont *NotificationController) createRouter() *mux.Router {
	router := mux.NewRouter()
	router.HandleFunc("/api/notify", cont.sendNotification).Methods("POST")
	return router
}

func (cont *NotificationController) sendNotification(w http.ResponseWriter, r *http.Request) {
	if r.Header.Get("Content-Type") != "application/json" {
		http.Error(w, "Invalid Content-Type. Only application/json is allowed.", http.StatusBadRequest)
		return
	}

	var request modals.NotificationRequest
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		response := modals.NotificationResponse{IsDelivered: false, Message: "Invalid payload"}
		json.NewEncoder(w).Encode(&response)
		log.Println(err)
		return
	}

	isValid, message := cont.service.ValidateRequest(&request)
	if !isValid {
		w.WriteHeader(http.StatusBadRequest)
		response := modals.NotificationResponse{IsDelivered: false, Message: message}
		json.NewEncoder(w).Encode(&response)
		return
	}

	response, status := cont.service.SendNotification(&request)
	switch status {
	case "partially_delivered":
		w.WriteHeader(http.StatusPartialContent)
	case "failed":
		w.WriteHeader(http.StatusInternalServerError)
	case "success":
		w.WriteHeader(http.StatusOK)
	}
	json.NewEncoder(w).Encode(response)
}
