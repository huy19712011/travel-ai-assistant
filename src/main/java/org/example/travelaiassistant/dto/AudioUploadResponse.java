package org.example.travelaiassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AudioUploadResponse {

    private String fileId;
    private String originalFilename;
    private String storedFilename;
    private String contentType;
    private long size;
}
