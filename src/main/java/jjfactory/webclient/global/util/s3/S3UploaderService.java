package jjfactory.webclient.global.util.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jjfactory.webclient.business.post.dto.req.PostImageCreate;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class S3UploaderService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Autowired
    public S3UploaderService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public PostImageCreate upload(MultipartFile multipartFile, String dirName){
        return upload(multipartFile, bucket, dirName);
    }

    private PostImageCreate upload(MultipartFile uploadFile, String bucketName, String dirName) {
        String saveName = dirName + "/" + UUID.randomUUID() + uploadFile.getName(); // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, bucketName, saveName); // S3로 업로드

        return PostImageCreate.builder()
                .saveName(saveName)
                .imgUrl(uploadImageUrl)
                .originName(uploadFile.getOriginalFilename())
                .build();
    }

    private String putS3(MultipartFile multipartFile, String bucket, String fileName) {
        File file = multipartToFile(multipartFile);
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonS3Exception e) {
            log.error("aws s3 put object error",e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    private File multipartToFile(MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        File file = new File(System.getProperty("user.dir") + storeFileName);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error("aws s3 file Convert error",e);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        return file;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
