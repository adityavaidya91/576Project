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
	
	//@TODO: Don't use the imgArr and imgMap statics
	//Reconsider doing this post completion, keeping it here for now
	static ImageSub[] imgArr;
	static HashMap<String, Integer> imgMap;
	static final int WIDTH = 352;
	static final int HEIGHT = 288;

	//Modify this for now, read in different directories
	static String dirName = "dataset-1";
	
	public static void main( String[] args )
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		iterateAndClusterDirectory(dirName);
	}
	
	public static void iterateAndClusterDirectory(String dirName) {
		File[] files = new File(dirName).listFiles();
		imgArr = new ImageSub[files.length];
		imgMap = new HashMap<>();
		for(int i = 0; i < imgArr.length; i++) {
			imgArr[i] = new ImageSub(files[i], WIDTH, HEIGHT);
			imgMap.put(files[i].getName(), i);
		}		
		for(String s: imgMap.keySet()){
			if(s.indexOf("042")!=-1 || s.indexOf("50")!=-1)
				showResult(imgArr[imgMap.get(s)]);
		}
		ClusteringHelper k = new ClusteringHelper(imgMap, imgArr, imgArr.length/10);	
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
