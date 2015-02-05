package dao.impl;

import com.avaje.ebean.Ebean;
import dao.RateResourceDao;
import models.*;
import play.db.ebean.Model;
import utilities.Dependencies;

import java.util.List;

public class RateResourceDaoImpl implements dao.RateResourceDao {

    public Model.Finder<Integer, RateResource> find = new Model.Finder<Integer,RateResource>(
            Integer.class, RateResource.class
    );

    @Override
    public RateResource create(int rate, Resource resource, User user) {
        RateResource rateResource = new RateResource();
        rateResource.rate=Math.min(rate, 5);
        rateResource.rate=Math.max(rateResource.rate, 0);
        rateResource.resource = resource;
        rateResource.user = user;
        if(rateResource.user == null || rateResource.resource==null)
            return null;
        List<RateResource> x = find.where().eq("user", rateResource.user).eq("resource", rateResource.resource).findList();
        if(x.size()==0) {
            Ebean.save(rateResource);
            Dependencies.getRateResourceDao().findById(rateResource.id);
            resource.rates.add(rateResource);
            Dependencies.getResourceDao().update(resource);
            return rateResource;
        }
        else{
            x.get(0).rate = rateResource.rate;
            Dependencies.getRateResourceDao().update(x.get(0));

            resource.rates.remove(x.get(0));
            resource.rates.add(x.get(0));
            Dependencies.getResourceDao().update(resource);
            return x.get(0);
        }
    }

    @Override
    public RateResource findById(int id) {
        return find.byId(id);
    }

    @Override
    public List<RateResource> findAll() {
        return find.all();
    }

    @Override
    public RateResource create(RateResource rateResource) {
        return create(rateResource.rate, rateResource.resource, rateResource.user);
    }

    @Override
    public void update(RateResource rateResource) {
        rateResource.rate=Math.min(rateResource.rate, 5);
        rateResource.rate=Math.max(rateResource.rate, 0);
        Ebean.save(rateResource);

    }

    @Override
    public int getRate(User user, Resource resource) {
        List<RateResource> rateResources = find.where().eq("user", user).eq("resource", resource).findList();
        if(rateResources.size() != 0){
            return rateResources.get(0).rate;
        }
        return 0;
    }
}
