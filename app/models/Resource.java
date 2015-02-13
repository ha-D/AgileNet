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

    public double rating;

    public ResourceType resourceType;

    public Resource(){}

    /**
     * @return A list of comments given to the resource
     */
    public List<Comment> getComments() {
        List<Comment> ret = new ArrayList<>();
        for(Comment comment: comments)
            ret.add(Dependencies.getCommentDao().findById(comment.id));
        return ret;
    }

    /**
     * @return The average rating given to the resource by users
     */
    public double getRate(){
        double sum = 0;
        for(RateResource rateResource: rates)
            sum += rateResource.rate;
        double rating = sum/Math.max(rates.size(), 1);
        if (this.rating != rating) {
            this.rating = rating;
            Dependencies.getResourceDao().update(this);
        }
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Resource) {
            Resource r = (Resource)o;
            return id == r.id;
        }
        return false;
    }

}
