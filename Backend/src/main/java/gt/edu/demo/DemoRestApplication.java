package gt.edu.demo;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/data")
@ApplicationScoped
public class DemoRestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Registrar tus controllers
        classes.add(OperacionController.class);
        classes.add(HelloController.class);

        // REGISTRAR EL CORS FILTER
        classes.add(CorsFilter.class);

        return classes;
    }
}