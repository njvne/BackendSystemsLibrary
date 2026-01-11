package application.port.in;

import application.domain.Authorisation.AuthorizationResult;

public interface FindAuthorisationUseCase
{
    AuthorizationResult checkAuthorisation(long uid, String password);
}