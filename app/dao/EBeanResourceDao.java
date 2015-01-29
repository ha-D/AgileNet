package dao;

import com.avaje.ebean.Ebean;
import models.Category;
import models.Resource;
import models.ResourceType;
import play.db.ebean.Model;
import views.html.resource;

import java.util.Set;

/**
 * Created by Phoenix on 29/01/2015.
 */
public class EBeanResourceDao implements ResourceDao {

    public Model.Finder<Integer,Resource> find = new Model.Finder<Integer,Resource>(
            Integer.class, Resource.class
    );


    @Override
    public Resource create(ResourceType resourceType, String name, Set<Category> categories, String description){
        Resource resource = new Resource();
        resource.resourceType = resourceType;
        resource.name = name;
        resource.categories = categories;
        resource.description = description;
        Ebean.save(resource);
        return resource;
    }

    @Override
    public Resource create(Resource resource){
        Ebean.save(resource);
        return resource;
    }

    @Override
    public void update(Resource resource){
        Ebean.update(resource);
    }

    @Override
    public Resource findById(int id){
        return find.where().eq("id", id).findUnique();
    }
}
