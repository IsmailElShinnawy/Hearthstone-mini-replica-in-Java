package exceptions;

@SuppressWarnings("serial")
public class NotEnoughManaException extends HearthstoneException {
public NotEnoughManaException()
{
	super();
}
public NotEnoughManaException(String message)
{
	super(message);
}
}
