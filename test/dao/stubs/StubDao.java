package dao.stubs;

import models.BaseModel;

import java.util.HashMap;
import java.util.Map;

public class StubDao<T extends BaseModel> {
    private int nextId;
    private Map<Integer, T> map;

    public StubDao() {
        map = new HashMap<Integer, T>();
        nextId = 1;
    }

    public void update(T t) {
        throw new StubNotImplementedException();
    }

    public T create(T t) {
        t.id = nextId;
        map.put(nextId, t);
        nextId += 1;
        return t;
    }

    public T findById(int id) {
        return map.get(id);
    }
}
