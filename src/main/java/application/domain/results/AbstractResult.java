package application.domain.results;

public abstract class AbstractResult
{
    protected boolean hasError;
    protected int errorCode;
    protected String errorMessage;

    protected long databaseExecutionTimeInMs;




    protected AbstractResult()
    {
        this.hasError = false;
    }




    public abstract boolean isEmpty();

    public final void setTime(final long startTime, final long endTime)
    {
        this.databaseExecutionTimeInMs = endTime - startTime;
    }


    public final void setError() {
        this.hasError = true;
    }

    public final boolean hasError()
    {
        return hasError;
    }

    public final void setError(final int errorCode, final String errorMessage)
    {
        this.hasError = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}