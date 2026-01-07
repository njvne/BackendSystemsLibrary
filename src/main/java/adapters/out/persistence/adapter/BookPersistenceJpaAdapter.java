package adapters.out.persistence.adapter;

import adapters.out.persistence.Mapper;
import adapters.out.persistence.models.BookCopyJpaEntity;
import adapters.out.persistence.models.BookJpaEntity;
import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.results.BookResult;
import application.domain.results.BooksResult;
import application.domain.results.ErrorCodes;
import application.domain.results.NoContentResult;
import application.port.out.book.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.ArrayList;


@ApplicationScoped
public class BookPersistenceJpaAdapter implements DeleteBookPort, ReadAllBooksPort, ReadBookByIdPort, ReadBooksByFilterPort, UpdateBookPort
{
    @Inject
    EntityManager em;

    private final Mapper mapper = new Mapper();

    @Override
    public NoContentResult deleteBook(BookISBN bookISBN)
    {
        return null;
    }

    @Transactional
    @Override
    public NoContentResult updateOrPersistBook(Book book, BookISBN isbn)
    {
        final var returnValue = new NoContentResult();
        try
        {
            try
            {
                final var res = loadBookById(isbn);
                if(res.getErrorCode() == ErrorCodes.RESOURCE_NOT_FOUND)
                {
                    throw new RuntimeException();
                }
                //TODO: merge entities and shit
            }
            catch (Exception e)
            {
                em.getTransaction().begin();                        //transaction because we persist multiple entities (book AND its book copies)
                book.setIsbn(isbn);
                final var model = this.mapper.bookToEntity(book);
                this.em.persist(model);
                for(int i = 0; i < book.getCopyAmount(); i++)
                {
                    final var copy = new BookCopyJpaEntity();
                    copy.setBook(model);
                    this.em.persist(copy);
                }
                em.getTransaction().commit();
            }

        }
        catch(Exception e)
        {
            returnValue.setError();
            returnValue.setError(500, "Could not persist book");
        }
        return returnValue;
    }

    @Override
    public BooksResult loadAllBooks(int page)
    {
        try
        {
           final var criteriaBuilder = em.getCriteriaBuilder();
           final var criteriaQuery = criteriaBuilder.createQuery(BookJpaEntity.class);
           final var root = criteriaQuery.from(BookJpaEntity.class);
           criteriaQuery.select(root);
           final var results = em.createQuery(criteriaQuery).setFirstResult((page - 1) * 20).setMaxResults((page - 1) * 20 + 21).getResultList();
           final var tempres = this.mapper.booksToDomainModels(results);
           final var domainres = new ArrayList<Book>();
           for(Book book : tempres)
           {
               book = addAmountOfCopies(book);
               domainres.add(book);
           }
           return new BooksResult(domainres);
        }
        catch(NoResultException e)
        {
            BooksResult booksResult = new BooksResult();
            booksResult.setError();
            booksResult.setError(ErrorCodes.RESOURCE_NOT_FOUND, "Not Found");
            return booksResult;
        }
    }

    @Override
    public BookResult loadBookById(BookISBN isbn)
    {
        try
        {
            BookJpaEntity res = this.em.find(BookJpaEntity.class, isbn.getISBN());
            if(res == null){throw new NoResultException();}
            final var book = addAmountOfCopies(this.mapper.bookToDomain(res));
            return new BookResult(book);
        }
        catch(NoResultException e)
        {
            BookResult bookResult = new BookResult();
            bookResult.setError();
            bookResult.setError(ErrorCodes.RESOURCE_NOT_FOUND, "Not Found");
            return bookResult;
        }
    }

    @Override
    public BooksResult loadBookByFilter(int page, String query)
    {
        return null;
    }

    
    public Book addAmountOfCopies(Book book)
    {
        final var criteriaBuilder2 = em.getCriteriaBuilder();
        final var criteriaQuery2 = criteriaBuilder2.createQuery(long.class);
        Root<BookCopyJpaEntity> bookRoot = criteriaQuery2.from(BookCopyJpaEntity.class);
        criteriaQuery2.select(criteriaBuilder2.count(criteriaBuilder2.equal(bookRoot.get("book"), this.mapper.bookToEntity(book))));
        book.setCopyAmount((em.createQuery(criteriaQuery2).getSingleResult()));
        book.setAvailableAmount(1); //TODO
        return book;
    }
}