package foo;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Main {

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(8080);
		server.addConnector(connector);

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });

		resource_handler.setResourceBase("./src/main/resources"); //
		

		ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/templates", true, false);
		servletContextHandler.addServlet(RedirectToDefaultTemplateServlet.class, "/");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, servletContextHandler, new DefaultHandler() });
		server.setHandler(handlers);

		server.start();
		server.join();
	}
}