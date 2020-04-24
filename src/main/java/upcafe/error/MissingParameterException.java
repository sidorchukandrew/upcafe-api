package upcafe.error;

public class MissingParameterException extends RuntimeException {

	public MissingParameterException(String parameter) {
		super("Missing parameter { " + parameter + " }");
	}
}
