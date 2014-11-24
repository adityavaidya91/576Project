package com.mm576.proj;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.*;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
//This import is for all calcHist, compare and image processing
import org.opencv.imgproc.*;

public class Index {
	
	//@TODO: Handle videos here, remember only a representative frame is needed
	//Options available: Use a Factory design pattern
	//Keeping it as is might also work, if we are considering first frame only
	static ImageSub[] imgArr;
	static final int WIDTH = 352;
	static final int HEIGHT = 288;

	//Modify this for now, read in different directories
	static String dirName = "dataset-1";
	
	public static void main( String[] args )
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		iterateDirectory(dirName);
		ClusteringHelper k = new ClusteringHelper(imgArr, 3); //If sufficiently large, make this imgArr.length/10
	}
	
	public static void iterateDirectory(String dirName) {
		File[] files = new File(dirName).listFiles();
		imgArr = new ImageSub[files.length];
		for(int i = 0; i < imgArr.length; i++) {
			imgArr[i] = new ImageSub(files[i], WIDTH, HEIGHT);
		}		
		for(ImageSub img: imgArr){
			if(img.name.indexOf("02")!=-1 || img.name.indexOf("39")!=-1)
				showResult(img);
		}
			
	}
	
	//This displays only one image
	public static void showResult(ImageSub img) {
	    try {
	        JFrame frame = new JFrame();
	        frame.getContentPane().add(new JLabel(new ImageIcon(img.javaImg)));
	        frame.setTitle(img.name);
	        frame.pack();
	        frame.setVisible(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}	
