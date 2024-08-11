package com.mohit.docservice.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "doc_metadata")
@AllArgsConstructor
@NoArgsConstructor
public class DocumentMetadata {
    @Id
    private String docID;
    private String userID;
    private String docPath;
    private String docName;
    private long createdAt;
}
