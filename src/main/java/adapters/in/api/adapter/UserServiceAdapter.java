package adapters.in.api.adapter;

import adapters.in.api.Exceptions.ResourceConflictException;
import adapters.in.api.models.UserDTO;
import application.domain.Authorisation.AuthorizationResult;
import application.domain.models.BookISBN;
import application.domain.models.Borrow;
import application.domain.models.UserID;
import application.domain.results.ErrorCodes;
import application.port.in.FindAuthorisationUseCase;
import application.port.in.user.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;


@ApplicationScoped
public class UserServiceAdapter
{
    @Inject
    FindAuthorisationUseCase findAuthorisationUseCase;

    @Inject
    CreateBorrowUseCase createBorrowUseCase;

    @Inject
    CreateUserUseCase createUserUseCase;

    @Inject
    LoadAllBorrowsUseCase loadAllBorrowsUseCase;

    @Inject
    LoadUserBorrowByNumberUseCase loadUserBorrowByIdUseCase;

    @Inject
    LoadUserByIdUseCase loadUserByIdUseCase;

    @Inject
    ReturnBookUseCase returnBookUseCase;

    private Mapper mapper = new Mapper();


    public BorrowResult createBorrow(long uid, long isbn)
    {
        final var domainModel = new Borrow(new BookISBN(isbn), new UserID(uid), false);
        final var domainResult = this.createBorrowUseCase.create(domainModel);
        if(domainResult.hasError())
        {
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_UNAVAILABLE)
            {
                throw new ResourceConflictException(domainResult.getErrorMessage());
            }
            else
            {
                throw new InternalServerErrorException(domainResult.getErrorMessage());
            }
        }
        else
        {
            return new BorrowResult(this.mapper.borrowToApiModel(domainModel));
        }
    }

    public UserResult createUser(UserDTO userDTO, String password)
    {
        final var domainModel = this.mapper.userToDomainModel(userDTO);
        final var domainResult = this.createUserUseCase.create(domainModel, password);
        if(domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
        else
        {
            return new UserResult(this.mapper.userToApiModel(domainModel));
        }
    }

    public BorrowsResult getAllBorrows(long uid)
    {
        final var domainResult = this.loadAllBorrowsUseCase.loadAllBorrows(uid);
        if (domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
        else
        {
            return new BorrowsResult(this.mapper.borrowsToApiModels(domainResult.getResult()));
        }
    }

    public BorrowResult getBorrowByNumber(long uid, long number)
    {
        final var domainResult = this.loadUserBorrowByIdUseCase.loadBorrowByNumber(number);

        if(domainResult.isEmpty())
        {
            throw new NotFoundException();
        }
        if (domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }

        return new BorrowResult(this.mapper.borrowToApiModel(domainResult.getResult()));
    }

    public UserResult getUserById(long id)
    {
        final var domainResult = this.loadUserByIdUseCase.loadUserById(id);

        if(domainResult.isEmpty())
        {
            throw new NotFoundException();
        }
        if(domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }

        return new UserResult( this.mapper.userToApiModel(domainResult.getResult()));
    }

    public void returnBook(long uid, long borrowNum)
    {
        final var domainResult = this.returnBookUseCase.returnBook(uid, borrowNum);
        if(domainResult.hasError())
        {
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_TO_UPDATE_NOT_FOUND)
            {
                throw new NotFoundException(domainResult.getErrorMessage());
            }
            else
            {
                throw new InternalServerErrorException(domainResult.getErrorMessage());
            }
        }
    }


    public AuthorizationResult checkAuth(long uid, String password)
    {
        return this.findAuthorisationUseCase.checkAuthorisation(uid, password);
    }
}