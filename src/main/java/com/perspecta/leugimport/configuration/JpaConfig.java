package com.perspecta.leugimport.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = {"com.perspecta.leugimport.business.domain"})
@EnableTransactionManagement
@Configuration
public class JpaConfig {

}