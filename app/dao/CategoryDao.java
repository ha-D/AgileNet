package dao;

import models.Category;

public interface CategoryDao extends BaseDao<Category> {
    Category create(String name);
    Category create(String name, int id);
//    void addChild(String parent_name, Category category);
    void update(Category category);
    void deleteCategory(int id);
}
