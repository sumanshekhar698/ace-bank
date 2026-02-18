package dev.codecounty.acebank.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Servlet Filter implementation class ChangePasswordFilter
 */
@Log
public class ChangePasswordFilter extends HttpFilter implements Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpFilter#HttpFilter()
	 */
	public ChangePasswordFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String newPassword = request.getParameter("newPassword");
		String confirmNewPassword = request.getParameter("confirmNewPassword");

		if (!StringUtils.isEmpty(newPassword) && !StringUtils.isEmpty(confirmNewPassword)
				&& newPassword.equals(confirmNewPassword)) {
			// pass the request along the filter chain
			log.info("Filteration forwarded");
			chain.doFilter(request, response);
		} else {

			log.info("Filteration intercepted");

			((HttpServletResponse) response).sendRedirect("ChangePasswordMismatch.html");
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
