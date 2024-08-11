package com.mohit.docservice.Controllers;

import com.mohit.docservice.DTOs.CreateDocumentResponseDTO;
import com.mohit.docservice.DTOs.DocumentMetadataDTO;
import com.mohit.docservice.Services.DocumentService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/doc")
public class DocumentController {
    private final DocumentService documentService;
    DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    @PostMapping(value = "upload", headers = ("content-type=multipart/*"), consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateDocumentResponseDTO> uploadDoc(@RequestPart(value = "file",required = true) MultipartFile file, @RequestHeader("Authorization") String jwtToken){
        return documentService.createDoc(file, jwtToken.replace("Bearer ", ""));
    }

    @GetMapping("/allDocs")
    public ResponseEntity<List<DocumentMetadataDTO>> getAllDocs(@RequestHeader("Authorization") String jwtToken){
        return documentService.getAllDocs(jwtToken.replace("Bearer ", ""));
    }

    @GetMapping("/{docId}")
    public ResponseEntity<InputStreamResource> getDoc(@RequestHeader("Authorization") String jwtToken, @PathVariable("docId")String docId){
        return documentService.getDoc(jwtToken.replace("Bearer ", ""), docId);
    }

    @GetMapping("/getDocByName/{docName}")
    public ResponseEntity<InputStreamResource> getDocByName(@RequestHeader("Authorization") String jwtToken, @PathVariable("docName")String docName){
        return documentService.getDocByName(jwtToken.replace("Bearer ", ""), docName);
    }
}
