package application.domain.results;


public class SingleModelResult<E> extends AbstractResult
{

    protected E result;

    protected boolean found;

    public SingleModelResult()
    {
        this.found = false;
    }

    public SingleModelResult(final E result)
    {
        this.result = result;
        this.found = (result!=null);
    }

    public E getResult()
    {
        return result;
    }

    @Override
    public boolean isEmpty()
    {
        return result == null;
    }
}