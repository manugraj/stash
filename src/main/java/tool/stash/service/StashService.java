package tool.stash.service;

import org.apache.commons.io.FilenameUtils;
import tool.stash.doc.StashedDocument;
import tool.stash.doc.StashedDocumentMetadata;
import tool.stash.store.StashDefaultStore;
import tool.stash.store.StashDefaultStoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class StashService {

    private final StashDefaultStore<StashedDocument> fileStoreLocation;

    private static final String FILE_STORE = "fs";

    @Autowired
    public StashService(StashDefaultStoreManager storeManager){
        fileStoreLocation = storeManager.createStore(FILE_STORE);
    }

    public StashedDocument save(MultipartFile file) throws IOException {
        return saveActualData(file);
    }

    public Optional<Path> findDoc(String fileName) {
       return fileStoreLocation.find(fileName);
    }

    private StashedDocument saveActualData(MultipartFile file) throws IOException {
        StashedDocument doc = new StashedDocument();
        doc.setFile(file);
        doc.setType(FilenameUtils.getExtension(file.getOriginalFilename()));
        doc.setId(UUID.randomUUID().getLeastSignificantBits());
        StashedDocumentMetadata metadata = new StashedDocumentMetadata();
        metadata.setMimeType(file.getContentType());
        metadata.setStashedDate(new Date());
        Path savedPath = fileStoreLocation.save(doc);
        doc.setMetadata(metadata);
        metadata.setUri(savedPath.toUri().toURL().toExternalForm());
        metadata.setStoredFileName(savedPath.getFileName().toString());
        metadata.setId(doc.getId());
        return doc;
    }
}
