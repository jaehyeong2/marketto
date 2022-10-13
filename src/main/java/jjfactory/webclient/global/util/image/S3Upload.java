package jjfactory.webclient.global.util.image;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jjfactory.webclient.global.ex.BusinessException;
import jjfactory.webclient.global.ex.ErrorCode;
import jjfactory.webclient.global.slack.SlackService;
import jjfactory.webclient.global.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class S3Upload {
    private AmazonS3 amazonS3;
    private final SlackService slackService;
    public S3Upload(SlackService slackService) {
        this.slackService = slackService;
    }

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostConstruct
    public void init() {
        amazonS3 = AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .build();
    }

    public String upload(MultipartFile file, String path) {
        String fileName = StringUtil.getRandomSHA256(Objects.requireNonNull(file.getOriginalFilename()));
        String key = String.format("%s%s.%s", path, fileName, StringUtil.getExtension(file.getOriginalFilename()));
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(new PutObjectRequest(bucket, key, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("aws s3 upload error",e);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        } catch (SdkClientException e){
            log.error("aws s3 authorize error",e);
            slackService.postSlackMessage("aws 이미지 업로드중 에러가 발생하였습니다." + e.toString());
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        return amazonS3.getUrl(bucket, key).toString();
    }
}
