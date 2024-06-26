package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import ru.otus.hw.controllers.RestInterceptor;

import java.util.Locale;

@Configuration
public class LocalizeConfig  implements WebMvcConfigurer {
    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver() {
        var resolver = new CookieLocaleResolver("locale");
        resolver.setDefaultLocale(new Locale("en"));
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new RestInterceptor()).addPathPatterns("/*", "/*/*", "/*/*/*", "/*/*/*/*");
    }

}
