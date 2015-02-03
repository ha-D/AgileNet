package dao;

import com.avaje.ebean.Ebean;
import models.*;
import play.db.ebean.Model;

public class EBeanRateResourceDao implements RateResourceDao {

    public Model.Finder<Integer, RateResource> find = new Model.Finder<Integer,RateResource>(
            Integer.class, RateResource.class
    );

    @Override
    public RateResource create(int rate, Resource resource, User user) {
        RateResource rateResource = new RateResource();
        rateResource.rate = rate;
        rateResource.rate=Math.min(rateResource.rate, 5);
        rateResource.rate=Math.max(rateResource.rate, 0);
        rateResource.resource = resource;
        rateResource.user = user;
        resource.rates.add(rateResource);
        Dependencies.getResourceDao().update(resource);
        Ebean.save(rateResource);
        return rateResource;
    }

    @Override
    public RateResource findById(int id) {
        return find.byId(id);
    }

    @Override
    public RateResource create(RateResource rateResource) {
        rateResource.rate=Math.min(rateResource.rate, 5);
        rateResource.rate=Math.max(rateResource.rate, 0);
        if(rateResource.user == null || rateResource.resource==null)
            return null;
        Ebean.save(rateResource);
        return rateResource;
    }

    @Override
    public void update(RateResource rateResource) {
        Ebean.save(rateResource);
        rateResource.rate=Math.min(rateResource.rate, 5);
        rateResource.rate=Math.max(rateResource.rate, 0);
        Ebean.save(rateResource);

    }
}
