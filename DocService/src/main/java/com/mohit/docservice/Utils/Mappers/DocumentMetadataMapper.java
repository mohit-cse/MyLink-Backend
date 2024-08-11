package com.mohit.docservice.Utils.Mappers;

import com.mohit.docservice.DTOs.DocumentMetadataDTO;
import com.mohit.docservice.Data.DocumentMetadata;

public class DocumentMetadataMapper {
    public static DocumentMetadataDTO mapToDocumentMetadataDTO(DocumentMetadata documentMetadata){
        return new DocumentMetadataDTO(documentMetadata.getDocID(), documentMetadata.getDocName(), documentMetadata.getCreatedAt());
    }
}
