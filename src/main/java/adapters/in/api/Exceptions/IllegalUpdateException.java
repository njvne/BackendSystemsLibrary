package adapters.in.api.Exceptions;

public class IllegalUpdateException extends RuntimeException
{
    public IllegalUpdateException(String message)
    {
        super(message);
    }
    public IllegalUpdateException()
    {
        super();
    }
}