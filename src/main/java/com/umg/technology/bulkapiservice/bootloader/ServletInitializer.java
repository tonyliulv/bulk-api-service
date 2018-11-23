package com.umg.technology.bulkapiservice.bootloader;

import com.umg.technology.bulkapiservice.BulkApiServiceApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Extending the SpringBootServletInitializer class also allows us to configure
// our application when it’s run by the servlet container, by overriding the configure() method.
// https://www.baeldung.com/spring-boot-servlet-initializer
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BulkApiServiceApplication.class);
    }

}
