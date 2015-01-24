package models;

import dao.BaseDao;
import play.db.ebean.Model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class BaseModel<T extends BaseDao> extends Model {
    @Transient
    protected T dao;

    public BaseModel(T dao) {
        this.dao = dao;
    }

    public void save() {
        dao.update(this);
    }
}
