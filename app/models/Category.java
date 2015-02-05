package models;

import dao.CategoryDao;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public String getJson(){
        String children = "[";
        for(Category c: this.children)
            children+=c.getJson()+", ";
        children+="]";
        return "{id: "+this.id+", name: '"+this.name+"', children: "+children+"}";
    }
}
