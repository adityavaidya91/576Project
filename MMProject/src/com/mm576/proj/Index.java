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
		
		double[][] diffArr = new double[imgArr.length][imgArr.length];
		for(int i = 0; i < imgArr.length; i++) {
			for(int j = 0; j < imgArr.length; j++) {
				if(i != j && diffArr[j][i] == 0) {
					diffArr[i][j] = compareImages(imgArr[i], imgArr[j]);
					diffArr[j][i] = diffArr[i][j];
				}
			}
		}
		//showResult(imgArr[0]);
	}
	
	
	/*
	 * A few good references: http://stackoverflow.com/questions/22464503/how-to-use-opencv-to-calculate-hsv-histogram-in-java-platform
	 * http://stackoverflow.com/questions/11541154/checking-images-for-similarity-with-opencv
	 * http://docs.opencv.org/doc/tutorials/imgproc/histograms/histogram_comparison/histogram_comparison.html
	 * http://www.pyimagesearch.com/2014/07/14/3-ways-compare-histograms-using-opencv-python/
	 * 
	 * The current version implements a compareHist on R,G,B channels and returns a double of Root Mean Square Distance
	 */
	public static double compareImages(ImageSub img1, ImageSub img2) {
		//This does not work directly, it needs to be greyscale images
		double[] channels = new double[3];
		double sumOfSquares = 0;
		for(int i = 0; i < channels.length; i++) {
			channels[i] = Imgproc.compareHist(img1.cvChannels.get(i), img2.cvChannels.get(i), Imgproc.CV_COMP_CORREL);
			sumOfSquares += channels[i] * channels[i];
		}
		sumOfSquares /= channels.length;
		System.out.println(sumOfSquares);
		return Math.sqrt(sumOfSquares);
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
