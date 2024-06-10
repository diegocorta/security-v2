package pm.security.v2.api.exception;

public class PasswordNotMatchesException extends RuntimeException{

	private static final long serialVersionUID = -7082390569419214677L;

	private final static String error = "The name or username provided are not valid";
	
	public PasswordNotMatchesException() {
		super(error);
	}
}
