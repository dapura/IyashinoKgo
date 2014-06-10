/**
 * 
 */
package info.dapura.iyashino_kgo.web.ui.page;

import jabara.wicket.Models;

import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class SlideShowPage extends WebPageBase {
    private static final long serialVersionUID = -2332621337544880277L;

    /**
     * @see info.dapura.iyashino_kgo.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("スライドショー"); //$NON-NLS-1$
    }

}
