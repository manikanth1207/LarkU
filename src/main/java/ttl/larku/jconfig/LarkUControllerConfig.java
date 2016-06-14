package ttl.larku.jconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages={"ttl.larku.controllers"})
@EnableWebMvc
public class LarkUControllerConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private LarkUConfig larkUConfig;
}
