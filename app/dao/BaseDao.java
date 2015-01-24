package dao;

import models.User;

public interface BaseDao<T> {
    T findById(int id);
    T create(T object);
    void update(T object);
}
