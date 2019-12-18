package org.nansa.wynss.stash.store;

import org.nansa.wynss.stash.doc.BasicDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class StashDefaultStoreManager {
   private static final String BASE_STORE = "stash.location";

   private final String baseStore;

   @Autowired
   public StashDefaultStoreManager(Environment env){
      baseStore = env.getRequiredProperty(BASE_STORE);
      File baseStoreFile = new File(baseStore);
      if(baseStoreFile.exists()){
         if(baseStoreFile.isFile()) throw new RuntimeException("Base store cannot be a file");
      }else{
         baseStoreFile.mkdirs();
      }
   }

   public <T extends BasicDocument> StashDefaultStore<T> createStore(String path){
      return new StashDefaultStore<>(baseStore.concat(File.separator).concat(path));
   }
}
