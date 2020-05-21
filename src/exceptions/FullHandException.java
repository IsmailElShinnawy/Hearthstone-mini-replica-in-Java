package exceptions;

import model.cards.Card;

@SuppressWarnings("serial")
public class FullHandException extends HearthstoneException {
	private Card burned;
	public FullHandException(Card b) {
		super();

	}

	public FullHandException(String message, Card b) {
		super(message);
		burned=b;
	}

	public Card getBurned() {
		return burned;
	}

}
