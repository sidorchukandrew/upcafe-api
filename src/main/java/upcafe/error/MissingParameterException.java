package upcafe.error;

public class MissingParameterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MissingParameterException(String parameter) {
        super("Missing parameter { " + parameter + " }");
    }
}
