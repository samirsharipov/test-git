package com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SecurityFilterConfig {

    @Autowired
    private JwtFilter jwtFilter;



    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistrationBean() {
        FilterRegistrationBean<JwtFilter> bean = new FilterRegistrationBean<JwtFilter>();
        bean.setFilter(jwtFilter);

        bean.addUrlPatterns("/profile/*");
        bean.addUrlPatterns("/profile/updatePhoto/*");
        bean.addUrlPatterns("/category/adm/*");
        bean.addUrlPatterns("/article_like/*");
        bean.addUrlPatterns("/region/adm/*");
        bean.addUrlPatterns("/types/adm/*");
        bean.addUrlPatterns("/article/adm/publish/notPublish/*");
        bean.addUrlPatterns("/article/adm/notPublish/*");
        bean.addUrlPatterns("/article/adm/*");
        bean.addUrlPatterns("/comment/user/*");
        bean.addUrlPatterns("/comment/adm/*");
        bean.addUrlPatterns("/comment/adm/list/*");
        bean.addUrlPatterns("/comment/filter/*");
        bean.addUrlPatterns("/auth/adm/emailList/*");
        bean.addUrlPatterns("/comment_like/like/*");
        bean.addUrlPatterns("/comment_like/remove/*");
        bean.addUrlPatterns("/saved_article/adm/*");
        bean.addUrlPatterns("/saved_article/adm/delete/*");
        bean.addUrlPatterns("/saved_article/adm/list/*");
        bean.addUrlPatterns("/attache/adm/pagination/*");

        return bean;
    }


}
