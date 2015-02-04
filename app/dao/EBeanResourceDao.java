package dao;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import models.Category;
import models.Resource;
import models.ResourceType;
import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Override
    public List<Resource> findAll() {
        return find.all();
    }

    @Override
    public List<Resource> findByCriteria(ResourceSearchCriteria criteria) {
        ExpressionList<Resource> query = find.where();

        if (!criteria.getResourceTypes().isEmpty()) {
            query = query.where().in("resourceType", criteria.getResourceTypes());
        }

        if (criteria.getCategory() != null) {
            query = query.in("categories.id", getCategoryDescendantIds(criteria.getCategory()));
        }

        if (criteria.getQuery() != null) {
            query = query.or(
                Expr.contains("name", criteria.getQuery()),
                Expr.contains("description", criteria.getQuery())
            );
        }

       return  query.findPagingList(criteria.getPageSize())
                .getPage(criteria.getPageNumber()).getList();
    }

    private List<Integer> getCategoryDescendantIds(Category category) {
        List<Integer> list = new ArrayList<Integer>();
        for (Category desc : category.getDescendants()) {
            list.add(desc.id);
        }
        list.add(category.id);
        return list;
    }

}
