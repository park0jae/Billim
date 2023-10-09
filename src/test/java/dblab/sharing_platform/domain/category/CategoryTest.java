package dblab.sharing_platform.domain.category;

import dblab.sharing_platform.factory.category.CategoryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CategoryTest {

    @Test
    @DisplayName("카테고리 생성 테스트")
    public void createCategoryTest(){
        // given
        Category category = CategoryFactory.createCategory();

        // when & then
        assertThat(category.getName()).isEqualTo("category");
    }

    @Test
    @DisplayName("부모 카테고리 및 자식 카테고리 테스트")
    public void parentAndChildCategoryTest(){
        // given
        Category parent = CategoryFactory.createCategoryWithName("parent");
        Category child = CategoryFactory.createCategory("child", parent);


        Assertions.assertThat(child.getParent().getName()).isEqualTo("parent");
    }
}
