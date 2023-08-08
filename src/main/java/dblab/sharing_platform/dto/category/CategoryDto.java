package dblab.sharing_platform.dto.category;

import dblab.sharing_platform.domain.category.Category;
import dblab.sharing_platform.helper.FlatListToHierarchicalHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "전체 카테고리 조회 응답")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @ApiModelProperty(value = "카테고리 ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "카테고리 명", example = "Book")
    private String name;

    @ApiModelProperty(value = "자식 카테고리 리스트")
    private List<CategoryDto> children;

    @SuppressWarnings("unchecked")
    public static List<CategoryDto> toList(List<Category> categoryList) {
        FlatListToHierarchicalHelper helper = FlatListToHierarchicalHelper.newInstance(
                categoryList,
                c -> CategoryDto.toDto(c),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(),
                category.getName(),
                new ArrayList<>());
    }
}