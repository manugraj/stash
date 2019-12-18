package tool.stash.doc;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class StashedDocumentMetadata  extends BasicDocument{

    private String mimeType;

    private Date stashedDate;

    private String remarks;

    @JsonIgnore
    public String getStoredFileName() {
        return storedFileName;
    }

    @JsonIgnore
    public String getUri() {
        return uri;
    }

    private String storedFileName;

    private String uri;

}