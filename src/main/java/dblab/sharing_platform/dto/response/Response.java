package dblab.sharing_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL) // 1. null 값을 가지는 필드는, JSON 응답에 포함되지 않도록 합니다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response {
    private boolean success;
    private int code;
    private Result result;

    public static Response success(int code) {
        return new Response(true, code, null);
    }

    // DTO 객체에 대한 success 처리를 위해 <T> 제네릭 타입 선언
    public static <T> Response success(int code, T data) {
        return new Response(true, code, new Success<>(data));
    }

    public static Response failure(int code, String msg) {
        return new Response(false, code, new Failure(msg));
    }
}
