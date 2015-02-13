package models;

import dao.CategoryDao;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import utilities.Dependencies;

@Entity
public class Category extends BaseModel<CategoryDao> {
    @Constraints.Required
    @Column(unique = true, nullable = false)
    public String name;

    @ManyToOne
    public Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    public List<Category> children;

    public Category(){
        children = new ArrayList<Category>();
    }

    /**
     * @return A list of all descendants of the category,
     * i.e. the categories children and the chidlren of it's children
     */
    public List<Category> getDescendants() {
        List<Category> list = new ArrayList<Category>();
        if (children == null) {
            return list;
        }

        list.addAll(children);
        for (Category child : children) {
            list.addAll(child.getDescendants());
        }
        return list;
    }

    /**
     * @return A representation of the category in the JSON format
     */
    public String getJson(){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(Category c: this.children)
            builder.append(c.getJson()+", ");
        builder.append("]");
        return "{id: "+this.id+", name: '"+this.name+"', children: "+builder.toString()+"}";
    }

    /**
     * @return All the root categories in JSON format
     */
    public static String getAllJson(){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(Category c: Dependencies.getCategoryDao().findRootCategories())
            builder.append(c.getJson()+", ");
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Category) {
            Category c = (Category)o;
            if (id != 0 && c.id != 0) {
                return id == c.id;
            }

            if(name == null || c.name == null)
                return false;
            return name.equals(c.name);
        }
        return false;
    }
}
