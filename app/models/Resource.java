package models;

import dao.ResourceDao;
import play.data.validation.Constraints;
import utilities.Dependencies;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public final class Resource extends BaseModel<ResourceDao>{
    @Constraints.Required(message = "my.required.message")
    @Column(nullable = false)
    public String name;

    public List<Comment> getComments() {
        List<Comment> ret = new ArrayList<>();
        for(Comment comment: comments)
            ret.add(Dependencies.getCommentDao().findById(comment.id));
        return ret;
    }

    @ManyToMany(cascade= CascadeType.ALL)
    public Set<Category> categories;

    @Column(columnDefinition = "TEXT")
    public String description;
    public String fileUrl;
    public String url;
    public String owner;

    @ManyToOne(fetch=FetchType.EAGER)
    public User user;

    @OneToMany(mappedBy = "parResource", cascade = CascadeType.ALL)
    public List<Comment> comments;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    public Set<RateResource> rates;


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
            sum += rateResource.rate;
        return sum/Math.max(rates.size(), 1);
    }

}
