package itm.video;

import itm.model.MediaFactory;
import itm.model.VideoMedia;
import itm.util.DecodeAndCaptureFrame;

/*******************************************************************************
 This file is part of the ITM course 2016
 (c) University of Vienna 2009-2016
@author Markus Bader 1404709
Does not correctly yet

 *******************************************************************************/

import itm.util.ImageCompare;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IIndexEntry;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.ConverterFactory;

/**
 * This class reads video files, extracts metadata for both the audio and the
 * video track, and writes these metadata to a file.
 * 
 * It can be called with 3 parameters, an input filename/directory, an output
 * directory and an "overwrite" flag. It will read the input video file(s),
 * retrieve the metadata and write it to a text file in the output directory.
 * The overwrite flag indicates whether the resulting output file should be
 * overwritten or not.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */
public class VideoThumbnailGenerator {
	
	ArrayList<BufferedImage> frameList = new ArrayList<BufferedImage>();
	
	/**
	 * Constructor.
	 */
	public VideoThumbnailGenerator() {
	}

	/**
	 * Processes a video file directory in a batch process.
	 * 
	 * @param input
	 *            a reference to the video file directory
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing output files should be overwritten
	 *            or not
	 * @return a list of the created media objects (videos)
	 */
	public ArrayList<File> batchProcessVideoFiles(File input, File output, boolean overwrite, int timespan) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<File> ret = new ArrayList<File>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4"))
					try {
						File result = processVideo(f, output, overwrite, timespan);
						System.out.println("processed file " + f + " to " + output);
						ret.add(result);
					} catch (Exception e0) {
						System.err.println("Error processing file " + input + " : " + e0.toString());
					}
			}
		} else {

			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4"))
				try {
					File result = processVideo(input, output, overwrite, timespan);
					System.out.println("processed " + input + " to " + result);
					ret.add(result);
				} catch (Exception e0) {
					System.err.println("Error when creating processing file " + input + " : " + e0.toString());
				}

		}
		return ret;
	}

	/**
	 * Processes the passed input video file and stores a thumbnail of it to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input video file
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing files should be overwritten or not
	 * @return the created video media object
	 */
	protected File processVideo(File input, File output, boolean overwrite, int timespan) throws Exception {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		// create output file and check whether it already exists.
		//File outputFile = new File(output, input.getName() + "_thumb.avi");
		
		//Getting IMediaReader 
		//
		VideoMedia media = (VideoMedia) MediaFactory.createMedia(input);
		IContainer container = IContainer.make();
		IContainerFormat format = IContainerFormat.make();

		//DecodeAndCaptureFrame dcf = null;
		if (container.open(input.getAbsoluteFile().toString(), IContainer.Type.READ, format) < 0)
			throw new IllegalArgumentException("could not open file: " + input.getAbsoluteFile());
		long duration = container.getDuration(); //microseconds. divide by 1000^2 for s
		int dur_in_sec = (int) duration/(1000*1000);
		int allFrames;
		long interval = timespan*1000*1000;
		double frameRate;
		int numStreams = container.getNumStreams();
		int vidstream;
		File outputFile = new File(output, input.getName() + "_thumb");
		for(int i = 0; i<3; i++){
			outputFile = new File(output, input.getName() + "_thumb" +i + ".avi");
			new DecodeAndCaptureFrame(input.getAbsolutePath(), outputFile, interval);
			interval +=interval;
		}
		//Zum Testen
		//Macht 4 AVI Dateien von der jeweiligen Stelle..
		//Ohne speichern möglich, ums dann weiterzuverarbeiten?
		
		return outputFile;
	}
		
	
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// extract frames from input video
		
		// add a watermark of your choice and paste it to the image
        // e.g. text or a graphic

		// create a video writer

		// add a stream with the proper width, height and frame rate
		
		// if timespan is set to zero, compare the frames to use and add 
		// only frames with significant changes to the final video

		// loop: get the frame image, encode the image to the video stream
		
		// Close the writer



	
	protected BufferedImage watermark(BufferedImage img){ //From Assignment 1
    	BufferedImage watermark = new BufferedImage(img.getWidth(), img.getHeight(),
    	        BufferedImage.TYPE_INT_RGB);
    	        
    	        Graphics g = watermark.getGraphics();
    	        g.drawImage(img, 0, 0, null);
    	        g.setFont(new Font("Arial", Font.BOLD, img.getHeight()/3));
    	        Color c = new Color(255,0,0,120);
    	        
    	        
    	        g.setColor(c);
    	        String s = "Uni Wien";
    	        g.drawString(s, 0, img.getHeight());
    	        g.dispose();
    	        return watermark;
    	        
    }

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 3) {
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-video> <output-directory> <timespan>");
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-directory> <output-directory> <timespan>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        File fo = new File(args[1]);
        int timespan = 5;
        if(args.length == 3)
            timespan = Integer.parseInt(args[2]);
        
        VideoThumbnailGenerator videoMd = new VideoThumbnailGenerator();
        videoMd.batchProcessVideoFiles(fi, fo, true, timespan);
	}
}