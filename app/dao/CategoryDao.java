package dao;

import models.Category;
import java.util.List;

import java.util.List;

public interface CategoryDao extends BaseDao<Category> {
    Category create(String name);
    Category create(String name, int parentId);
//    void addChild(String parent_name, Category category);
    void update(Category category);
    void deleteCategory(int id);
    List<Category> findRootCategories();
}
