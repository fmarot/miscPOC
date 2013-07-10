package com.teamtter.svg;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

@Slf4j
public class SVGApplication extends JFrame {

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				String filename = "countries.svg";
				//				String filename = "test.svgz.uncompressed";
				File svgFile = new File("src/main/resources/" + filename);
				SVGApplication app;
				try {
					app = new SVGApplication(svgFile);
					app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					app.setSize(400, 400);
					app.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SVGApplication(File svgFile) throws Exception {
		JComponent comp = createComponents(svgFile);
		getContentPane().add(comp);
	}

	private JSVGCanvas	svgCanvas	= new JSVGCanvas();

	public JComponent createComponents(File svgFile) throws Exception {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(svgCanvas, BorderLayout.CENTER);
		svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		svgCanvas.setURI(svgFile.toURL().toString());
		svgCanvas.enableInputMethods(true);

		svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {

			@Override
			public void gvtBuildStarted(GVTTreeBuilderEvent e) {
				log.info("Build Started...");
			}

			@Override
			public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
				log.info("Build Done.");
				pack();
			}
		});

		svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {

			@Override
			public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
				log.info("Rendering Started...");
			}

			@Override
			public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
				log.info("gvtRenderingCompleted");
				registerListener("fr");
				registerListener("es");
				//				registerListener("aaa");
				//				registerListener("circle");

			}
		});

		return panel;
	}

	protected void registerListener(String id) {
		Element elt = svgCanvas.getSVGDocument().getElementById(id);
		EventTarget t = (EventTarget) elt;
		t.addEventListener("click", new ClickHandler(), false);
	}

	public class ClickHandler implements EventListener {

		@Override
		public void handleEvent(Event evt) {
			log.error("Click on " + ((Element) evt.getTarget()).getAttribute("id"));
		}
	}

}