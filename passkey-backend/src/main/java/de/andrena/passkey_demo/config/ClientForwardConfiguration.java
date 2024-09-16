package de.andrena.passkey_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@Configuration
public class ClientForwardConfiguration implements WebMvcConfigurer {

    private static final String ROOT = "/";

    @Override public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{path1:[^\\.]*}")
                .setViewName(ROOT);
        registry.addViewController("/{path1}/{path2:[^\\.]*}")
                .setViewName(ROOT);
        registry.addViewController("/{path1}/{path2}/{path3:[^\\.]*}")
                .setViewName(ROOT);
        registry.addViewController("/{path1}/{path2}/{path3}/{path4:[^\\.]*}")
                .setViewName(ROOT);
        registry.addViewController("/{path1}/")
                .setViewName(ROOT);
        registry.addViewController("/{path1}/{path2}/")
                .setViewName(ROOT);
        registry.addViewController("/{path1}/{path2}/{path3}/")
                .setViewName(ROOT);
        registry.addViewController("/{path1}/{path2}/{path3}/{path4}/")
                .setViewName(ROOT);
    }
}
