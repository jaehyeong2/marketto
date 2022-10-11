package jjfactory.webclient.global.dto.res;

import jjfactory.webclient.global.ex.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String server_message;
    private int server_status;

    public ErrorResponse(final ErrorCode code) {
        this.server_message = code.getMessage();
        this.server_status = code.getStatus();
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}
