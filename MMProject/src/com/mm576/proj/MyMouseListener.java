package com.mm576.proj;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MyMouseListener implements MouseListener {
	
	ArrayList<String> displayList;
	String when;
	ImageSub img;
	
	MyMouseListener(ArrayList<String> displayList, ImageSub img, String when) {
		this.displayList = displayList;
		this.when = when;
		this.img = img;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		switch(when) {
		case "Initial": 
			Index.showImageGrid(displayList, "After");
			break;
		case "After":
			//Bind image/video players
			if(img.videoImgs == null)
				ImageSub.showResult(img);
			else	
				ImageSub.playVideo(img);
			break;
		case "back":
			Index.showImageGrid(displayList, "Initial");
			break;
		default:
			break;
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
