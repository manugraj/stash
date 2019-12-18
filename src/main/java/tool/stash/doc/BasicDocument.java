package tool.stash.doc;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public abstract class BasicDocument {
    private Long id;
    private MultipartFile file;
    private String type;
}
