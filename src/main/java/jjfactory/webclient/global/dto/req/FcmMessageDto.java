package jjfactory.webclient.global.dto.req;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmMessageDto {
    private String fcmToken;
    private String content;
    private String title;

    @Builder
    public FcmMessageDto(String fcmToken, String content, String title) {
        this.fcmToken = fcmToken;
        this.content = content;
        this.title = title;
    }
}
