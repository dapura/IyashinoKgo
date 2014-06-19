/**
 * 
 */
package info.jabara.iyashino_kgo.web;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jabaraster
 */
public class HeartBeat extends HttpServlet {
    private static final long serialVersionUID = -7188181192254764001L;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(@SuppressWarnings("unused") final HttpServletRequest pReq, final HttpServletResponse pResp) throws IOException {
        pResp.setContentType("text/plain"); //$NON-NLS-1$
        pResp.getWriter().append("OK"); //$NON-NLS-1$
    }
}
