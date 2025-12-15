package application.domain.results;

import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.XsiNilLoader;

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
        return this.isEmpty();
    }
}