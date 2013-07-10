package glasspane;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author Andres Toussaint
 */
public class SvgGlassPaneExample {

	protected JSVGCanvas	canvas;
	protected JLabel		target;

	/** Creates a new instance of SvgGlassPaneExample */
	public SvgGlassPaneExample(JPanel panel) {
		panel.removeAll();
		canvas = new JSVGCanvas();
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		canvas.setSize(400, 400);
		canvas.setDocument(buildDocument());
		panel.add(canvas);
		panel.repaint();
	}

	protected Document buildDocument() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// get the root element (the svg element)
		Element svgRoot = doc.getDocumentElement();

		// set the width and height attribute on the root svg element
		svgRoot.setAttributeNS(null, "width", "400");
		svgRoot.setAttributeNS(null, "height", "400");

		// create the array of rectangles
		Element g = doc.createElementNS(svgNS, "g");
		g.setAttributeNS(null, "id", "rectangles");

		for (int x = 10; x < 400; x += 30) {
			for (int y = 10; y < 400; y += 30) {
				Element rectangle = doc.createElementNS(svgNS, "rect");
				rectangle.setAttributeNS(null, "x", "" + x);
				rectangle.setAttributeNS(null, "y", "" + y);
				rectangle.setAttributeNS(null, "width", "20");
				rectangle.setAttributeNS(null, "height", "20");
				rectangle.setAttributeNS(null, "style", "fill:red");
				rectangle.setAttributeNS(null, "id", "rect: " + x + ", " + y);
				// attach the rectangle to the g element
				g.appendChild(rectangle);
			}
		}

		svgRoot.appendChild(g);

		return doc;
	}

	public void addGlassPane() {
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = canvas.getSVGDocument();
		Element rectangle = doc.createElementNS(svgNS, "rect");
		rectangle.setAttributeNS(null, "x", "0");
		rectangle.setAttributeNS(null, "y", "0");
		rectangle.setAttributeNS(null, "width", "400");
		rectangle.setAttributeNS(null, "height", "400");
		rectangle.setAttributeNS(null, "style", "fill:none;pointer-events:fill");
		rectangle.setAttributeNS(null, "id", "glasspane");
		Element svgRoot = doc.getDocumentElement();

		svgRoot.insertBefore(rectangle, doc.getElementById("rectangles"));

	}

	public void registerListeners(JLabel target) {
		//this label provides feedback on the selected item
		this.target = target;
		// Gets an element from the loaded document.
		// document is your SVGDocument
		Element elt = canvas.getSVGDocument().getElementById("fr");
		EventTarget t = (EventTarget) elt;
		t.addEventListener("click", new GlassPaneClick(), false);

		Element elt2 = canvas.getSVGDocument().getElementById("es");
		EventTarget t2 = (EventTarget) elt2;

		t2.addEventListener("click", new ObjectClick(), false);
	}

	public class GlassPaneClick implements EventListener {

		@Override
		public void handleEvent(Event evt) {
			target.setText("Glasspane event " + ((Element) evt.getTarget()).getAttribute("id"));
			target.repaint();
		}
	}

	public class ObjectClick implements EventListener {

		@Override
		public void handleEvent(Event evt) {
			target.setText("Rectangles event " + ((Element) evt.getTarget()).getAttribute("id"));
			target.repaint();
		}
	}
}