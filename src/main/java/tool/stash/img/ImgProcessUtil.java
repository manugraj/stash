package tool.stash.img;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class ImgProcessUtil {

    public static byte[] resizeImage( Integer width, Integer height, Path path) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(path.toFile()).size(width, height).toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static boolean isImg(String file){
        String extension = FilenameUtils.getExtension(file);
        return "png".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension) || "gif".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension);
    }
}
