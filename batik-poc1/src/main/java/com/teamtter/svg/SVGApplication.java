package com.teamtter.svg;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SVGApplication extends JFrame implements SelectedPartListener, ActionListener {

	private JButton	addArrowButton;
	private JButton	addMarkButton;

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				//				String filename = "countries.svg";
				//				String filename = "test.svg";
				String filename = "test02.svg";
				//				String filename = "16.svg";
				//				String filename = "circle.svg";
				File svgFile = new File("src/main/resources/" + filename);
				SVGApplication app = new SVGApplication(svgFile);
				app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				app.setSize(400, 400);
				app.setVisible(true);
				app.pack();
			}
		});
	}

	public SVGApplication(File svgFile) {

		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel controlsPanel = new JPanel(new FlowLayout());
		addArrowButton = new JButton("add Arrow");
		addArrowButton.addActionListener(this);
		controlsPanel.add(addArrowButton);
		addMarkButton = new JButton("add Mark");
		addMarkButton.addActionListener(this);
		controlsPanel.add(addMarkButton);
		mainPanel.add(controlsPanel, BorderLayout.NORTH);
		ImageWithClickableParts app = new ImageWithClickableParts(svgFile);
		app.registerSelectedPartListener(this);
		Component svgImage = app.getAsComponent();
		mainPanel.add(svgImage, BorderLayout.CENTER);
		getContentPane().add(mainPanel);
	}

	@Override
	public void partSelected(String partKey, SelectedPartsState event) {
		log.info("{} selected", partKey);
		displaySelectedParts(event);
	}

	@Override
	public void partUnSelected(String partKey, SelectedPartsState event) {
		log.info("{} unselected", partKey);
		displaySelectedParts(event);
	}

	private void displaySelectedParts(SelectedPartsState event) {
		log.info("The following parts are currently selected: {}", event.getSelectedPartsKeys());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

}