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
	static Image[] imgArr;
	static final int WIDTH = 352;
	static final int HEIGHT = 288;

	//Modify this for now, read in different directories
	static String dirName = "sample";
	
	public static void main( String[] args )
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
		System.out.println( "mat = " + mat.dump() );
	}
	
	public static void iterateDirectory(String dirName) {
		File[] files = new File(dirName).listFiles();
		imgArr = new Image[files.length];
		for(int i = 0; i < imgArr.length; i++) {
			imgArr[i] = new Image(files[i].getName(), WIDTH, HEIGHT);
		}
	}
	
	public static void showResult(Mat img) {
	    Imgproc.resize(img, img, new Size(640, 480));
	    MatOfByte matOfByte = new MatOfByte();
	    Highgui.imencode(".jpg", img, matOfByte);
	    byte[] byteArray = matOfByte.toArray();
	    BufferedImage bufImage = null;
	    try {
	        InputStream in = new ByteArrayInputStream(byteArray);
	        bufImage = ImageIO.read(in);
	        JFrame frame = new JFrame();
	        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
	        frame.pack();
	        frame.setVisible(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}	
