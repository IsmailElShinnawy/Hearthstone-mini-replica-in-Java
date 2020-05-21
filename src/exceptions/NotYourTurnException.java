package exceptions;

@SuppressWarnings("serial")
public class NotYourTurnException extends HearthstoneException {

	public NotYourTurnException() {
		super();

	}

	public NotYourTurnException(String message) {
		super(message);

	}

}
