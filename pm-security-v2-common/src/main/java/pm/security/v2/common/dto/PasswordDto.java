package pm.security.v2.common.dto;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import es.common.dto.AbstractCommonDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(collectionRelation = "permissions")
public class PasswordDto extends AbstractCommonDto {

    private static final long serialVersionUID = 4295066675214743638L;

	private Long id;

    // La pass por defecto es 1111111111
    @JsonProperty(access = Access.WRITE_ONLY)
    @NotBlank(message = "The password must exists")
    @Size(min = 10, max = 30, message = "The password must contain between 10 an 30 characters")
    private String password;
    
    @JsonIgnore
    private String encodedPassword;
    
    @NotNull
    private Long userId;
    
}
