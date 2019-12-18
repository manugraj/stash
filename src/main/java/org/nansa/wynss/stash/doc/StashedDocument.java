package org.nansa.wynss.stash.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StashedDocument extends BasicDocument{

    @JsonIgnore
    public MultipartFile getFile() {
        return file;
    }

    private MultipartFile file;
    private StashedDocumentMetadata metadata;
}
