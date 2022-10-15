package jjfactory.webclient.global.ex;


import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(400,  "올바르지 않은 형식입니다."),
    INVALID_ENCODE_TYPE(400,  "암/복호화가 불가능합니다."),
    METHOD_NOT_ALLOWED(405,  "지원하지 않는 메소드입니다."),
    ENTITY_NOT_FOUND(400, " 해당 엔티티를 찾을 수가 없습니다."),
    INTERNAL_SERVER_ERROR(500, "알 수 없는 에러 (서버 에러)"),
    RETROFIT_NETWORK_ERROR(500, "서버간 연결에 실패하였습니다."),
    FILE_UPLOAD_ERROR(500, "AWS 파일 업로드 중 에러가 발생하였습니다."),
    INVALID_TYPE_VALUE(400, "타입이 올바르지 않습니다."),
    INVALID_TYPE_VALUE2(400, "하이픈이나 문자를 포함할 수 없습니다." ),
    INVALID_LENGTH_VALUE(400, "유효하지 않은 길이입니다." ),
    DUPLICATE_MEMBER(400,  "중복된 회원입니다."),
    DUPLICATE_ENTITY(400,  "이미 중복된 값이 존재합니다."),
    INVALID_REQUEST(400, "현재 접근할 수 없는 상태입니다." ),
    HANDLE_ACCESS_DENIED(403,  "권한이 없습니다."),
    PASSWORD_NOT_MATCH(403,  "비밀번호가 올바르지 않습니다"),
    HANDLE_INVALID_TOKEN(401,  "토큰이 없거나 올바르지 않습니다."),

    NOT_FOUND_USER(509,  "존재하지 않는 회원입니다.");

    private final int status;
    private final String message;


   ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
