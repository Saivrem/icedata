package in.ua.icetools.icedata;

import in.ua.icetools.icedata.config.Config;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityWebApplicationInitializer() {
        super(Config.class);
    }
}
