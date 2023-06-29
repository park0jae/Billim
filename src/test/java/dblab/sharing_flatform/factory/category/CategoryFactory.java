package dblab.sharing_flatform.factory.category;

import dblab.sharing_flatform.domain.category.Category;

public class CategoryFactory {
    public static Category createCategory() {
        return new Category("category1", null);
    }
}
