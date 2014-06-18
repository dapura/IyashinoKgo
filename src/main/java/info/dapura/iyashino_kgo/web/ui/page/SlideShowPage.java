/**
 * 
 */
package info.dapura.iyashino_kgo.web.ui.page;

import info.dapura.iyashino_kgo.Environment;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.ComponentJavaScriptHeaderItem;
import jabara.wicket.Models;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

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
    }

    /**
     * @see info.dapura.iyashino_kgo.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly(Environment.getApplicationName());
    }

    private Image getLogo() {
        if (this.logo == null) {
            this.logo = new Image("logo", new PackageResourceReference(SlideShowPage.class, "logo.png")); //$NON-NLS-1$//$NON-NLS-2$
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
