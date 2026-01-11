package application.domain;

import application.domain.Authorisation.AuthorizationResult;
import application.domain.models.Borrow;
import application.domain.models.User;
import application.domain.results.*;
import application.port.in.FindAuthorisationUseCase;
import application.port.in.user.*;
import application.port.out.FindAuthorisationPort;
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
    ReturnBookUseCase returnBookPort;



    @Override
    public NoContentResult create(Borrow borrow)
    {
        return this.persistBorrowPort.createBorrow(borrow);
    }

    @Override
    public NoContentResult create(User user, String password)
    {
        return this.persistUserPort.createUser(user, password);
    }

    @Override
    public BorrowsResult loadAllBorrows(long uid)
    {
        return this.readAllBorrowsPort.readAllBorrows(uid);
    }

    @Override
    public BorrowResult loadBorrowByNumber(long borrowNumber)
    {
        return this.readBorrowByNumberPort.readBorrowByNumber(borrowNumber);
    }

    @Override
    public UserResult loadUserById(long id)
    {
        return this.readUserByIdPort.readUserById(id);
    }

    @Override
    public NoContentResult returnBook(long uid, long borrowNumber)
    {
        final var result = this.readBorrowByNumberPort.readBorrowByNumber(borrowNumber);
        final var returnValue = new NoContentResult();

        if( result.isEmpty() )
        {
            returnValue.setError(ErrorCodes.RESOURCE_TO_UPDATE_NOT_FOUND, "path id: " + uid);
        }
        else if(result.getResult().getUserid().getId() != uid)
        {
            returnValue.setError(ErrorCodes.RESOURCE_ID_DOES_NOT_MATCH, "path id: " + uid + " , resource id: " + borrowNumber);
        }
        else if(result.hasError())
        {
            returnValue.setError(result.getErrorCode(), result.getErrorMessage());
        }
        else
        {
            this.returnBookPort.returnBook(uid, borrowNumber);
        }
        return returnValue;
    }
}