package dblab.sharing_platform.controller.category;

import dblab.sharing_platform.dto.category.CategoryCreateRequestDto;
import dblab.sharing_platform.dto.response.Response;
import dblab.sharing_platform.service.category.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Category Controller", tags = "Category")
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 전체 조회", notes = "카테고리를 계층형으로 조회")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response readAllCategory() {
        return Response.success(categoryService.readAllCategory());
    }

    @ApiOperation(value = "카테고리 생성", notes = "카테고리를 생성 / ADMIN 전용")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryCreateRequestDto) {
        return Response.success(categoryService.createCategory(categoryCreateRequestDto));
    }

    @ApiOperation(value = "카테고리 삭제", notes = "카테고리를 삭제 / ADMIN 전용")
    @DeleteMapping("/{categoryName}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteCategoryByCategoryName(@ApiParam(name="삭제할 카테고리 이름" , required = true) @PathVariable String categoryName) {
        categoryService.deleteCategoryByCategoryName(categoryName);
        return Response.success();
    }
}
