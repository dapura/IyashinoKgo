package info.dapura.iyashino_kgo.web.ui.page;

import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.ComponentJavaScriptHeaderItem;
import jabara.wicket.Models;

import java.io.Serializable;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 */
@SuppressWarnings("synthetic-access")
public class TopPage extends WebPageBase {
    private static final long serialVersionUID = 4767679453276681767L;

    private final Handler     handler          = new Handler();

    /**
     * @see info.dapura.iyashino_kgo.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(ComponentCssHeaderItem.forType(TopPage.class));
        pResponse.render(ComponentJavaScriptHeaderItem.forType(TopPage.class));

        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(TopPage.class, "jsrender.min.js"))); //$NON-NLS-1$
    }

    /**
     * @see info.dapura.iyashino_kgo.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("Top"); //$NON-NLS-1$
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = -1058979848537389629L;

    }
}
