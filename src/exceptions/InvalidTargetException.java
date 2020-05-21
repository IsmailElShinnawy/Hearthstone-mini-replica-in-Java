package exceptions;

@SuppressWarnings("serial")
public class InvalidTargetException extends HearthstoneException {

	public InvalidTargetException() {
		super();
	}

	public InvalidTargetException(String message) {
		super(message);
		
	}

}
