package dblab.sharing_flatform.service.category;

import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.dto.category.crud.create.CategoryCreateRequestDto;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static dblab.sharing_flatform.factory.category.CategoryFactory.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    Category category;


    @BeforeEach
    public void beforeEach(){
        category = createCategory();
    }

    @Test
    @DisplayName("카테고리 생성 테스트 (부모 존재 X)")
    public void createCategoryTest(){
        // Given
        CategoryCreateRequestDto categoryCreateRequestDto = new CategoryCreateRequestDto(category.getName(), null);

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
        given(categoryRepository.findByName(category.getName())).willReturn(Optional.of(category));

        // When
        categoryService.delete(category.getName());

        // Then
        verify(categoryRepository).delete(category);
    }


}
