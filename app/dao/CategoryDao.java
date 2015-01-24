package dao;

import models.Category;

public interface CategoryDao {
    Category create(String id, String name);
    Category create(String id, String name, String parent_name);
    void addChild(String parent_name, Category category);
    void update(Category category);
    void deleteCategory(String id);
}
