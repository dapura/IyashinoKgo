package info.jabara.iyashino_kgo.web.ui.page;

import info.jabara.iyashino_kgo.web.ui.AppSession;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;

/**
 * 
 */
public class LogoutPage extends WebPage {
    private static final long serialVersionUID = -1289687837969853166L;

    /**
     * 
     */
    public LogoutPage() {
        AppSession.get().invalidateNow();
        throw new RestartResponseException(LoginPage.class);
    }
}
