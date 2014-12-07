package com.mm576.proj;

import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.apporiented.algorithm.clustering.*;
import com.apporiented.algorithm.clustering.visualization.*;

import org.opencv.imgproc.Imgproc;

public class ClusteringHelper {
	
	HashMap<String, Integer> imgMap;
	ImageSub[] imgArr;
	String[] names;
	double[][] diffArr;
	ArrayList<ArrayList<Cluster>> clusters;
	ArrayList<Cluster> representativeLevel;
	
	ClusteringHelper(HashMap<String, Integer> imgMap, ImageSub[] imgArr, int k) {
		this.imgArr = imgArr;
		this.imgMap = imgMap;
		names = new String[imgArr.length];
		for(int i = 0; i < imgArr.length; i++) {
			names[i] = imgArr[i].name;
		}
		diffArr = new double[imgArr.length][imgArr.length];
		calcDist();
		ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
		Cluster cluster = alg.performClustering(diffArr, names,
		    new AverageLinkageStrategy());
		clusters = bfsCreateLevel(cluster);
		for(ArrayList<Cluster> level: clusters) {
			if(level.size() > k) {
				representativeLevel = level;
				break;
			}
		}
		//System.out.println(representativeLevel.toString());
	}
	
	public void showDendrogram(Cluster cluster) {
		DendrogramPanel dp = new DendrogramPanel();
		dp.setModel(cluster);
		JFrame frame = new JFrame();
        frame.add(dp);
        frame.pack();
        frame.setVisible(true);
	}
	
	public void calcDist() {
		
		for(int i = 0; i < imgArr.length; i++) {
			for(int j = 0; j < imgArr.length; j++) {
				if(i != j && diffArr[j][i] == 0) {
					diffArr[i][j] = compareImages(imgArr[i], imgArr[j]);
					diffArr[j][i] = diffArr[i][j];
				}
			}
		}
	}
	
	
	/* 
	 * The current version implements a compareHist on R,G,B channels and returns a double of Root Mean Square Distance
	 */
	public double compareImages(ImageSub img1, ImageSub img2) {
		double[] channels = new double[3];
		double sumOfSquares = 0;
		for(int i = 0; i < channels.length; i++) {
			channels[i] = Imgproc.compareHist(img1.cvChannels.get(i), img2.cvChannels.get(i), Imgproc.CV_COMP_BHATTACHARYYA);
			sumOfSquares += channels[i];
		}
		sumOfSquares /= channels.length;
		//System.out.println(sumOfSquares);
		return sumOfSquares;
	}
	
	public ArrayList<ArrayList<Cluster>> bfsCreateLevel(Cluster root) {
		if(root == null)
			return null;
		ArrayList<ArrayList<Cluster>> levels = new ArrayList<ArrayList<Cluster>> ();
		ArrayList<Cluster> currentLevel = new ArrayList<Cluster>();
		currentLevel.add(root);
		boolean childExist = true;
		while(currentLevel.size() > 0 && childExist) {
			childExist = false;
			levels.add(currentLevel);
			ArrayList<Cluster> parents = currentLevel;
			currentLevel = new ArrayList<>();
			
			for(Cluster clus: parents) {
				List<Cluster> children = clus.getChildren();
				if(children.size() == 0) {
					currentLevel.add(clus);
				}
				for(Cluster c: children) {
					currentLevel.add(c);
					childExist = true;
				}
			}	
		}
		
		return levels;
	}
}
