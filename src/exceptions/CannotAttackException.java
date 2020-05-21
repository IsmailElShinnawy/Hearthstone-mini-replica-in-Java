package exceptions;

@SuppressWarnings("serial")
public class CannotAttackException extends HearthstoneException {

	public CannotAttackException() {
		super();
	}

	public CannotAttackException(String message) {
		super(message);

	}

}
