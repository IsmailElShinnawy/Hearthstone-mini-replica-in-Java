package exceptions;

@SuppressWarnings("serial")
public abstract class HearthstoneException extends Exception {
	public HearthstoneException() {
		super();
	}

	public HearthstoneException(String message)

	{
		super(message);
	}
}
