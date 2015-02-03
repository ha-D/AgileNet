package dao;

import models.Category;
import models.Resource;
import models.ResourceType;

import java.util.List;
import java.util.Set;

public interface ResourceDao extends BaseDao<Resource> {
    void update(Resource resource);
    Resource create(Resource resource);
    Resource create(ResourceType resourceType, String name, Set<Category> categories, String description);
    Resource findById(int id);
    List<Resource> findByCriteria(ResourceSearchCriteria criteria);
}
