package health.data.java.app.config;

import health.data.java.app.util.CommonUtils;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import health.data.java.app.util.ConstantUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"health.data.java.app.repository"})
public class DatasourceConfig {
    private final Environment environment;

    public DatasourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.DB2);
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource());
        entityManager.setPackagesToScan("health.data.java.app.model.dto");
        entityManager.setJpaVendorAdapter(vendorAdapter);
        entityManager.setJpaProperties(additionalJpaProperties());

        return entityManager;
    }

    @Bean
    public DataSource dataSource() {
        String url = "jdbc:db2://%s.c3n41cmd0nqnrk39u98g.databases.appdomain.cloud:%s/bludb:sslConnection=true;";
        String db2HostName = CommonUtils.getSystemEnvProperty(ConstantUtils.DB2_HOSTNAME, null);
        String db2PortNumber = CommonUtils.getSystemEnvProperty(ConstantUtils.DB2_PORT_NUM, null);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
        dataSource.setUrl(String.format(url, db2HostName, db2PortNumber));
        dataSource.setUsername(CommonUtils.getSystemEnvProperty(ConstantUtils.DB2_USERNAME, "db2UserNameError"));
        dataSource.setPassword(CommonUtils.getSystemEnvProperty(ConstantUtils.DB2_PASSWORD, "db2PasswordError"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties additionalJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", this.environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.setProperty("hibernate.dialect", this.environment.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.setProperty("hibernate.current_session_context_class", this.environment.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
        properties.setProperty("hibernate.show_sql", this.environment.getProperty("spring.jpa.show-sql"));
        properties.setProperty("hibernate.format_sql", this.environment.getProperty("spring.jpa.properties.hibernate.format_sql"));
        return properties;
    }
}

