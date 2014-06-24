package info.jabara.iyashino_kgo.web.ui;

import info.jabara.iyashino_kgo.web.ui.page.SlideShowPage;
import jabara.general.ArgUtil;
import jabara.wicket.MarkupIdForceOutputer;

import org.apache.wicket.Page;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.IProvider;
import org.apache.wicket.util.time.Duration;

import com.google.inject.Injector;

/**
 *
 */
public class WicketApplication extends WebApplication {

    private static final String       ENC = "UTF-8";   //$NON-NLS-1$

    private final IProvider<Injector> injectorProvider;

    /**
     * @param pInjectorProvider Guiceの{@link Injector}を供給するオブジェクト. DI設定に使用します.
     */
    public WicketApplication(final IProvider<Injector> pInjectorProvider) {
        ArgUtil.checkNull(pInjectorProvider, "pInjectorProvider"); //$NON-NLS-1$
        this.injectorProvider = pInjectorProvider;
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return SlideShowPage.class;
    }

    /**
     * @return -
     */
    public Injector getInjector() {
        return this.injectorProvider.get();
    }

    /**
     * @param pResource -
     * @return -
     */
    @SuppressWarnings("static-method")
    public ResourceReference getSharedResourceReference(final Resource pResource) {
        ArgUtil.checkNull(pResource, "pResource"); //$NON-NLS-1$
        return new SharedResourceReference(pResource.getName());
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        mountResources();
        initializeEncoding();
        initializeInjection();
        initializeOther();
    }

    private void initializeEncoding() {
        getMarkupSettings().setDefaultMarkupEncoding(ENC);
        getRequestCycleSettings().setResponseRequestEncoding(getMarkupSettings().getDefaultMarkupEncoding());
    }

    private void initializeInjection() {
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this, this.injectorProvider.get()));
    }

    private void initializeOther() {
        getComponentInstantiationListeners().add(new MarkupIdForceOutputer());
    }

    private void mountResource(final Resource pResource, final String pFilePath, final Duration pCacheDuration) {
        mountResource(pResource.getName(), new ResourceReference(pResource.getName()) {
            private static final long serialVersionUID = -8982729375012083247L;

            @SuppressWarnings("resource")
            @Override
            public IResource getResource() {
                return new ResourceStreamResource(new UrlResourceStream(WicketApplication.class.getResource(pFilePath))) //
                        .setCacheDuration(pCacheDuration) //
                ;
            }
        });
    }

    @SuppressWarnings({ "nls" })
    private void mountResources() {
        mountResource(Resource.BACK, "brickwall.png", Duration.days(10));
        mountResource(Resource.FAVICON, "logo.png", Duration.days(10));
    }

    /**
     * @return -
     */
    public static WicketApplication get() {
        return (WicketApplication) WebApplication.get();
    }

    /**
     * @author jabaraster
     */
    public enum Resource {
        /**
         * 
         */
        BACK("back"), //$NON-NLS-1$

        /**
         * 
         */
        FAVICON("favicon"), //$NON-NLS-1$

        ;

        private final String name;

        Resource(final String pName) {
            this.name = pName;
        }

        /**
         * @return -
         */
        public String getName() {
            return this.name;
        }
    }
}
