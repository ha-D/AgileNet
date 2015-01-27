package dao;

import models.Category;
import play.db.ebean.Model;

public class EBeanCategoryDao implements CategoryDao {
    public static Model.Finder<Integer,Category> find = new Model.Finder<Integer,Category>(
            Integer.class, Category.class
    );

    @Override
    public Category create(String name){
        Category category = new Category();
        category.name = name;
        category.parent = null;
        category.save();
        return category;
    }

    @Override
    public Category create(String name, int parentID){
        Category category = create(name);
        Category parent = find.byId(parentID);
        category.parent = parent;
        parent.children.add(category);
        update(parent);
        update(category);
        return category;
    }

    @Override
    public Category create(Category category) {
        category.save();
        return category;
    }

//    @Override
//    public void addChild(String parent_name, Category category){
//        Category parent = find.where().eq("name", parent_name).findUnique();
//        parent.children.add(category);
//    }

    @Override
    public void update(Category category){
        category.update();
    }

    @Override
    public void deleteCategory(int id){
        Category category = find.byId(id);
        for(Category cat: category.children){
            deleteCategory(cat.id);
        }
        category.delete();
    }

    @Override
    public Category findById(int id) {
        return find.byId(id);
    }
}
