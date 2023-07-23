package dblab.sharing_flatform.service.category;

import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.dto.category.crud.CategoryDto;
import dblab.sharing_flatform.dto.category.crud.create.CategoryCreateRequestDto;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dblab.sharing_flatform.factory.category.CategoryFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    Category childCategory;
    Category parentCategory;

    @BeforeEach
    public void beforeEach(){
        parentCategory = createCategory("category1", null);
        childCategory = createCategory("category2", parentCategory);
    }

    @Test
    @DisplayName("카테고리 생성 테스트 (부모 존재 X)")
    public void createCategoryTest(){
        // Given
        CategoryCreateRequestDto categoryCreateRequestDto = new CategoryCreateRequestDto(childCategory.getName(), null);

        given(categoryRepository.save(any(Category.class))).willAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            return savedCategory;
        });

        // When
        categoryService.create(categoryCreateRequestDto);

        // Then
        verify(categoryRepository).save(any(Category.class));
    }
    @Test
    @DisplayName("카테고리 생성 테스트 (부모 존재 O)")
    public void createCategoryWithParentTest(){
        // Given
        CategoryCreateRequestDto categoryCreateRequestDto = new CategoryCreateRequestDto(childCategory.getName(), parentCategory.getName());

        given(categoryRepository.findByName(categoryCreateRequestDto.getParentCategoryName())).willReturn(Optional.of(parentCategory));
        given(categoryRepository.save(any(Category.class))).willAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            return savedCategory;
        });

        // When
        categoryService.create(categoryCreateRequestDto);

        // Then
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    public void deleteCategoryTest(){
        // Given
        given(categoryRepository.findByName(childCategory.getName())).willReturn(Optional.of(childCategory));

        // When
        categoryService.delete(childCategory.getName());

        // Then
        verify(categoryRepository).delete(childCategory);
    }

    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    public void readAllTest(){
        // Given
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(parentCategory);

        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc()).willReturn(categoryList);

        // When
        List<CategoryDto> result = categoryService.readAll();

        // Then
        assertThat(result).hasSize(1);
        CategoryDto parentDto = result.get(0);
        assertThat(parentDto.getName()).isEqualTo("category1");
    }

}
