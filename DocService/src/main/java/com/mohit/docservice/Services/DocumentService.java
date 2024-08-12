package com.mohit.docservice.Services;

import com.mohit.docservice.DTOs.AuthRequestDTO;
import com.mohit.docservice.DTOs.AuthResponseDTO;
import com.mohit.docservice.DTOs.CreateDocumentResponseDTO;
import com.mohit.docservice.DTOs.DocumentMetadataDTO;
import com.mohit.docservice.Data.DocumentMetadata;
import com.mohit.docservice.Data.DocumentMetadataRepository;
import com.mohit.docservice.HttpRequests.AuthService;
import com.mohit.docservice.Utils.Exceptions.FileExistsException;
import com.mohit.docservice.Utils.Exceptions.NoSuchFileException;
import com.mohit.docservice.Utils.Mappers.DocumentMetadataMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {
    private final DocumentMetadataRepository documentMetadataRepository;
    private final AuthService authService;
    private final MinioService minIOService;
    DocumentService(DocumentMetadataRepository documentMetadataRepository,
                    AuthService authService,
                    MinioService minIOService){
        this.documentMetadataRepository = documentMetadataRepository;
        this.authService = authService;
        this.minIOService = minIOService;
    }

    private String createRandomUserDocId() {
        String docId;
        do {
            docId = UUID.randomUUID().toString();
        } while (documentMetadataRepository.existsById(docId));
        return docId;
    }

    private Optional<String> getUserId(String jwtToken){
        try {
            Call<AuthResponseDTO> auth = authService.authenticateToken(new AuthRequestDTO(jwtToken));
            Response<AuthResponseDTO> response = auth.execute();
            if(response.code() != 200) return Optional.empty();
            AuthResponseDTO result = response.body();
            return Optional.of(result.userId());
        }
        catch (IOException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private ResponseEntity<InputStreamResource> getDoc(DocumentMetadata documentMetadata){
        try {
            Optional<InputStreamResource> inputStreamResourceOptional = minIOService.downloadFile(documentMetadata.getDocPath());
            if(inputStreamResourceOptional.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + documentMetadata.getDocName());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(inputStreamResourceOptional.get(), headers, HttpStatus.OK);
        }
        catch (NoSuchFileException e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<Boolean> deleteDocUtil(DocumentMetadata documentMetadata){
        try {
            if(minIOService.deleteFile(documentMetadata.getDocPath())){
                documentMetadataRepository.deleteById(documentMetadata.getDocID());
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseEntity<CreateDocumentResponseDTO> createDoc(MultipartFile file, String jwtToken){
        Optional<String> userIdOptional = getUserId(jwtToken);
        if(userIdOptional.isEmpty())
            return new ResponseEntity<>(new CreateDocumentResponseDTO(false, null), HttpStatus.UNAUTHORIZED);
        if(file.isEmpty())
            return new ResponseEntity<>(new CreateDocumentResponseDTO(false, null), HttpStatus.BAD_REQUEST);
        String docID = createRandomUserDocId();
        String userId = userIdOptional.get();
        String filePath = userId + "/" + file.getOriginalFilename();
        try {
            if(minIOService.uploadFile(userId, file, filePath)){
                documentMetadataRepository.save(new DocumentMetadata(docID,
                        userId,
                        filePath,
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        System.currentTimeMillis()));
                return new ResponseEntity<>(new CreateDocumentResponseDTO(true, docID), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(new CreateDocumentResponseDTO(false, null), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (FileExistsException e){
            e.printStackTrace();
            return new ResponseEntity<>(new CreateDocumentResponseDTO(false, null), HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<List<DocumentMetadataDTO>> getAllDocs(String jwtToken) {
        Optional<String> userIdOptional = getUserId(jwtToken);
        if(userIdOptional.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        List<DocumentMetadata> allDocs = documentMetadataRepository.findAllByUserID(userIdOptional.get());
        List<DocumentMetadataDTO> allDocsDTO = allDocs.stream().map(DocumentMetadataMapper::mapToDocumentMetadataDTO).toList();

        return new ResponseEntity<>(allDocsDTO, HttpStatus.OK);
    }

    public ResponseEntity<InputStreamResource> getDoc(String jwtToken, String docId) {
        Optional<String> userIdOptional = getUserId(jwtToken);
        if(userIdOptional.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        String userId = userIdOptional.get();

        Optional<DocumentMetadata> documentMetadataOptional = documentMetadataRepository.findById(docId);
        if(documentMetadataOptional.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        DocumentMetadata documentMetadata = documentMetadataOptional.get();
        if(!documentMetadata.getUserID().equals(userId))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        return getDoc(documentMetadata);
    }

    public ResponseEntity<InputStreamResource> getDocByName(String jwtToken, String docName) {
        Optional<String> userIdOptional = getUserId(jwtToken);
        if(userIdOptional.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        String userId = userIdOptional.get();

        Optional<DocumentMetadata> documentMetadataOptional = documentMetadataRepository.findByDocName(docName);
        if(documentMetadataOptional.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        DocumentMetadata documentMetadata = documentMetadataOptional.get();
        if(!documentMetadata.getUserID().equals(userId))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        return getDoc(documentMetadata);
    }

    public ResponseEntity<Boolean> deleteDoc(String jwtToken, String docId) {
        Optional<String> userIdOptional = getUserId(jwtToken);
        if(userIdOptional.isEmpty())
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);

        String userId = userIdOptional.get();

        Optional<DocumentMetadata> documentMetadataOptional = documentMetadataRepository.findById(docId);
        if(documentMetadataOptional.isEmpty())
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

        DocumentMetadata documentMetadata = documentMetadataOptional.get();
        if(!documentMetadata.getUserID().equals(userId))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        return deleteDocUtil(documentMetadata);
    }
    public ResponseEntity<Boolean> deleteDocByName(String jwtToken, String docName) {
        Optional<String> userIdOptional = getUserId(jwtToken);
        if(userIdOptional.isEmpty())
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);

        String userId = userIdOptional.get();

        Optional<DocumentMetadata> documentMetadataOptional = documentMetadataRepository.findByDocName(docName);
        if(documentMetadataOptional.isEmpty())
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

        DocumentMetadata documentMetadata = documentMetadataOptional.get();
        if(!documentMetadata.getUserID().equals(userId))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        return deleteDocUtil(documentMetadata);
    }
}
