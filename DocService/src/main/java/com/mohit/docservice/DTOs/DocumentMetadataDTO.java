package com.mohit.docservice.DTOs;

public record DocumentMetadataDTO(String docID,
                                  String docName,
                                  String docType,
                                  long docSize,
                                  long createdAt) { }
