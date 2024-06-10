package pm.security.v2.api.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import es.common.util.ApiErrorUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import pm.security.v2.api.exception.PasswordNotMatchesException;

/**
 * Class for handling exceptions thrown by the controllers
 * 
 * @author soincon.es
 * @version 202203
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler 
	extends ResponseEntityExceptionHandler {

	// ##################
	// # Parent methods #
	// ##################
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			@NonNull Exception ex,
			@Nullable Object body, 
			@NonNull HttpHeaders headers,
			@NonNull HttpStatusCode status,
			@NonNull WebRequest request) {

		if (HttpStatus.INTERNAL_SERVER_ERROR == status) {
			
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, RequestAttributes.SCOPE_REQUEST);
		}

		return new ResponseEntity<>(ApiErrorUtil.buildErrorMsg(ex, status), 
				headers, 
				status);
	}
	
	@Override
	protected ResponseEntity<Object> handleErrorResponseException(
			ErrorResponseException ex, 
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {
		
		if (HttpStatus.INTERNAL_SERVER_ERROR == status) {
			
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, RequestAttributes.SCOPE_REQUEST);
		}

		return new ResponseEntity<>(ApiErrorUtil.buildErrorMsg(ex, status), 
				headers, 
				status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			@NonNull MethodArgumentNotValidException ex,
			@NonNull HttpHeaders headers,
			@NonNull HttpStatusCode status,
			@NonNull WebRequest request) {
		 
		return new ResponseEntity<>(ApiErrorUtil.buildErrorMsg(ex, status), 
				headers, 
				status);
	}
	
	
	
	// #####################
	// # Custom exceptions #
	// #####################
	
	@ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleRuntimeException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(ApiErrorUtil.buildErrorMsg(ex, status),
        		new HttpHeaders(),
        		status);
    }
	
	@ExceptionHandler({ PasswordNotMatchesException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handlePasswordNotMatchesException(Exception ex, WebRequest request) {

        return new ResponseEntity<>(ApiErrorUtil.buildErrorMsg(ex, HttpStatus.UNAUTHORIZED),
        		new HttpHeaders(),
        		HttpStatus.UNAUTHORIZED);
    }
	
	@ExceptionHandler({ EntityNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEntityNotFoundException(Exception ex, WebRequest request) {

        return new ResponseEntity<>(ApiErrorUtil.buildErrorMsg(ex, HttpStatus.NOT_FOUND),
        		new HttpHeaders(),
        		HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler({ EntityExistsException.class })
	@ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleEntityExistsException(Exception ex, WebRequest request) {

        return new ResponseEntity<>(ApiErrorUtil.buildErrorMsg(ex, HttpStatus.CONFLICT),
        		new HttpHeaders(),
        		HttpStatus.CONFLICT);
    }
	
}
