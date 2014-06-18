package info.dapura.iyashino_kgo.web.ui;

import info.dapura.iyashino_kgo.model.FailAuthentication;
import info.dapura.iyashino_kgo.model.LoginUser;
import info.dapura.iyashino_kgo.service.IAuthenticationService;
import info.dapura.iyashino_kgo.web.LoginUserHolder;

import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * 
 */
public class AppSession extends WebSession {
    private static final long                serialVersionUID = -993562397311694898L;

    private final AtomicReference<LoginUser> authenticated    = new AtomicReference<>();

    /**
     * @param pRequest -
     */
    public AppSession(final Request pRequest) {
        super(pRequest);
    }

    /**
     * @return 管理者ユーザとしてログイン済みならtrue.
     */
    public boolean currentUserIsAdministrator() {
        if (!isAuthenticatedCore()) {
            return false;
        }
        return this.authenticated.get().isAdministrator();
    }

    /**
     * @return -
     * @throws IllegalStateException 未ログインの場合.
     */
    public LoginUser getLoginUser() throws IllegalStateException {
        final LoginUser ret = this.authenticated.get();
        if (ret == null) {
            throw new IllegalStateException();
        }
        return ret;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebSession#invalidate()
     */
    @Override
    public void invalidate() {
        super.invalidate();
        invalidateHttpSession();
    }

    /**
     * @see org.apache.wicket.Session#invalidateNow()
     */
    @Override
    public void invalidateNow() {
        super.invalidateNow();
        invalidateHttpSession();
    }

    /**
     * @return 認証済みあればtrue.
     */
    public boolean isAuthenticated() {
        return isAuthenticatedCore();
    }

    /**
     * @param pUserId -
     * @param pPassword -
     * @throws FailAuthentication 認証NGの場合にスローして下さい.
     */
    public void login(final String pUserId, final String pPassword) throws FailAuthentication {
        final LoginUser loginUser = getAuthenticationService().login(pUserId, pPassword);

        // セッション固定攻撃への対処.
        ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession().invalidate();

        this.authenticated.set(loginUser);

        final HttpSession session = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession();
        LoginUserHolder.set(session, loginUser);
    }

    private boolean isAuthenticatedCore() {
        return this.authenticated.get() != null;
    }

    /**
     * @return 現在のコンテキスト中の{@link AppSession}.
     */
    public static AppSession get() {
        return (AppSession) Session.get();
    }

    private static IAuthenticationService getAuthenticationService() {
        return WicketApplication.get().getInjector().getInstance(IAuthenticationService.class);
    }

    private static void invalidateHttpSession() {
        // Memcahcedによるセッション管理を行なっていると、Session#invalidate()だけではセッションが完全には破棄されない.
        final HttpSession session = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession();
        session.invalidate();
    }
}
