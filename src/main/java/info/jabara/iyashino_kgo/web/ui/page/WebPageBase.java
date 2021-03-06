package info.jabara.iyashino_kgo.web.ui.page;

import info.jabara.iyashino_kgo.Environment;
import info.jabara.iyashino_kgo.web.ui.WicketApplication;
import info.jabara.iyashino_kgo.web.ui.WicketApplication.Resource;
import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.IoUtil;
import jabara.wicket.IconHeaderItem;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.Models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.io.IOUtils;

/**
 *
 */
public abstract class WebPageBase extends WebPage {
    private static final long                        serialVersionUID  = 7118284432588712186L;

    private static final CssResourceReference        REF_BOOTSTRAP_CSS = new CssResourceReference(WebPageBase.class, "bootstrap/css/bootstrap.css"); //$NON-NLS-1$
    private static final CssResourceReference        REF_APP_CSS       = new CssResourceReference(WebPageBase.class, "App.css");                    //$NON-NLS-1$
    private static final JavaScriptResourceReference REF_BOOTSTRAP_JS  = new JavaScriptResourceReference(WebPageBase.class,
            "bootstrap/js/bootstrap.js");                                        //$NON-NLS-1$
    /**
     *
     */
    protected static final CharSequence              SCRIPT_ANALYTICS  = loadAnalyticsScript();

    /**
     *
     */
    protected Label                                  titleLabel;

    /**
     *
     */
    protected WebPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters -
     */
    protected WebPageBase(final PageParameters pParameters) {
        super(pParameters);
        this.add(getTitleLabel());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        renderCommonHead(pResponse);
    }

    /**
     * titleタグの中を表示するラベルです. <br>
     * このメソッドはサブクラスでコンポーネントIDの重複を避けるためにprotectedにしています. <br>
     *
     * @return titleタグの中を表示するラベル.
     * @see #getTitleLabelModel()
     * @see #getTitleLabelId()
     */
    @SuppressWarnings({ "nls" })
    protected Label getTitleLabel() {
        if (this.titleLabel == null) {
            this.titleLabel = new Label(getTitleLabelId(), Models.of(getTitleLabelModel().getObject() + " - " + Environment.getApplicationName()));
        }
        return this.titleLabel;
    }

    /**
     * @return -
     * @see #getTitleLabel()
     */
    @SuppressWarnings("static-method")
    protected final String getTitleLabelId() {
        return "titleLabel"; //$NON-NLS-1$
    }

    /**
     * @return HTMLのtitleタグの内容
     */
    protected abstract IModel<String> getTitleLabelModel();

    /**
     * @param pResponse 全ての画面に共通して必要なheadタグ内容を出力します.
     */
    public static void renderCommonHead(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$

        pResponse.render(IconHeaderItem.forReference(WicketApplication.get().getSharedResourceReference(Resource.FAVICON)));

        pResponse.render(CssHeaderItem.forReference(REF_BOOTSTRAP_CSS));
        pResponse.render(CssHeaderItem.forReference(REF_APP_CSS));

        pResponse.render(JavaScriptHeaderItem.forReference(JavaScriptUtil.JQUERY_1_9_1_REFERENCE));
        pResponse.render(JavaScriptHeaderItem.forReference(REF_BOOTSTRAP_JS));
    }

    private static CharSequence loadAnalyticsScript() {
        try (InputStream in = IoUtil.toBuffered(WebPageBase.class.getResourceAsStream("google-analytics.js"))) { //$NON-NLS-1$
            final ByteArrayOutputStream mem = new ByteArrayOutputStream();
            IOUtils.copy(in, mem);
            return new String(mem.toByteArray(), Charset.forName("UTF-8")); //$NON-NLS-1$

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }
}
