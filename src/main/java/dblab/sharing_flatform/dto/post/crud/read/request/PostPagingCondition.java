package dblab.sharing_flatform.dto.post.crud.read.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

public class PostPagingCondition {
    @NotNull
    @PositiveOrZero(message = "0 이상의 올바른 페이지 번호를 입력해주세요.")
    private Integer page;

    @NotNull
    @Positive(message = "1 이상의 올바른 페이지당 글 개수를 입력해주세요.")
    private Integer size;

    // 검색 조건
    private List<Long> memberId = new ArrayList<>();
    private List<Long> categoryId = new ArrayList<>();

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
        return 10; // 예시로 10으로 설정
    }

}
