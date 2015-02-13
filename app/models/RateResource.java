package models;

import javax.persistence.*;

import dao.RateResourceDao;
import play.data.validation.Constraints;

import java.util.Date;

@Entity
public class RateResource extends BaseModel<RateResourceDao> {
    @Id
    public int id;

    @Constraints.Required
    @Column(nullable = false)
    public int rate;

    @Constraints.Required
    @Column(nullable = false)
    @ManyToOne
    public Resource resource;

    @Constraints.Required
    @Column(nullable = false)
    @ManyToOne
    public User user;

    public Date date;

    @Override
    public boolean equals(Object o) {
        if (o instanceof RateResource) {
            RateResource r = (RateResource)o;
            return (user.equals(r.user) && resource.equals(r.resource));
        }
        return false;
    }
}
