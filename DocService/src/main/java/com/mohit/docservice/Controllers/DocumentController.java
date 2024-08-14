package com.mohit.docservice.Controllers;

import com.mohit.docservice.DTOs.CreateDocumentResponseDTO;
import com.mohit.docservice.DTOs.DocumentMetadataDTO;
import com.mohit.docservice.Services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/doc")
@CrossOrigin(origins = "http://api.mohit", maxAge = 3600)
public class DocumentController {
    private final DocumentService documentService;
    DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PostMapping(value = "upload", headers = ("content-type=multipart/*"), consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateDocumentResponseDTO> uploadDoc(@RequestPart(value = "file",required = true) MultipartFile file,
                                                               @RequestHeader("Authorization") String jwtToken){
        return documentService.createDoc(file, jwtToken.replace("Bearer ", ""));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/allDocs")
    public ResponseEntity<List<DocumentMetadataDTO>> getAllDocs(@RequestHeader("Authorization") String jwtToken){
        return documentService.getAllDocs(jwtToken.replace("Bearer ", ""));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{docId}")
    public ResponseEntity<InputStreamResource> getDoc(@RequestHeader("Authorization") String jwtToken,
                                                      @PathVariable("docId")String docId){
        return documentService.getDoc(jwtToken.replace("Bearer ", ""), docId);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/getDocByName/{docName}")
    public ResponseEntity<InputStreamResource> getDocByName(@RequestHeader("Authorization") String jwtToken,
                                                            @PathVariable("docName")String docName){
        return documentService.getDocByName(jwtToken.replace("Bearer ", ""), docName);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping("/{docId}")
    public ResponseEntity<Boolean> deleteDoc(@RequestHeader("Authorization") String jwtToken,
                                             @PathVariable("docId")String docId){
        return documentService.deleteDoc(jwtToken.replace("Bearer ", ""), docId);
    }
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping("/deleteDocByName/{docName}")
    public ResponseEntity<Boolean> deleteDocByName(@RequestHeader("Authorization") String jwtToken,
                                                   @PathVariable("docName")String docName){
        return documentService.deleteDocByName(jwtToken.replace("Bearer ", ""), docName);
    }
}
