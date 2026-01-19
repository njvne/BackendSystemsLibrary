package adapters.out.persistence.adapter;

import application.domain.results.PutStatus;
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
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;


@ApplicationScoped
public class BookPersistenceJpaAdapter implements DeleteBookPort, ReadAllBooksPort, ReadBookByIdPort, ReadBooksByFilterPort, UpdateBookPort
{
    @Inject
    EntityManager em;

    private final Mapper mapper = new Mapper();



    @Override
    @Transactional
    public NoContentResult deleteBook(BookISBN bookISBN)
    {
        BookJpaEntity toDelete = this.em.find(BookJpaEntity.class, bookISBN.getISBN());
        if(toDelete == null)
        {
            final var res = new NoContentResult();
            res.setError(ErrorCodes.RESOURCE_NOT_FOUND, "Book to delete not found");
            return res;
        }
        this.em.remove(toDelete);
        return new NoContentResult();
    }

    @Override
    @Transactional
    public NoContentResult updateOrPersistBook(Book book, BookISBN isbn)        //logic COULD be better fit in domain, creates more back and forth calls though
    {
        final var returnValue = new NoContentResult();
        try
        {
            try
            {
                BookJpaEntity res = this.em.find(BookJpaEntity.class, isbn.getISBN());
                if(res == null)
                {
                    throw new NoResultException();
                }
                final var bookEntity = this.mapper.bookToEntity(book);
                long toBe = book.getCopyAmount();
                long currCopyAmount = addAmountOfCopies(this.mapper.bookToDomain(res));
                if(currCopyAmount != toBe)
                {
                    if(toBe < currCopyAmount)
                    {
                        TypedQuery<BookCopyJpaEntity> queue = em.createQuery(
                                "FROM BookCopyJpaEntity bc LEFT JOIN BorrowingJpaEntity b ON b.bookcopy = bc AND b.isactive = false WHERE bc.book = :book AND bc.isRetired = false"
                                , BookCopyJpaEntity.class);
                        queue.setParameter("book", res);
                        final var nonBorrowedBooks = queue.getResultList();
                        System.out.println("queue size: " + nonBorrowedBooks.size());
                        /*if(currCopyAmount > nonBorrowedBooks.size()) {throw new IllegalArgumentException();}*/
                        while(toBe < currCopyAmount)
                        {
                            nonBorrowedBooks.getFirst().setRetired(true);
                            nonBorrowedBooks.removeFirst();
                            currCopyAmount--;
                        }
                    }
                    while(book.getCopyAmount() > currCopyAmount)
                    {
                        final var copy = new BookCopyJpaEntity();
                        copy.setBook(res);
                        this.em.persist(copy);
                        currCopyAmount++;
                    }
                }
                this.em.lock(res, LockModeType.PESSIMISTIC_WRITE);
                res.setAuthor(bookEntity.getAuthor());
                res.setTitle(bookEntity.getTitle());
                res.setDescription(bookEntity.getDescription());
                this.em.lock(res, LockModeType.NONE);
                this.em.flush();
                returnValue.setError(PutStatus.UPDATED, "");
            }
            catch (NoResultException e)
            {
                //this.em.getTransaction().begin();     "not supported for JTA entity managers"
                book.setIsbn(isbn);
                final var model = this.mapper.bookToEntity(book);
                model.setIsbn(isbn.getISBN());
                this.em.persist(model);
                for(int i = 0; i < book.getCopyAmount(); i++)
                {
                    final var copy = new BookCopyJpaEntity();
                    copy.setBook(model);
                    this.em.persist(copy);
                    this.em.flush();
                }
                //this.em.getTransaction().commit();    "not supported for JTA entity managers"
                returnValue.setError(PutStatus.CREATED, "Created");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            returnValue.setError(ErrorCodes.IMPOSSIBLE_UPDATE, "Error updating or persisting book");
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
           return new BooksResult(addAmountOfCopiesToList(results));
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
            if(res == null)
            {
                throw new NoResultException();
            }
            final var result = this.mapper.bookToDomain(res);
            addAmountOfCopies(result);
            return new BookResult(result);
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
        try
        {
            //SQL injects :c#
            TypedQuery<BookJpaEntity> queue = em.createQuery(
                    "FROM BookJpaEntity book WHERE book.title LIKE :query OR book.author LIKE :query OR book.description LIKE :query"
                    , BookJpaEntity.class);
            queue.setParameter("query", "%" + query + "%");
            queue.setFirstResult((page - 1) * 20);
            queue.setMaxResults((page - 1) * 20 + 21);
            final var res = queue.getResultList();
            if(res == null)
            {
                throw new NoResultException();
            }
            return new BooksResult(addAmountOfCopiesToList(res));
        }
        catch(NoResultException e)
        {
            BooksResult bookResult = new BooksResult();
            bookResult.setError();
            bookResult.setError(ErrorCodes.RESOURCE_NOT_FOUND, "Not Found");
            return bookResult;
        }
    }




    private List<Book> addAmountOfCopiesToList(List<BookJpaEntity> bookJpaEntityList)
    {
        final var domainmodels = this.mapper.booksToDomainModels(bookJpaEntityList);
        for(Book book : domainmodels)
        {
            addAmountOfCopies(book);
        }
        return domainmodels;
    }


    private int addAmountOfCopies(Book book)
    {
        TypedQuery<BookCopyJpaEntity> queue = em.createQuery("FROM BookCopyJpaEntity bc WHERE bc.book = :book AND bc.isRetired = false", BookCopyJpaEntity.class);
        queue.setParameter("book", this.mapper.bookToEntity(book));
        int i = queue.getResultList().size();
        book.setCopyAmount(i);
        book.setAvailableAmount(1); //TODO
        return i;
    }
}