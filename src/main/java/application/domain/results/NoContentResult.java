package application.domain.results;

public class NoContentResult extends AbstractResult
{
    public NoContentResult()
    {
        super();
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }
}