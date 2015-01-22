import com.avaje.ebean.Ebean;
import dao.EBeanRoleDao;
import dao.EBeanUserDao;
import dao.RoleDao;
import models.Dependencies;
import models.Role;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {
        Dependencies.initialize(new EBeanUserDao(), new EBeanRoleDao());

        if (Dependencies.getRoleDao().getRoleCount() == 0) {
            Ebean.save((List) Yaml.load("initial-data/roles.yml"));
        }
    }
}
