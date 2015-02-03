package dao.stubs;

import dao.ResourceDao;
import models.Category;
import models.Resource;
import models.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StubResourceDao extends StubDao<Resource> implements ResourceDao {
    private int nextId;
    private Map<Integer, Resource> resources;

    public StubResourceDao() {
        resources = new HashMap<Integer, Resource>();
        nextId = 1;
    }

    @Override
    public Resource create(ResourceType resourceType, String name, Set<Category> categories, String description) {
        Resource resource = new Resource();
        resource.resourceType = resourceType;
        resource.name = name;
        resource.categories = categories;
        resource.description = description;
        return create(resource);
    }
}
