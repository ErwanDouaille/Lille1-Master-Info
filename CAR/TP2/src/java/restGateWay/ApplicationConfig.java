
package restGateWay;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;


/**
 * My goal is to define the path application
 * 
 * @author DOUAILLE ERWAN & DOUYLLIEZ MAXIME
 */

@javax.ws.rs.ApplicationPath("resources")
public class ApplicationConfig extends Application {

    /**
     *
     * @return
     */
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(RestGateWay.class);
        resources.add(MultiPartFeature.class);
        return resources;
    }

}
