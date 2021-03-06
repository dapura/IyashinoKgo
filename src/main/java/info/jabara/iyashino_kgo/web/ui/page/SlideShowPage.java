/**
 *
 */
package info.jabara.iyashino_kgo.web.ui.page;

import info.jabara.iyashino_kgo.Environment;
import info.jabara.iyashino_kgo.web.ui.WicketApplication;
import info.jabara.iyashino_kgo.web.ui.WicketApplication.Resource;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.ComponentJavaScriptHeaderItem;
import jabara.wicket.Models;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * @author jabaraster
 */
public class SlideShowPage extends WebPageBase {
    private static final long serialVersionUID = -2332621337544880277L;

    private Image             logo;

    /**
     *
     */
    public SlideShowPage() {
        this.add(getLogo());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addStyleSheetToHead(pResponse);
        addJavaScriptToHead(pResponse);
        pResponse.render(JavaScriptHeaderItem.forScript(SCRIPT_ANALYTICS, null));
    }

    /**
     * @see info.jabara.iyashino_kgo.web.ui.page.WebPageBase#getTitleLabel()
     */
    @Override
    protected Label getTitleLabel() {
        if (this.titleLabel == null) {
            this.titleLabel = new Label(getTitleLabelId(), getTitleLabelModel());
        }
        return this.titleLabel;
    }

    /**
     * @see info.jabara.iyashino_kgo.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly(Environment.getApplicationNameInJapanese());
    }

    private Image getLogo() {
        if (this.logo == null) {
            this.logo = new Image("logo", ((WicketApplication) getApplication()).getSharedResourceReference(Resource.FAVICON)); //$NON-NLS-1$
        }
        return this.logo;
    }

    private static void addJavaScriptToHead(final IHeaderResponse pResponse) {
        @SuppressWarnings("nls")
        final String[] jss = { "jquery-1.11.1.min.js" //
            , "jquery.cycle.all.min.js" //
            , "jquery.maximage.min.js" //
            , "jsrender.min.js" //
            , "bootstrap/js/bootstrap.min.js" //
            , "soundcloud_sdk.js" //
            , "jquery.activity-indicator-1.0.0.min.js" //
        };
        for (final String js : jss) {
            pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(SlideShowPage.class, js)));
        }
        pResponse.render(ComponentJavaScriptHeaderItem.forType(SlideShowPage.class));
    }

    private static void addStyleSheetToHead(final IHeaderResponse pResponse) {
        @SuppressWarnings("nls")
        final String[] csss = { "cssreset-min.css" //
            , "jquery.maximage.min.css" //
            , "bootstrap/css/bootstrap.min.css" //
        };
        for (final String css : csss) {
            pResponse.render(CssHeaderItem.forReference(new CssResourceReference(SlideShowPage.class, css)));
        }
        pResponse.render(ComponentCssHeaderItem.forType(SlideShowPage.class));
    }
}
