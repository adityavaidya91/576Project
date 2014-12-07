package com.mm576.proj;

import java.awt.Graphics;

import javax.swing.JFrame;

public class MyFrame extends JFrame {
	String frameType;
	
	MyFrame(String frameType) {
		this.frameType = frameType;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
	}
}
