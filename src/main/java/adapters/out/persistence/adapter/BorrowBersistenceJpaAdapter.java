package adapters.out.persistence.adapter;

import adapters.out.persistence.Mapper;
import adapters.out.persistence.models.BookCopyJpaEntity;
import adapters.out.persistence.models.BookJpaEntity;
import adapters.out.persistence.models.BorrowingJpaEntity;
import adapters.out.persistence.models.UserJpaEntity;
import application.domain.models.Borrow;
import application.domain.results.BorrowResult;
import application.domain.results.BorrowsResult;
import application.domain.results.ErrorCodes;
import application.domain.results.NoContentResult;
import application.port.out.user.PersistBorrowPort;
import application.port.out.user.ReadAllBorrowsPort;
import application.port.out.user.ReadBorrowByNumberPort;
import application.port.out.user.ReturnBookPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class BorrowBersistenceJpaAdapter implements PersistBorrowPort, ReadAllBorrowsPort, ReadBorrowByNumberPort, ReturnBookPort
{
    @Inject
    EntityManager em;

    private Mapper mapper = new Mapper();


    @Override
    public NoContentResult createBorrow(Borrow borrow)
    {
        try {
            final var entityusermodel = this.em.find(UserJpaEntity.class, borrow.getUserid());
            final var book = this.em.find(BookJpaEntity.class, borrow.getIsbn());
            TypedQuery<BookCopyJpaEntity> queue = em.createQuery(
                    "FROM BookCopyJpaEntity bc LEFT JOIN BorrowingJpaEntity b ON b.bookcopy = bc AND b.isactive = false WHERE bc.book.isbn = :isbn AND bc.isRetired = false"
                    , BookCopyJpaEntity.class);
            queue.setParameter("isbn", borrow.getIsbn());
            final var entitybookmodels = queue.getResultList();
            if (book == null) {
                final var result = new NoContentResult();
                result.setError(ErrorCodes.RESOURCE_NOT_FOUND, "Book not found");
                return result;
            }
            if (entityusermodel == null) {
                final var result = new NoContentResult();
                result.setError(ErrorCodes.RESOURCE_NOT_FOUND, "User not found");
                return result;
            }
            if (entitybookmodels.isEmpty()) {
                final var result = new NoContentResult();
                result.setError(ErrorCodes.RESOURCE_CONFLICT, "Book does not have any free copies available for borrowing");
                return result;
            }
            BorrowingJpaEntity topersist = this.mapper.borrowToEntity(borrow);
            topersist.setBookcopy(entitybookmodels.getFirst());
            topersist.setUser(entityusermodel);
            this.em.persist(topersist);
            this.em.flush();
            borrow.setId(topersist.getBorrowid());
            return new NoContentResult();
        }
        catch (Exception e)
        {
            final var result = new NoContentResult();
            result.setError();
            return result;
        }
    }

    @Override
    public BorrowsResult readAllBorrows(long uid)
    {
        TypedQuery<BorrowingJpaEntity> find = this.em.createQuery("FROM BorrowingJpaEntity b WHERE b.user.userid = :userid", BorrowingJpaEntity.class);
        find.setParameter("userid", uid);
        final var borrowjpalist = find.getResultList();
        if(borrowjpalist.isEmpty())
        {
            final var result = new BorrowsResult();
            result.setError(ErrorCodes.RESOURCE_NOT_FOUND, "No borrows found for specified user");
            return result;
        }
        final var borrows = this.mapper.borrowsToDomainModels(borrowjpalist);
        return new BorrowsResult(borrows);
    }

    @Override
    public BorrowResult readBorrowByNumber(long borrowNumber)
    {
        final var borrowjpa = this.em.find(BorrowingJpaEntity.class, borrowNumber);
        if (borrowjpa == null)
        {
            final var result = new BorrowResult();
            result.setError(ErrorCodes.RESOURCE_NOT_FOUND, "Specific borrow number not found");
            return result;
        }
        final var domainmodel = this.mapper.borrowToDomain(borrowjpa);
        return new BorrowResult(domainmodel);
    }

    @Override
    public NoContentResult returnBook(long borrowNumber)
    {
        final var borrowjpa = this.em.find(BorrowingJpaEntity.class, borrowNumber);
        final var result = new NoContentResult();
        if (borrowjpa == null)
        {
            result.setError(ErrorCodes.RESOURCE_NOT_FOUND, "Specific borrow number not found");
        }
        else if(!borrowjpa.isIsactive())
        {
            result.setError(ErrorCodes.PRECONDITION_FAILED, "Cannot return this book because the borrow is already marked as returned");
        }
        else
        {
            borrowjpa.setIsactive(false);
            this.em.merge(borrowjpa);
            this.em.flush();
        }
        return result;
    }
}