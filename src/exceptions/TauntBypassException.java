package exceptions;

@SuppressWarnings("serial")
public class TauntBypassException extends HearthstoneException {
	public TauntBypassException() {
		super();
	}

	public TauntBypassException(String message) {
		super(message);
	}
}
