package com.teamtter.svg;

import java.awt.Component;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.traversal.TraversalSupport;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/** WARNING: this class modifies independent attributes of the XML nodes to indicate the selection
 * One alternative solution would be to modify directly the "style" attribute where we could change
 * many values (color, stroke-width...) at one time */
@Slf4j
public class ImageWithClickableParts implements EventListener {

	private static final String						DEFAULT_FILL_PROPERTY_VALUE		= "white";
	private static final String						DEFAULT_STYLE_PROPERTY_VALUE	= "";
	private static final String						STYLE_PROPERTY					= "style";
	private static final String						FILL_PROPERTY					= "fill";
	private static final String						FILL_PROPERTY_HIGHLIGHTED		= "rgb(255,50,50)";
	private static final String						SENSITIVE_ZONE_IDENTIFIER		= "id";

	private JSVGCanvas								svgCanvas							= new JSVGCanvas();

	/** Keep the original properties of modified nodes */
	private Map<ObjectAndProperty<Element>, String>	nodeAndProperty2Value				= new HashMap<ObjectAndProperty<Element>, String>();

	private List<SelectedPartListener>				listeners							= new ArrayList<SelectedPartListener>();

	public ImageWithClickableParts(File svgFile) {
		createComponents(svgFile);
	}

	public Component getAsComponent() {
		return svgCanvas;
	}

	public void createComponents(File svgFile) {
		try {
			svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
			svgCanvas.setURI(svgFile.toURL().toString());
			svgCanvas.enableInputMethods(true);

			svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {

				@Override
				public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
				}

				@Override
				public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
					addListenersToSensitiveZones();
				}
			});
		} catch (MalformedURLException e) {
			log.error("Error loading svg file", e);
		}

	}

	private void toggleHighlight(Element sensitiveZone) {
		cleanAttributes(sensitiveZone);
		ObjectAndProperty<Element> objAndPropertyFill = new ObjectAndProperty<Element>(sensitiveZone, FILL_PROPERTY);
		String savedFillPropertyValue = nodeAndProperty2Value.get(objAndPropertyFill);
		NamedNodeMap zoneAttributes = sensitiveZone.getAttributes();
		Node fillProperty = zoneAttributes.getNamedItem(FILL_PROPERTY);

		if (savedFillPropertyValue == null) {
			backupOriginalPropertyAndApplyNewProperty(fillProperty, objAndPropertyFill, FILL_PROPERTY_HIGHLIGHTED);
		} else {
			restoreOriginalProperty(fillProperty, objAndPropertyFill, savedFillPropertyValue);
		}

		String zoneKey = zoneAttributes.getNamedItem(SENSITIVE_ZONE_IDENTIFIER).getNodeValue();
		notifyListeners(zoneKey, true);
	}

	private void restoreOriginalProperty(Node namedItem, ObjectAndProperty<Element> objAndProperty, String savedPropertyValue) {
		namedItem.setNodeValue(savedPropertyValue);
		nodeAndProperty2Value.remove(objAndProperty);
	}

	private void backupOriginalPropertyAndApplyNewProperty(Node namedItem, ObjectAndProperty<Element> objAndProperty, String highlightedValue) {
		String targetOriginalPropertyValue = namedItem.getNodeValue();
		nodeAndProperty2Value.put(objAndProperty, targetOriginalPropertyValue);
		namedItem.setNodeValue(highlightedValue);
	}

	/** clean the attributes of this Element so that we can override some and set some values.
	 * Unexisting attributes but mandatory for us are created, problematic ('style' !) ones are deleted */
	protected void cleanAttributes(Element sensitiveZone) {
		NamedNodeMap zoneAttributes = sensitiveZone.getAttributes();

		// remove the style so we are able to override it using full blown attributes instead of concatenated stuffs in the 'style' attribute
		if (zoneAttributes.getNamedItem(STYLE_PROPERTY) != null) {
			log.warn("Setting default {}", STYLE_PROPERTY);
			sensitiveZone.setAttribute(STYLE_PROPERTY, DEFAULT_STYLE_PROPERTY_VALUE);
		}
		// If no FILL_PROPERTY defined, initialize it to a color by default
		if (zoneAttributes.getNamedItem(FILL_PROPERTY) == null) {
			log.warn("Setting default {}", FILL_PROPERTY);
			sensitiveZone.setAttribute(FILL_PROPERTY, DEFAULT_FILL_PROPERTY_VALUE);
		}
	}

	protected void addListenersToSensitiveZones() {
		List<Node> allSensitiveZones = getAllSensitiveZones();
		for (Node sensitiveZone : allSensitiveZones) {
			Element elt = (Element) sensitiveZone;
			EventTarget t = (EventTarget) elt;
			t.addEventListener("click", this, false);
		}

	}

	private List<Node> getAllSensitiveZones() {
		List<Node> oleaNodes = new ArrayList<Node>();
		SVGDocument svgDocument = svgCanvas.getSVGDocument();
		NodeFilter nodeFilter = createSensitiveZonesNodeFilter();
		TreeWalker treeWalker = TraversalSupport.createTreeWalker((AbstractDocument) svgDocument, svgDocument.getRootElement(), NodeFilter.SHOW_ALL, nodeFilter, true);
		Node currNode = treeWalker.nextNode();
		while (currNode != null) {
			oleaNodes.add(currNode);
			currNode = treeWalker.nextNode();
		}
		return oleaNodes;
	}

	/** @return a node filter able to identify nodes containing sensitive zones */
	private NodeFilter createSensitiveZonesNodeFilter() {
		return new NodeFilter() {

			@Override
			public short acceptNode(Node node) {
				NamedNodeMap attributes = node.getAttributes();
				if (attributesContainSpecificSensitgiveZonesAttribute(attributes)) {
					return FILTER_ACCEPT;
				} else {
					return FILTER_SKIP;
				}
			}

			protected boolean attributesContainSpecificSensitgiveZonesAttribute(NamedNodeMap attributes) {
				return attributes != null
						&& attributes.getNamedItem(SENSITIVE_ZONE_IDENTIFIER) != null;
			}
		};
	}

	@Override
	public void handleEvent(Event evt) {
		Element sensitiveZone = (Element) evt.getTarget();
		log.info("Click on " + sensitiveZone.getAttribute("id"));
		toggleHighlight(sensitiveZone);
	}

	/** @return an object describing the current state of the selected parts */
	private SelectedPartsState createCurrentSelectedPartsState() {
		Set<String> selectedKeys = new HashSet<String>();
		for (ObjectAndProperty<Element> objAndProp : nodeAndProperty2Value.keySet()) {
			Element element = objAndProp.getObject();
			String key = element.getAttributes().getNamedItem(SENSITIVE_ZONE_IDENTIFIER).getNodeValue();
			selectedKeys.add(key);
		}

		SelectedPartsState state = new SelectedPartsState();
		state.setSelectedPartsKeys(selectedKeys);
		return state;
	}

	private void notifyListeners(String zoneKey, boolean isSelectedNow) {
		SelectedPartsState state = createCurrentSelectedPartsState();
		for (SelectedPartListener listener : listeners) {
			if (isSelectedNow) {
				listener.partSelected(zoneKey, state);
			} else {
				listener.partUnSelected(zoneKey, state);
			}
		}
	}

	public void registerSelectedPartListener(SelectedPartListener listener) {
		listeners.add(listener);
	}
}

@EqualsAndHashCode
@AllArgsConstructor
@Data
class ObjectAndProperty<T> {

	private T		object;
	private String	property;
}
