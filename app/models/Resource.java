package models;

import dao.ResourceDao;
import play.data.validation.Constraints;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
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

    @ManyToOne
    public User user;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> comments;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    public List<RateResource> rates;


    @Column(nullable = false)
    public Date date;

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

    public double getRate(){
        double sum = 0;
        for(RateResource rateResource: rates)
            sum+= rateResource.rate;
        return sum/rates.size();
    }

}
