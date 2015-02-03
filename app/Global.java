import com.avaje.ebean.Ebean;
import dao.*;
import models.Dependencies;
import models.Role;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {
        Dependencies.initialize(new EBeanUserDao(), new EBeanRoleDao(), new EBeanCategoryDao(), new EBeanResourceDao(), new EBeanCommentDao(), new EBeanRateResourceDao());

        if (Dependencies.getRoleDao().getRoleCount() == 0) {
            Ebean.save((List) Yaml.load("initial-data/roles.yml"));
            Ebean.save((List) Yaml.load("initial-data/users.yml"));
        }
    }
}
