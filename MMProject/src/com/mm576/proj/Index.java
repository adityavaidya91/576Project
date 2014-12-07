package com.mm576.proj;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.*;

import org.opencv.core.*;
//This import is for all calcHist, compare and image processing
import org.opencv.imgproc.*;

import com.apporiented.algorithm.clustering.Cluster;

public class Index {
	
	//@TODO: Don't use the imgArr and imgMap statics
	//Reconsider doing this post completion, keeping it here for now
	static ImageSub[] imgArr;
	static HashMap<String, Integer> imgMap;
	static HashMap<String, ArrayList<String>> clusterReps = new HashMap<>();
	static final int WIDTH = 352;
	static final int HEIGHT = 288;

	static JFrame frame;
	static JPanel panel; 
	static JScrollPane pane;
	//Modify this for now, read in different directories
	static String dirName = "dataset-1";
	
	public static void main( String[] args )
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("Reading files....");
		iterateAndClusterDirectory(dirName);
		frame.setVisible(true);
	}
	
	public static void iterateAndClusterDirectory(String dirName) {
		File[] files = new File(dirName).listFiles();
		//imgArr = new ImageSub[files.length];
		imgArr = new ImageSub[50];
		imgMap = new HashMap<>();
		for(int i = 0; i < imgArr.length; i++) {
			imgArr[i] = new ImageSub(files[i], WIDTH, HEIGHT);
			imgMap.put(files[i].getName(), i);
		}		
		
		ClusteringHelper k = new ClusteringHelper(imgMap, imgArr, imgArr.length/8);	
		clusterReps = createClusterReps(k.representativeLevel);
		ArrayList<String> displayList = new ArrayList<>(clusterReps.keySet());
		//System.out.println(displayList.toString());
		showImageGrid(displayList, "Initial");
	}
	
	public static void showImageGrid(ArrayList<String> displayList, String when) {
		try {
			if(panel != null) {
				frame.remove(pane);
			}
			
			panel = new MyPanel();
	        int gridSize = (int)Math.ceil(Math.sqrt(displayList.size()));
	        GridLayout gl = new GridLayout(gridSize + 1, gridSize, 4, 4);
	        panel.setLayout(gl);
	        if(when == "After") {
	        	JLabel label = new JLabel();
	        	BufferedImage back = ImageIO.read(new File("back.png"));
	        	label.setIcon(new ImageIcon(back.getScaledInstance(WIDTH/3, HEIGHT/3, Image.SCALE_SMOOTH)));
	        	label.addMouseListener(new MyMouseListener(new ArrayList<>(clusterReps.keySet()), null, "back"));
	        	panel.add(label);
	        }
	        JLabel labels[] = new JLabel[displayList.size()];
	        
	        for(int i = 0; i < displayList.size(); i++) {
	        	BufferedImage imgToAdd = imgArr[imgMap.get(displayList.get(i))].javaImg;
	        	labels[i] = new JLabel();
	        	labels[i].setIcon(new ImageIcon(imgToAdd.getScaledInstance(WIDTH/3, HEIGHT/3, Image.SCALE_SMOOTH)));
	        	//System.out.println("Binding Listeners....");
	        	if(when == "Initial")
	        		labels[i].addMouseListener(new MyMouseListener(clusterReps.get(displayList.get(i)), null, when));
	        	else if(when == "After"){
	        		//System.out.println(imgArr[imgMap.get(displayList.get(i))]);
	        		labels[i].addMouseListener(new MyMouseListener(null, imgArr[imgMap.get(displayList.get(i))], when));
	        	}
	        		
	        	panel.add(labels[i]);
	        }
	        pane = new JScrollPane(panel);
	        frame.add(pane);
	        frame.validate();
	        frame.repaint();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static HashMap<String, ArrayList<String>> createClusterReps(ArrayList<Cluster> representativeLevel) {
		HashMap<String, ArrayList<String>> returnMap = new HashMap<String, ArrayList<String>>();
		for(Cluster c: representativeLevel) {
			if(c.getName().indexOf("&") != -1) {
				String name = c.getName().substring(0, c.getName().indexOf("&"));
				ArrayList<String> names = new ArrayList<>(Arrays.asList(c.getName().split("&")));
				returnMap.put(name, names);
			}
			else {
				ArrayList<String> names = new ArrayList<String>();
				names.add(c.getName());
				returnMap.put(c.getName(), names);
			}
		}
		return returnMap;
	}
}	
