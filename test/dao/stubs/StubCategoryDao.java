package dao.stubs;

import dao.CategoryDao;
import models.Category;
import models.Resource;

import java.util.Map;

public class StubCategoryDao extends StubDao<Category> implements CategoryDao {
    private int nextId;
    private Map<Integer, Resource> resources;

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
}
