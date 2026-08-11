package com.laptrinhfulllstack.commonservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class MailConfig {

    @Primary
    @Bean
    public FreeMarkerConfigurationFactoryBean factoryBean() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        // Cho phép tìm kiếm ở nhiều đường dẫn classpath khác nhau
        // 1. Tìm ở thư mục templates của ứng dụng đang gọi (notification-service)
        // 2. Tìm dự phòng ở thư mục templates bên trong chính commonservice
        bean.setTemplateLoaderPaths("classpath:/templates/", "classpath*:/templates/");
        return bean;
    }
}