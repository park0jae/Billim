package dblab.sharing_platform.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@Data
public class PostPagingCondition {

    @NotNull(message = "페이지 번호를 입력해주세요.")
    @PositiveOrZero(message = "0 이상의 올바른 페이지 번호를 입력해주세요.")
    private Integer page;

    @NotNull(message = "페이지 크기를 입력해주세요.")
    @Positive(message = "1 이상의 올바른 페이지당 글 개수를 입력해주세요.")
    private Integer size;

    // 검색 조건
    private String categoryName;
    private String title;
    private String username;

    public PostPagingCondition() {
        this.page = getDefaultPageNum();
        this.size = getDefaultPageSize();
    }

    private int getDefaultPageNum() {
        // 디폴트 페이지 번호 로직
        return 0; // 예시로 1로 설정
    }

    private int getDefaultPageSize() {
        // 디폴트 페이지당 글 개수 로직
        return 50; // 예시로 50으로 설정
    }

}
