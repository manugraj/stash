package tool.stash.store;

import tool.stash.doc.BasicDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class StashDefaultStore<T extends BasicDocument> {

    private final File location;

    public StashDefaultStore(String location){
        this.location = new File(location);
        if(!this.location.exists()) this.location.mkdirs();
    }

    public Path save(T doc) throws IOException {
        if(doc.getId() == null) doc.setId(UUID.randomUUID().getLeastSignificantBits());
        File saveFile = new File(this.location.getAbsolutePath().concat(File.separator).concat(doc.getId().toString()).concat(".").concat(doc.getType()));
        if(saveFile.exists()) saveFile.delete();
        doc.getFile().transferTo(saveFile);
        return saveFile.toPath();
    }

    public Optional<Path> find(String docId) {
        File file = new File(this.location.getAbsolutePath().concat(File.separator).concat(docId));
        if(!file.exists()) return Optional.empty();
        return Optional.of(file.toPath());
    }

}
