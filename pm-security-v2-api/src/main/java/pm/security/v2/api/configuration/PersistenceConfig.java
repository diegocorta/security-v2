package pm.security.v2.api.configuration;

import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;



@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
		(basePackages = {"pm.security.v2.api.repository"},
		transactionManagerRef = "tmVT",
		entityManagerFactoryRef = "emfVT")
@PropertySource({ "classpath:datasource.properties" })
@ComponentScan({ "pm.security.v2.api.entity"})
public class PersistenceConfig {
	
	// ##############
	// # Properties #
	// ##############

	// JNDI params
	private @Value("${jndi.name}") String jndiName;

	// Hibernate params
	private @Value("${hibernate.dialect}") String hbDialect;
	private @Value("${hibernate.generateStatistics}") String hbGenerateStatistics;
	private @Value("${hibernate.orderInserts}") String hbOrderInserts;
	private @Value("${hibernate.orderUpdates}") String hbOrderUpdates;
	private @Value("${hibernate.mode}") String hbMode;
	private @Value("${hibernate.timeZone}") String hbTimeZone;
	
	private @Value("${hibernate.formatSql}") String hbFormatSql;
	private @Value("${hibernate.showSql}") String hbShowSql;
	private @Value("${hibernate.autoRegisterUserTypes}") String hbAutoRegisterUserTypes;
	private @Value("${hibernate.databaseZone}") String hbDatabaseZone;

	
	// ###########
	// # Methods #
	// ###########

	/**
	 * Creates and returns a datasource (for production environment)
	 * 
	 * @return the new datasource
	 */
	@Bean(name = "dsVT", destroyMethod = "")
	public DataSource dataSource() throws NamingException {

		// Get datasource from JNDI
		JndiObjectFactoryBean jndiBean = new JndiObjectFactoryBean();
		jndiBean.setJndiName("java:comp/env/" + jndiName);
		jndiBean.afterPropertiesSet();

		return (HikariDataSource) jndiBean.getObject();
	}

	/**
	 * Creates and returns an entity manager factory
	 * 
	 * @return the new entity manager factory
	 */
	@Bean(name = "emfVT")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {

		final LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setPackagesToScan(
				"pm.security.v2.api.entity");
		entityManagerFactory.setPersistenceUnitName("persistence-unit");
		entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
		entityManagerFactory.setJpaProperties(hibernateProperties());

		return entityManagerFactory;
	}

	/**
	 * Creates and returns a transaction manager
	 * 
	 * @return the new transaction manager
	 */
	@Bean(name = "tmVT")
	public PlatformTransactionManager transactionManager() throws NamingException {

		final JpaTransactionManager transactionManager = new JpaTransactionManager();

		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	/**
	 * Sets every hibernate database connection parameter
	 * 
	 * @return hibernate properties to be use during the database conexion setup
	 */
	private final Properties hibernateProperties() {

		final Properties hibernateProperties = new Properties();

		hibernateProperties.setProperty("hibernate.current_session_context_class", "thread");
		hibernateProperties.setProperty("hibernate.generate_statistics", hbGenerateStatistics);
		hibernateProperties.setProperty("hibernate.id.new_generator_mappings", "true");
		hibernateProperties.setProperty("hibernate.jdbc.batch_size", "50");
		hibernateProperties.setProperty("hibernate.jdbc.batch_versioned_data", "true");
		hibernateProperties.setProperty("hibernate.order_inserts", hbOrderInserts);
		hibernateProperties.setProperty("hibernate.order_updates", hbOrderUpdates);
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hbMode);
		hibernateProperties.setProperty("hibernate.jdbc.time_zone", hbTimeZone);
		
		hibernateProperties.setProperty("hibernate.format_sql", hbFormatSql);
		hibernateProperties.setProperty("hibernate.show_sql", hbShowSql);
		hibernateProperties.setProperty("jadira.usertype.autoRegisterUserTypes", hbAutoRegisterUserTypes);
		hibernateProperties.setProperty("jadira.usertype.databaseZone", hbDatabaseZone);

		return hibernateProperties;
	}

//	/**
//	 * Returns the ehcache config file
//	 * 
//	 * @return the ehcache config file path
//	 */
//	public static final String ehcacheConfigFilePath() {
//
//		return Thread.currentThread()
//				.getContextClassLoader()
//				.getResource(HB_LEVEL2_CACHE_FILE_CONFIG)
//				.toString();
//	}

}