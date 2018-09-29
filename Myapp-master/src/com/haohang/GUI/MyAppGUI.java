package com.haohang.GUI;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyAppGUI extends Frame {
	public MyAppGUI() {
		this.setSize(600, 800);
		this.setLocation(400, 0);
		TextArea text = new TextArea(40, 60);
		text.setEditable(false);
		Panel panel = new Panel();
		panel.add(text);
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.CENTER);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
}
