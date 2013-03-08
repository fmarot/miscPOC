package foo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * This servlet is used when the requested template was not found. It redirects
 * to the default one instead The default one should exist, have the same name
 * (ie analysis.html) and be in the DEFAULT_TEMPLATE_DIR_NAME directory
 */
@Slf4j
public class RedirectToDefaultTemplateServlet extends HttpServlet {

	private static final long serialVersionUID = 8080801499698509786L;
	private static final String DEFAULT_TEMPLATE_DIR_NAME = "defaultTemplates";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String servletPath = req.getServletPath();
		log.info("servletPath=" + servletPath);

		if (servletPath.contains(DEFAULT_TEMPLATE_DIR_NAME)) {
			log.warn("Already looking for default template but seems it is not found. Stopping recursion...");
			// dirty hack to prevent endless recursion in case even the default
			// template is not found
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		List<String> pathsElements = getPathElements(servletPath);
		String redirectedTemplateLocation = req.getContextPath() + changePathToDefaultTemplatesDirectory(pathsElements);
		log.info("redirectedTemplateLocation=" + redirectedTemplateLocation);
		resp.sendRedirect(redirectedTemplateLocation);
	}

	/** Specific template is not found so return the default one's path instead */
	private String changePathToDefaultTemplatesDirectory(List<String> pathsElements) {
		int indexToReplace = pathsElements.size() - 2; // TODO: warning not safe
														// !
		pathsElements.set(indexToReplace, DEFAULT_TEMPLATE_DIR_NAME);
		String finalDirectory = "";
		for (String currElement : pathsElements) {
			finalDirectory += ("/" + currElement);
		}
		return finalDirectory;
	}

	private List<String> getPathElements(String servletPath) {
		StringTokenizer tokenizer = new StringTokenizer(servletPath, "/", false);
		List<String> elements = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			String s = tokenizer.nextToken();
			elements.add(s);
		}
		return elements;
	}
}