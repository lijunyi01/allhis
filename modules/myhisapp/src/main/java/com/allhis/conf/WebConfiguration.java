package com.allhis.conf;

import com.allhis.interceptor.MyInterceptor1;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Created by ljy on 16/10/25.
 * ok
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
//    @Bean
//    public RemoteIpFilter remoteIpFilter() {
//        return new RemoteIpFilter();
//    }

//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        return new LocaleChangeInterceptor();
//    }

    @Bean
    public MyInterceptor1 myInterceptor1(){
        return new MyInterceptor1();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(myInterceptor1());
    }
}
