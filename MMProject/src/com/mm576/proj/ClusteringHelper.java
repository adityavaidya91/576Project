package com.mm576.proj;

import java.util.*;
import com.apporiented.algorithm.clustering.*;
import com.apporiented.algorithm.clustering.visualization.*;

import org.opencv.imgproc.Imgproc;

public class ClusteringHelper {

	ImageSub[] imgArr;
	List<ImageSub> centroids;
	String[] names;
	double[][] diffArr;
	
	//Have not used k yet
	ClusteringHelper(ImageSub[] imgArr, int k) {
		this.imgArr = imgArr;
		names = new String[imgArr.length];
		for(int i = 0; i < imgArr.length; i++) {
			names[i] = imgArr[i].name;
		}
		//centroids = new ArrayList<ImageSub>(k);
		diffArr = new double[imgArr.length][imgArr.length];
		calcDist();
		ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
		Cluster cluster = alg.performClustering(diffArr, names,
		    new AverageLinkageStrategy());
		cluster.toConsole(5);
		System.out.println(cluster.toString());
		DendrogramPanel dp = new DendrogramPanel();
		dp.setModel(cluster);
		//initCentroids();
		//cluster();
	}
	
	public List<List<ImageSub>> cluster() {
		List<List<ImageSub>> result = new LinkedList<List<ImageSub>>();
		
		return result;
	}
	
	public void initCentroids() {
		if(centroids.size() > imgArr.length) {
			return;
		}
		int i = 0;
		for(ImageSub centroid: centroids) {
			centroid = imgArr[i++];
		}
	}
	
	public void updateCentroids() {
		
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
	 * A few good references: http://stackoverflow.com/questions/22464503/how-to-use-opencv-to-calculate-hsv-histogram-in-java-platform
	 * http://stackoverflow.com/questions/11541154/checking-images-for-similarity-with-opencv
	 * http://docs.opencv.org/doc/tutorials/imgproc/histograms/histogram_comparison/histogram_comparison.html
	 * http://www.pyimagesearch.com/2014/07/14/3-ways-compare-histograms-using-opencv-python/
	 * 
	 * The current version implements a compareHist on R,G,B channels and returns a double of Root Mean Square Distance
	 */
	public static double compareImages(ImageSub img1, ImageSub img2) {
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
}
