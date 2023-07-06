package dblab.sharing_flatform.service.category;

import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.dto.category.crud.create.CategoryCreateRequestDto;
import dblab.sharing_flatform.dto.category.crud.CategoryDto;
import dblab.sharing_flatform.exception.category.CategoryNotFoundException;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> readAll() {
        List<Category> categoryList = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return CategoryDto.toList(categoryList);
    }

    @Transactional
    public void create(CategoryCreateRequestDto categoryCreateRequestDto) {
        if (categoryCreateRequestDto.getParentCategoryName() != null) {
            Category parentCategory = categoryRepository.findByName(categoryCreateRequestDto.getParentCategoryName()).orElseThrow(CategoryNotFoundException::new);
            categoryRepository.save(CategoryCreateRequestDto.toEntity(categoryCreateRequestDto, parentCategory));
        } else {
            categoryRepository.save(CategoryCreateRequestDto.toEntity(categoryCreateRequestDto, null));
        }
    }

    @Transactional
    public void delete(String name) {
        categoryRepository.delete(categoryRepository.findByName(name).orElseThrow(CategoryNotFoundException::new));
    }


}
