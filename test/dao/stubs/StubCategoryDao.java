package dao.stubs;

import dao.CategoryDao;
import models.Category;
import models.Resource;

import java.util.List;
import java.util.Map;

public class StubCategoryDao extends StubDao<Category> implements CategoryDao {
    @Override
    public Category create(String name) {
        Category category = new Category();
        category.name = name;
        return create(category);
    }

    @Override
    public Category create(String name, int id) {
        throw new StubNotImplementedException();
    }

    @Override
    public void deleteCategory(int id) {
        throw new StubNotImplementedException();
    }

    @Override
    public List<Category> findRootCategories() {
        throw new StubNotImplementedException();
    }
}
