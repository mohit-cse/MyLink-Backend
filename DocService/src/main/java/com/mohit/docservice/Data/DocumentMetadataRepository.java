package com.mohit.docservice.Data;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentMetadataRepository extends ElasticsearchRepository<DocumentMetadata, String> {
    List<DocumentMetadata> findAllByUserID(String userId);
    Optional<DocumentMetadata> findByDocName(String docName);
}
