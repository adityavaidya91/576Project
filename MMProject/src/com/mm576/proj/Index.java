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
	static final int WIDTH = 512;
	static final int HEIGHT = 512;

	//Modify this for now, read in different directories
	static String dirName = "sample";
	
	public static void main( String[] args )
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		iterateDirectory(dirName);
	}
	
	public static void iterateDirectory(String dirName) {
		File[] files = new File(dirName).listFiles();
		imgArr = new ImageSub[files.length];
		for(int i = 0; i < imgArr.length; i++) {
			imgArr[i] = new ImageSub(files[i], WIDTH, HEIGHT);
		}
		compareImages(imgArr[0], imgArr[1]);
		showResult(imgArr[0]);
	}
	
	
	/*
	 * A few good references: http://stackoverflow.com/questions/22464503/how-to-use-opencv-to-calculate-hsv-histogram-in-java-platform
	 * http://stackoverflow.com/questions/11541154/checking-images-for-similarity-with-opencv
	 * http://docs.opencv.org/doc/tutorials/imgproc/histograms/histogram_comparison/histogram_comparison.html
	 * http://www.pyimagesearch.com/2014/07/14/3-ways-compare-histograms-using-opencv-python/
	 */
	public static void compareImages(ImageSub img1, ImageSub img2) {
		//This does not work directly, it needs to be greyscale images
		double res = Imgproc.compareHist(img1.cvChannels.get(0), img2.cvChannels.get(0), Imgproc.CV_COMP_CORREL);
		System.out.println(res);
		
	}
	
	public static void showResult(ImageSub img) {
	    try {
	        JFrame frame = new JFrame();
	        frame.getContentPane().add(new JLabel(new ImageIcon(img.javaImg)));
	        frame.pack();
	        frame.setVisible(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}	
