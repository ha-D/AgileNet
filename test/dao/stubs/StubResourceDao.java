package dao.stubs;

import dao.ResourceDao;
import models.Category;
import models.Resource;
import models.ResourceType;
import models.User;

import java.util.List;
import java.util.Set;

public class StubResourceDao extends StubDao<Resource> implements ResourceDao {
    public Resource create(ResourceType resourceType, String name, Set<Category> categories, String description, User user) {
        Resource resource = new Resource();
        resource.resourceType = resourceType;
        resource.name = name;
        resource.categories = categories;
        resource.description = description;
        resource.user = user;
        return create(resource);
    }

    @Override
    public Resource create(ResourceType resourceType, String name, Set<Category> categories, String description, User user, String fileUrl, String url, String owner) {
        return null;
    }

    @Override
    public List<Resource> findByCriteria(ResourceSearchCriteria criteria) {
        throw new StubNotImplementedException();
    }
}
