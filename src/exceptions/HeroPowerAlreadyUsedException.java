package exceptions;

@SuppressWarnings("serial")
public class HeroPowerAlreadyUsedException extends HearthstoneException {

	public HeroPowerAlreadyUsedException() {
		super();
	}

	public HeroPowerAlreadyUsedException(String message) {
		super(message);

	}

}
