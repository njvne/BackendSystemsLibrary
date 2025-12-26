package adapters.out.persistence.adapter;

import application.domain.models.Borrow;
import application.domain.results.BorrowResult;
import application.domain.results.BorrowsResult;
import application.domain.results.NoContentResult;
import application.port.out.user.PersistBorrowPort;
import application.port.out.user.ReadAllBorrowsPort;
import application.port.out.user.ReadBorrowByNumberPort;
import application.port.out.user.ReturnBookPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class BorrowBersistenceJpaAdapter implements PersistBorrowPort, ReadAllBorrowsPort, ReadBorrowByNumberPort, ReturnBookPort
{
    @Inject
    EntityManager em;

    @Override
    public NoContentResult createBorrow(Borrow borrow)
    {
        return null;
    }

    @Override
    public BorrowsResult readAllBorrows(long uid)
    {
        return null;
    }

    @Override
    public BorrowResult readBorrowByNumber(long uid, long borrowNumber)
    {
        return null;
    }

    @Override
    public NoContentResult returnBook(long uid, long borrowNumber)
    {
        return null;
    }
}