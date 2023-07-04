package dblab.sharing_flatform.dto.category;

import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.helper.FlatListToHierarchicalHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "전체 카테고리 조회 응답")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryResponseDto {

    @ApiModelProperty(value = "카테고리 ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "카테고리 명", example = "Book")
    private String name;

    @ApiModelProperty(value = "자식 카테고리 리스트")
    private List<CategoryResponseDto> children;

    public static List<CategoryResponseDto> toList(List<Category> categoryList) {
        FlatListToHierarchicalHelper helper = FlatListToHierarchicalHelper.newInstance(
                categoryList,
                c -> CategoryResponseDto.toDto(c),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }

    public static CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(),
                category.getName(),
                new ArrayList<>());
    }
}
