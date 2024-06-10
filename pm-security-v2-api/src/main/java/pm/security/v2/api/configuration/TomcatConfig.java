package pm.security.v2.api.configuration;

import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Embedded tomcat configuration class
 * for development environment
 * 
 * @author soincon.es
 * @version 202202
 */
@Configuration
@PropertySource("classpath:/datasource.properties")
public class TomcatConfig {

	// JNDI resources
	private @Value("${jndi.name}") String jndiDatasourceName;

	// Database connection params	
	private @Value("${connection.factory}") String factory;
	private @Value("${connection.driverClassName}") String driverClassName;
	private @Value("${connection.url}") String url;
	private @Value("${connection.username}") String username;
	private @Value("${connection.password}") String password;

	// JDBC pool params	
	private @Value("${datasource.poolName}") String poolName;
	private @Value("${datasource.maximumPoolSize}") String maximumPoolSize;
	private @Value("${datasource.minimumIdle}") String minimumIdle;
	private @Value("${datasource.connectionTimeout}") String connectionTimeout;	
	private @Value("${datasource.autoCommit}") String autoCommit;
	private @Value("${datasource.cachePrepStmts}") String cachePrepStmts;	
	private @Value("${datasource.cachePrepStmtsSize}") String cachePrepStmtsSize;
	private @Value("${datasource.cachePrepStmtsSqlLimit}") String cachePrepStmtsSqlLimit;
	private @Value("${datasource.useServerPrepStmts}") String useServerPrepStmts;
	
	
	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {

		return new TomcatServletWebServerFactory() {

			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				
				tomcat.enableNaming();
				
				return super.getTomcatWebServer(tomcat);
			}

			@Override
			protected void postProcessContext(Context context) {

				setDatasourceResource(context);
			}

			/**
			 * Sets the data source context configuration
			 *
			 * @param context the context to be used
			 */
			private void setDatasourceResource(Context context){

				ContextResource resource = new ContextResource();

				resource.setName(jndiDatasourceName);

				resource.setType(DataSource.class.getName());
				resource.setProperty("factory", factory);
				resource.setProperty("driverClassName", driverClassName);
				resource.setProperty("jdbcUrl", url);
				resource.setProperty("username", username);
				resource.setProperty("password", password);

				resource.setProperty("poolName", poolName);
				resource.setProperty("maximumPoolSize", maximumPoolSize);
				resource.setProperty("minimumIdle", minimumIdle);
				resource.setProperty("connectionTimeout", connectionTimeout);
				resource.setProperty("autoCommit", autoCommit);
				resource.setProperty("dataSource.cachePrepStmts", cachePrepStmts);
				resource.setProperty("dataSource.useServerPrepStmts", useServerPrepStmts);
				resource.setProperty("dataSource.prepStmtCacheSize", cachePrepStmtsSize);
				resource.setProperty("dataSource.prepStmtCacheSqlLimit", cachePrepStmtsSqlLimit);

				context.getNamingResources().addResource(resource);
			}

		};
	}
}
