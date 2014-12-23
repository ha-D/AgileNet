package utils;

import play.test.FakeApplication;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class BaseTest extends WithApplication {

    @Override
    protected FakeApplication provideFakeApplication() {
        Map<String, String> settings = inMemoryDatabase();
        settings = new HashMap<String, String>(settings);
        settings.put("ebean.default", "models.*");
        return fakeApplication(settings);
    }

}
