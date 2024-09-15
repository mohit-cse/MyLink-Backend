package controllers

import (
	"errors"
	"log"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/configs"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/modals"
	"github.com/mohit-cse/MyLink-Backend/NotificationService/services"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
)

type NotificationController struct {
	config  *configs.RestConfig
	router  *gin.Engine
	service *services.NotificationService
}

func (cont *NotificationController) Initialize() {
	cont.router = cont.createRouter()
	cont.config = configs.GetRestConfig()
	cont.service = &services.NotificationService{}
	cont.service.Initialize()
}

func (cont *NotificationController) StartServer() {
	log.Println("Started HTTP Server")
	err := cont.router.Run(":" + strconv.Itoa(cont.config.ServerPort))
	if errors.Is(err, http.ErrServerClosed) {
		log.Println("Server Closed")
		panic(err)
	}
}

func (cont *NotificationController) createRouter() *gin.Engine {
	router := gin.Default()
	router.GET("/docs/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))
	router.POST("/api/notify", cont.sendNotification)
	return router
}

// CreateTags		godoc
// @Summary			Send notification
// @Description		Sends notification to mentioned channels
// @Param			request body modals.NotificationRequest true "Send notification"
// @Produce			application/json
// @Tags			tags
// @Success			200 {object}  modals.NotificationResponse{}
// @Router			/api/notify [post]
func (cont *NotificationController) sendNotification(c *gin.Context) {
	if c.Request.Header.Get("Content-Type") != "application/json" {
		c.String(http.StatusBadRequest, "Invalid Content-Type. Only application/json is allowed.")
		return
	}

	var request modals.NotificationRequest
	err := c.ShouldBindJSON(&request)
	if err != nil {
		response := modals.NotificationResponse{IsDelivered: false, Message: "Invalid payload"}
		c.JSON(http.StatusBadRequest, response)
		log.Println(err)
		return
	}

	isValid, message := cont.service.ValidateRequest(&request)
	if !isValid {
		response := modals.NotificationResponse{IsDelivered: false, Message: message}
		c.JSON(http.StatusBadRequest, response)
		return
	}

	response, status := cont.service.SendNotification(&request)
	http_status := http.StatusOK
	switch status {
	case "partially_delivered":
		http_status = http.StatusPartialContent
	case "failed":
		http_status = http.StatusInternalServerError
	}
	c.JSON(http_status, response)
}
