package com.laptrinhfulllstack.bookservice.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);

        // QUAN TRỌNG: Quét đồng thời package của bạn và toàn bộ package Entity của Axon
        em.setPackagesToScan(
                "com.laptrinhfulllstack.bookservice",
                "org.axonframework.eventhandling.tokenstore.jpa",
                "org.axonframework.modelling.saga.repository.jpa",
                "org.axonframework.eventhandling.deadletter.jpa");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        em.setJpaProperties(properties);

        return em;
    }
}