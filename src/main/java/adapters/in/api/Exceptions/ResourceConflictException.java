package adapters.in.api.Exceptions;

public class ResourceConflictException extends RuntimeException
{
    public ResourceConflictException(String message) {
        super(message);
    }
    public ResourceConflictException()
    {
        super();
    }
}