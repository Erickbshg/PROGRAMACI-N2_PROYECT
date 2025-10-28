package gt.edu.umg.demoMicroProfile;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 */
@ApplicationPath("/data")
@ApplicationScoped
public class DemoMicroProfileRestApplication extends Application {
}
