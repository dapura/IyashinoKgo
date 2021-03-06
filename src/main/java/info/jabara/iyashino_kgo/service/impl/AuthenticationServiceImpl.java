/**
 * 
 */
package info.jabara.iyashino_kgo.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import info.jabara.iyashino_kgo.entity.ELoginPassword;
import info.jabara.iyashino_kgo.entity.ELoginPassword_;
import info.jabara.iyashino_kgo.entity.EUser_;
import info.jabara.iyashino_kgo.model.FailAuthentication;
import info.jabara.iyashino_kgo.model.LoginUser;
import info.jabara.iyashino_kgo.service.IAuthenticationService;

/**
 * @author jabaraster
 */
public class AuthenticationServiceImpl extends JpaDaoBase implements IAuthenticationService {

    /**
     * @param pEntityManagerFactory -
     */
    @Inject
    public AuthenticationServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see info.jabara.iyashino_kgo.service.IAuthenticationService#login(java.lang.String, java.lang.String)
     */
    @Override
    public LoginUser login(final String pUserId, final String pPassword) throws FailAuthentication {
        ArgUtil.checkNullOrEmpty(pUserId, "pUserId"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<ELoginPassword> query = builder.createQuery(ELoginPassword.class);
        final Root<ELoginPassword> root = query.from(ELoginPassword.class);

        query.distinct(true);
        root.fetch(ELoginPassword_.user);

        query.where( //
        builder.equal(root.get(ELoginPassword_.user).get(EUser_.userId), pUserId) //
        );

        try {
            final ELoginPassword member = getSingleResult(em.createQuery(query));
            if (!member.equal(pPassword)) {
                throw FailAuthentication.INSTANCE;
            }

            return new LoginUser(member.getUser());

        } catch (final NotFound e) {
            throw FailAuthentication.INSTANCE;
        }
    }
}
