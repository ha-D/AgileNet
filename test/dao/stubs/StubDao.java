package dao.stubs;

import models.BaseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        if (t.id == 0) {
            t.id = nextId;
            nextId += 1;
        }
        map.put(t.id, t);
        return t;
    }

    public T findById(int id) {
        return map.get(id);
    }

    public List<T> findAll() {
        return new ArrayList<T>(map.values());
    }
}
