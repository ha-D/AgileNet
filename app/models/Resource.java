package models;

import dao.ResourceDao;
import play.data.validation.Constraints;

import javax.persistence.*;

import java.util.Set;

@Entity
public final class Resource extends BaseModel<ResourceDao>{
    @Id
    public int id;

    @Constraints.Required(message = "my.required.message")
    @Column(nullable = false)
    public String name;

    @ManyToMany(cascade= CascadeType.ALL)
    public Set<Category> categories;

    @Column
    public String description;

    public String url;
    public String owner;

    public ResourceType resourceType;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Resource) {
            Resource r = (Resource)o;
            return id == r.id;
        }
        return false;
    }

    public Resource(){}

}
