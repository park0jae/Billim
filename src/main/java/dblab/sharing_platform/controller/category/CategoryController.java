package dblab.sharing_platform.controller.category;

import dblab.sharing_platform.dto.category.CategoryCreateRequestDto;
import dblab.sharing_platform.service.category.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@Api(value = "Category Controller", tags = "Category")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 전체 조회", notes = "카테고리를 계층형으로 조회")
    @GetMapping
    public ResponseEntity readAllCategory() {
        return new ResponseEntity(categoryService.readAllCategory(), OK);
    }

    @ApiOperation(value = "카테고리 생성", notes = "카테고리를 생성 / ADMIN 전용")
    @PostMapping
    public ResponseEntity createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryCreateRequestDto) {
        return new ResponseEntity(categoryService.createCategory(categoryCreateRequestDto), CREATED);
    }

    @ApiOperation(value = "카테고리 삭제", notes = "카테고리를 삭제 / ADMIN 전용")
    @DeleteMapping("/{categoryName}")
    public ResponseEntity deleteCategoryByCategoryName(@ApiParam(name="삭제할 카테고리 이름" , required = true) @PathVariable String categoryName) {
        categoryService.deleteCategoryByCategoryName(categoryName);

        return new ResponseEntity(OK);
    }
}
