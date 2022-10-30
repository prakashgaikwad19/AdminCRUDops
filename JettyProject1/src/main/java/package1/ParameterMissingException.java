package package1;

public class ParameterMissingException extends Exception {
	
	public ParameterMissingException(String message) {
		super(message);
	}
	
	public ParameterMissingException() {
		super();
	}
}
