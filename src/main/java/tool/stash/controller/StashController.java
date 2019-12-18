package tool.stash.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import tool.stash.doc.StashedDocument;
import tool.stash.img.ImgProcessUtil;
import tool.stash.service.StashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@CrossOrigin
@RestController
@Api(tags = {"Basic stash endpoint"})
public class StashController {
    private static final String STASH_GET_IMG_FILE_NAME ="/stash/image/{fileName}";
    private static final String STASH_GET_FILE_NAME ="/stash/raw/{fileName}";
    private static final String FILE_NAME_PREFIX = "{fileName}";
    private StashService stashService;
    @Autowired
    public StashController(StashService stashService){
        this.stashService = stashService;
    }


    @PostMapping(value ="/stash/now",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "Dynamic stash operation")
    public String uploadNow(@RequestParam MultipartFile file) throws IOException {
        StashedDocument savedFileDoc = stashService.save(file);
        String fileCtx;
        if(ImgProcessUtil.isImg(file.getOriginalFilename())){
            fileCtx = StringUtils.replace(STASH_GET_IMG_FILE_NAME,FILE_NAME_PREFIX,savedFileDoc.getMetadata().getStoredFileName());
        }else{
            fileCtx = StringUtils.replace(STASH_GET_FILE_NAME,FILE_NAME_PREFIX,savedFileDoc.getMetadata().getStoredFileName());
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString().concat(fileCtx);
    }

    @GetMapping(value = STASH_GET_IMG_FILE_NAME,produces = {MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE})
    @ApiOperation(value = "Retrieve Image file with an option for custom resolution")
    public @ResponseBody
    byte[] getImage(@RequestParam(required = false) String size,@PathVariable String fileName) throws IOException {
        Optional<Path> option = this.stashService.findDoc(fileName);
        if(option.isPresent()){
            Path filePath = option.get();
            if(size != null){
                String[] widthHeight = size.split("x");
                if(widthHeight != null && widthHeight.length == 2 ){
                    int width = Integer.parseInt(widthHeight[0]);
                    int height = Integer.parseInt(widthHeight[1]);
                    if (validWidthHeight(width, height)){
                        return ImgProcessUtil.resizeImage(width, height, filePath);
                    }
                }
            }
            return IOUtils.toByteArray(filePath.toUri());
        }
        return null;
    }

    private boolean validWidthHeight(int width, int height) {
        return width > 0 && height > 0;
    }



    @GetMapping(value = STASH_GET_FILE_NAME,produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ApiOperation(value = "Retrieve raw file")
    public @ResponseBody byte[] raw(@PathVariable String fileName) throws IOException {
        Optional<Path> option = this.stashService.findDoc(fileName);
        if(option.isPresent()){
            return IOUtils.toByteArray(option.get().toUri());
        }
        return null;
    }
}
