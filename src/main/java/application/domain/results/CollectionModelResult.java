package application.domain.results;
import java.util.LinkedList;
import java.util.List;


public class CollectionModelResult<E> extends AbstractResult
{
    protected List<E> result;
    protected int resultsize;


    public CollectionModelResult()
    {
        this.result = new LinkedList<>();
        this.resultsize = 0;
    }


    public CollectionModelResult(final List<E> result)
    {
        this.result = result != null ? result : new LinkedList<>();
        this.resultsize = result.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.result.isEmpty();
    }

    public List<E> getResult()
    {
        return this.result;
    }

    public int getResultSize()
    {
        return this.resultsize;
    }

    public void setResultSize(final int totalSize)
    {
        this.resultsize = totalSize;
    }
}