package dao;

import models.Comment;
import models.RateResource;
import models.Resource;
import models.User;

import java.util.List;

public interface RateResourceDao extends BaseDao<RateResource> {
    RateResource create(int rate, Resource resource, User user);
    void update(RateResource rateResource);
    int getRate(User user, Resource resource);
    List<RateResource> findLatest(int limit);
}
