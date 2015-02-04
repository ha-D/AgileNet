package dao.stubs;

import dao.ResourceDao;
import dao.ResourceSearchCriteria;
import models.Category;
import models.Resource;
import models.ResourceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StubResourceDao extends StubDao<Resource> implements ResourceDao {
    @Override
    public Resource create(ResourceType resourceType, String name, Set<Category> categories, String description) {
        Resource resource = new Resource();
        resource.resourceType = resourceType;
        resource.name = name;
        resource.categories = categories;
        resource.description = description;
        return create(resource);
    }

    @Override
    public List<Resource> findByCriteria(ResourceSearchCriteria criteria) {
        throw new StubNotImplementedException();
    }
}
