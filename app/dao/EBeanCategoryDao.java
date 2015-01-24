package dao;

import models.Category;
import play.db.ebean.Model;

public class EBeanCategoryDao implements CategoryDao {
    public static Model.Finder<Integer,Category> find = new Model.Finder<Integer,Category>(
            Integer.class, Category.class
    );

    @Override
    public Category create(String name, String id){
        Category category = new Category();
        category.id = id;
        category.name = name;
        return category;
    }

    @Override
    public Category create(String id, String name, String parent_name){
        Category category = new Category();
        category.id = id;
        category.name = name;
        Category parent = find.where().eq("name", parent_name).findUnique();
        category.parent = parent;
        parent.children.add(category);
        return category;
    }

    @Override
    public void addChild(String parent_name, Category category){
        Category parent = find.where().eq("name", parent_name).findUnique();
        parent.children.add(category);
    }

    @Override
    public void update(Category category){
        category.save();
    }

    @Override
    public void deleteCategory(Integer id){
        Category category = find.byId(id);
    }
}
