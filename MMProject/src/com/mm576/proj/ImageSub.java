package com.mm576.proj;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.*;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ImageSub {
	
	int width, height;
	BufferedImage javaImg;
	Mat cvImg;
	List<Mat> cvChannels;
	
	public ImageSub(File file, int width, int height) {
		this.width = width;
		this.height = height;
		javaImg = readImage(file);
		cvChannels = new LinkedList<Mat>();
		cvImg = ImageSub.img2Mat(cvChannels, javaImg);
	}
	
	//This is the starter code for the course
	public BufferedImage readImage(File file){
		try{
			BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
			//File file = new File(fileName);
			InputStream is = new FileInputStream(file);
            long len = file.length();
            byte[] bytes = new byte[(int)len];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
            int ind = 0;
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    byte a = 0;
                    byte r = bytes[ind];
                    byte g = bytes[ind+height*width];
                    byte b = bytes[ind+height*width*2]; 

                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                    img.setRGB(x,y,pix);
                    ind++;
                }
            }
			return img;
		}
		catch(Exception e) {
			System.out.println(e + "In file read, Images");
		}
		return null;
	}
	
	//Helper methods from codeproject.com to convert BufferedImage to Mat and vice-versa
	public static Mat img2Mat(List<Mat> mv, BufferedImage in)
    {
          Mat out;
          byte[] data;
          int r, g, b;

          out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
          data = new byte[in.getHeight() * in.getWidth() * (int)out.elemSize()];
          int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
          for(int i = 0; i < dataBuff.length; i++)
          {
              data[i*3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
              data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
              data[i*3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
          }

           out.put(0, 0, data);
           
           Core.split(out, mv);
           for(Mat m : mv) {
        	   m.convertTo(m, CvType.CV_32F);
           }
           
           //System.out.println(mv.size());
           
           //This did not help either, haven't found solution yet
           //out.convertTo(out, CvType.CV_32F);
           return out;
     }
	
	//Untested
    public BufferedImage toBufferedImage(Mat m){
		  int type = BufferedImage.TYPE_BYTE_GRAY;
		  if ( m.channels() > 1 ) {
		      type = BufferedImage.TYPE_3BYTE_BGR;
		  }
		  int bufferSize = m.channels()*m.cols()*m.rows();
		  byte [] b = new byte[bufferSize];
		  m.get(0,0,b); // get all the pixels
	      BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	      final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      System.arraycopy(b, 0, targetPixels, 0, b.length);  
	      return image;
	
	  }




}
