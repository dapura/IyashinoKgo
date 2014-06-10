package info.dapura.iyashino_kgo.web.ui.page;

import info.dapura.iyashino_kgo.Environment;
import info.dapura.iyashino_kgo.web.ui.component.BodyCssHeaderItem;
import jabara.wicket.ComponentCssHeaderItem;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 */
public abstract class RestrictedPageBase extends WebPageBase {
    private static final long serialVersionUID = 8581002405664307678L;

    private Label             applicationName;
    private Link<?>           goTop;
    private Link<?>           goLogout;

    /**
     * 
     */
    protected RestrictedPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters -
     */
    protected RestrictedPageBase(final PageParameters pParameters) {
        super(pParameters);
        this.add(getGoTop());
        this.add(getGoLogout());
    }

    /**
     * @see info.dapura.iyashino_kgo.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(BodyCssHeaderItem.get());
        pResponse.render(ComponentCssHeaderItem.forType(RestrictedPageBase.class));
    }

    /**
     * このメソッドはサブクラスでコンポーネントIDの重複を避けるためにprotectedにしています. <br>
     * 
     * @return -
     */
    protected Link<?> getGoLogout() {
        if (this.goLogout == null) {
            this.goLogout = new BookmarkablePageLink<>("goLogout", LogoutPage.class); //$NON-NLS-1$
        }
        return this.goLogout;
    }

    private Label getApplicationName() {
        if (this.applicationName == null) {
            this.applicationName = new Label("applicationName", Environment.getApplicationName()); //$NON-NLS-1$
        }
        return this.applicationName;
    }

    private Link<?> getGoTop() {
        if (this.goTop == null) {
            this.goTop = new BookmarkablePageLink<>("goTop", Application.get().getHomePage()); //$NON-NLS-1$
            this.goTop.add(getApplicationName());
        }
        return this.goTop;
    }
}
