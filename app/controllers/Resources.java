package controllers;

import actions.Authorized;
import models.Dependencies;
import models.Resource;
import models.ResourceType;
import org.apache.commons.io.FileUtils;
import play.data.Form;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;

import static play.mvc.Controller.request;
import static play.mvc.Controller.session;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class Resources {
    @Authorized({})
    public static Result newResource() {
        Form<Resource> resourceForm = Form.form(Resource.class);
        return ok(views.html.addResource.render(resourceForm));
    }

    @Authorized({})
    public static Result addResource() {
        Form<Resource> form = Form.form(Resource.class).bindFromRequest();
        if(form.hasErrors())
            return badRequest(views.html.addResource.render(form));
        Resource r = form.get();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart part = body.getFile("content");
        if(r.resourceType!= ResourceType.WEBSITE && part!=null){
            File file = part.getFile();
            System.out.println(file.getName());
            try {
                File newFile = new File("public/resources/" + r.id, file.getName());
                FileUtils.moveFile(file, newFile);
                r.url=newFile.getPath();
            } catch (IOException ioe) {
                System.out.println("Problem operating on filesystem");
            }
        }
        r.user=Dependencies.getUserDao().findByEmail(session().get("email"));
        Dependencies.getResourceDao().create(r);
//        return redirect(routes.Accounts.resourceView(r.id));
        return ok(views.html.index.render());
    }

    @Authorized({})
    public static Result resourceView(Integer id) {
        Resource resource = Dependencies.getResourceDao().findById(id);
        return ok(views.html.resource.render(resource));
    }

}
