/**
 * 
 */
package info.jabara.iyashino_kgo.service;

import info.jabara.iyashino_kgo.model.FailAuthentication;
import info.jabara.iyashino_kgo.model.LoginUser;
import info.jabara.iyashino_kgo.service.impl.AuthenticationServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(AuthenticationServiceImpl.class)
public interface IAuthenticationService {

    /**
     * @param pUserId
     * @param pPassword
     * @return -
     * @throws FailAuthentication
     */
    LoginUser login(String pUserId, String pPassword) throws FailAuthentication;
}
