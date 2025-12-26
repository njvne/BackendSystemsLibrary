package application.domain;

import application.domain.models.Borrow;
import application.domain.models.User;
import application.domain.results.BorrowResult;
import application.domain.results.BorrowsResult;
import application.domain.results.NoContentResult;
import application.domain.results.UserResult;
import application.port.in.user.*;
import application.port.out.user.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService implements CreateBorrowUseCase, CreateUserUseCase, LoadAllBorrowsUseCase, LoadUserBorrowByNumberUseCase, LoadUserByIdUseCase, ReturnBookUseCase
{
    @Inject
    PersistBorrowPort persistBorrowPort;

    @Inject
    PersistUserPort persistUserPort;

    @Inject
    ReadAllBorrowsPort readAllBorrowsPort;

    @Inject
    ReadBorrowByNumberPort readBorrowByNumberPort;

    @Inject
    ReadUserByIdPort readUserByIdPort;

    @Inject
    ReturnBookUseCase returnBookUseCase;



    @Override
    public NoContentResult create(Borrow borrow)
    {
        return this.persistBorrowPort.createBorrow(borrow);
    }

    @Override
    public NoContentResult create(User user)
    {
        return this.persistUserPort.createUser(user);
    }

    @Override
    public BorrowsResult loadAllBorrows(long uid)
    {
        return this.readAllBorrowsPort.readAllBorrows(uid);
    }

    @Override
    public BorrowResult loadBorrowByNumber(long uid, long borrowNumber)
    {
        return this.readBorrowByNumberPort.readBorrowByNumber(uid, borrowNumber);
    }

    @Override
    public UserResult loadUserById(long id)
    {
        return this.readUserByIdPort.readUserById(id);
    }

    @Override
    public NoContentResult returnBook(long uid, long borrowNumber)
    {
        return this.returnBookUseCase.returnBook(uid, borrowNumber);
    }
}