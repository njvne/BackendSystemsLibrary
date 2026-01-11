package application.domain;

import application.domain.Authorisation.AuthorizationResult;
import application.port.in.FindAuthorisationUseCase;
import application.port.out.FindAuthorisationPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService implements FindAuthorisationUseCase
{
    @Inject
    FindAuthorisationPort findAuthorisationPort;


    @Override
    public AuthorizationResult checkAuthorisation(long uid, String password)
    {
        return this.findAuthorisationPort.checkPasswordCombination(uid, password);
    }
}
