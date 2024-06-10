package pm.security.v2.api.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenApi (SpringDoc + Swagger) configuration class (https://springdoc.org)
 * 
 */
@Configuration
@PropertySource({ "classpath:application.properties" })
public class OpenApiConfig {

	// Environment properties
	@Value("${server.servlet.context-path}")
	String contextPath;

	// Properties read from config file
	private @Value("${app.info.name}") String infoName;
	private @Value("${app.info.description}") String infoDescription;
	private @Value("${app.info.version}") String infoVersion;
	private @Value("${app.info.termsOfService}") String infoTermsOfService;
	private @Value("${app.license.name}") String licenseName;
	private @Value("${app.license.url}") String licenseUrl;
	private @Value("${app.contact.name}") String contactName;
	private @Value("${app.contact.url}") String contactUrl;
	private @Value("${app.contact.email}") String contactEmail;
	private @Value("${app.externaldocs.description}") String externaldocsDescription;
	private @Value("${app.externaldocs.url}") String externaldocsUrl;

	@Bean
	public OpenAPI springShopOpenAPI() {

		return new OpenAPI().addServersItem(getServerInfo()).info(getApiInfo()).externalDocs(getExternalDocInfo())
				.components(getComponentsInfo()).addSecurityItem(getSecurityRequeriment());
//		return new OpenAPI().info(getApiInfo()).externalDocs(getExternalDocInfo())
//				.components(getComponentsInfo()).addSecurityItem(getSecurityRequeriment());
	}

	/**
	 * Returns server details
	 * 
	 * @return the Server object with everything needed
	 */
	private Server getServerInfo() {

		return new Server().url(contextPath);
	}

	/**
	 * Returns the api info to be used in the API doc generation
	 * 
	 * @return the ApiInfo object with everything needed
	 */
	private Info getApiInfo() {

		return new Info().title(infoName).description(infoDescription).version(infoVersion).license(getLicenseInfo())
				.termsOfService(infoTermsOfService).contact(getContactInfo());
	}

	/**
	 * Returns api license details
	 * 
	 * @return the License object with everything needed
	 */
	private License getLicenseInfo() {

		return new License().name(licenseName).url(licenseUrl);
	}

	/**
	 * Returns api lecense details
	 * 
	 * @return the Contact object with everything needed
	 */
	private Contact getContactInfo() {

		return new Contact().name(contactName).email(contactEmail).url(contactUrl);
	}

	/**
	 * Returns api external documentation info
	 * 
	 * @return the ExternalDocumentation object with everything needed
	 */
	private ExternalDocumentation getExternalDocInfo() {

		return new ExternalDocumentation().description(externaldocsDescription).url(externaldocsUrl);
	}

	/**
	 * Returns components info
	 * 
	 * @return the Components object with everything needed
	 */
	private Components getComponentsInfo() {

		// Para autenticacion basica
		// "basicScheme", new
		// SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")

		return new Components().addSecuritySchemes("bearer-jwt",
				new SecurityScheme().description("Token (JWT) authentication").type(SecurityScheme.Type.HTTP)
						.in(SecurityScheme.In.HEADER).name("Authorization").scheme("bearer").bearerFormat("JWT"));
	}

	/**
	 * Returns security configuration
	 * 
	 * @return the Components object with everything needed
	 */
	private SecurityRequirement getSecurityRequeriment() {

		return new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write"));
	}

}
