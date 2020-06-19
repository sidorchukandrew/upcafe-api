package upcafe.error;

public class NonExistentIdFoundException extends RuntimeException {
	 private static final long serialVersionUID = 1L;

	    public NonExistentIdFoundException(String id, String type) {
	        super("Resource { " + type + " } with id { " + id + " } not found.");
	    }
}
