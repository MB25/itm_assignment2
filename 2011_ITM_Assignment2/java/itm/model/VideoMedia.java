package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class VideoMedia extends AbstractMedia {

	/* video format metadata */
	private String codecVideo;
	private String codecVideoId;
	private Double frameRate;
	private long duration;
	private int width;
	private int height;

	/* audio format metadata */
	private String codecAudio;
	private String codecAudioId;
	private int bitrate;
	private int channels;
	private int sampleRate;


	/**
	 * Constructor.
	 */
	public VideoMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public VideoMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */

	public String getCodecVideo() {
		return codecVideo;
	}

	public void setCodecVideo(String codecVideo) {
		this.codecVideo = codecVideo;
	}

	public String getCodecVideoId() {
		return codecVideoId;
	}

	public void setCodecVideoId(String codecVideoId) {
		this.codecVideoId = codecVideoId;
	}

	public Double getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(Double frameRate) {
		this.frameRate = frameRate;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getCodecAudio() {
		return codecAudio;
	}

	public void setCodecAudio(String codecAudio) {
		this.codecAudio = codecAudio;
	}

	public String getCodecAudioId() {
		return codecAudioId;
	}

	public void setCodecAudioId(String codecAudioId) {
		this.codecAudioId = codecAudioId;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	/**
	 * Serializes this object to the passed file.
	 *
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("type: video");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		// video
		out.println("codecVideo: " + getCodecVideo());
		out.println("codecVideoId: " + getCodecVideoId());
		out.println("frameRate: " + getFrameRate());
		out.println("duration: " + getDuration());
		out.println("width: " + getWidth());
		out.println("height: " + getHeight());

		// audio
		out.println("codecAudio: " + getCodecAudio());
		out.println("codecAudioId: " + getCodecAudioId());
		out.println("bitrate: " + getBitrate());
		out.println("channels: " + getChannels());
		out.println("sampleRate: " + getSampleRate());

		return data.getBuffer();
	}

	/**
	 * Deserializes this object from the passed string buffer.
	 */
	@Override
	public void deserializeObject(String data) throws IOException {
		super.deserializeObject(data);

		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while ((line = br.readLine()) != null) {

			// video
			if ( line.startsWith( "codecVideo: " ) )
				setCodecVideo(line.substring( "codecVideo: ".length()));
			if ( line.startsWith( "codecVideoId: " ) )
				setCodecVideoId(line.substring( "codecVideoId: ".length()));
			if ( line.startsWith( "frameRate: " ) )
				setFrameRate(Double.parseDouble(line.substring( "frameRate: ".length())));
			if ( line.startsWith( "duration: " ) )
				setDuration(Integer.parseInt(line.substring( "duration: ".length())));
			if ( line.startsWith( "width: " ) )
				setWidth(Integer.parseInt(line.substring( "width: ".length())));
			if ( line.startsWith( "height: " ) )
				setHeight(Integer.parseInt(line.substring( "height: ".length())));

			if ( line.startsWith( "codecAudio: " ) )
				setCodecAudio(line.substring( "codecAudio: ".length()));
			if ( line.startsWith( "codecAudioId: " ) )
				setCodecAudioId(line.substring( "codecAudioId: ".length()));
			if ( line.startsWith( "bitrate: " ) )
				setBitrate(Integer.parseInt(line.substring( "bitrate: ".length())));
			if ( line.startsWith( "channels: " ) )
				setChannels(Integer.parseInt(line.substring( "channels: ".length())));
			if ( line.startsWith( "sampleRate: " ) )
				setSampleRate(Integer.parseInt(line.substring( "sampleRate: ".length())));
		}
	}

}
