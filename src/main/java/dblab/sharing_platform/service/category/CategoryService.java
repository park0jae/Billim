package dblab.sharing_platform.service.category;

import dblab.sharing_platform.domain.category.Category;
import dblab.sharing_platform.dto.category.CategoryCreateRequestDto;
import dblab.sharing_platform.dto.category.CategoryDto;
import dblab.sharing_platform.exception.category.CategoryNotFoundException;
import dblab.sharing_platform.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> readAllCategory() {
        return CategoryDto.toList(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc());
    }

    @Transactional
    public Long createCategory(CategoryCreateRequestDto requestDto) {
        if (requestDto.getParentCategoryName() != null) {
            Category parentCategory = categoryRepository.findByName(requestDto.getParentCategoryName()).orElseThrow(CategoryNotFoundException::new);
            Category category = categoryRepository.save(CategoryCreateRequestDto.toEntity(requestDto, parentCategory));
            return category.getId();
        } else {
            return categoryRepository.save(CategoryCreateRequestDto.toEntity(requestDto, null)).getId();
        }
    }

    @Transactional
    public void deleteCategoryByCategoryName(String categoryName) {
        categoryRepository.delete(categoryRepository.findByName(categoryName).orElseThrow(CategoryNotFoundException::new));
    }
}
