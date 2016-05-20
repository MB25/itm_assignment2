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

public class AudioMedia extends AbstractMedia {

	private Long duration;
	private String author;
	private String album;
	private String title;
	private String year;
	private String comment;
	private String genre;
	private String composer;
	private String track;
	private String encoding;
	private int bitrate;
	private int channels;
	private float frequency;

	/**
	 * Constructor.
	 */
	public AudioMedia() {
		super();
	}

	/**
	 * Constructor.
	 */

	public AudioMedia(File instance) {
		super(instance);
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
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

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	/**
	 * Serializes this object to the passed file.
	 *
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("type: audio");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		out.println("author: " + getAuthor());
		out.println("title: " + getTitle());
		out.println("duration: " + getDuration());
		out.println("date: " + getYear());
		out.println("comment: " + getComment());
		out.println("album: " + getAlbum());
		out.println("genre: " + getGenre());
		out.println("track: " + getTrack());
		out.println("composer: " + getComposer());
		out.println("encoding: " + getEncoding());
		out.println("bitrate: " + getBitrate());
		out.println("channels: " + getChannels());
		out.println("frequency: " + getFrequency());

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

			if ( line.startsWith( "author: " ) )
				setAuthor(line.substring( "author: ".length()));
			if ( line.startsWith( "title: " ) )
				setTitle(line.substring( "title: ".length()));
			if ( line.startsWith( "duration: " ) )
				setDuration(Long.parseLong(line.substring( "duration: ".length())));
			if ( line.startsWith( "date: " ) )
				setYear(line.substring( "date: ".length()));
			if ( line.startsWith( "comment: " ) )
				setComment(line.substring( "comment: ".length()));
			if ( line.startsWith( "album: " ) )
				setComment(line.substring( "album: ".length()));
			if ( line.startsWith( "genre: " ) )
				setGenre(line.substring( "genre: ".length()));
			if ( line.startsWith( "track: " ) )
				setTrack(line.substring( "track: ".length()));
			if ( line.startsWith( "composer: " ) )
				setComposer(line.substring( "composer: ".length()));
			if ( line.startsWith( "encoding: " ) )
				setEncoding(line.substring( "encoding: ".length()));
			if ( line.startsWith( "bitrate: " ) )
				setBitrate(Integer.parseInt(line.substring( "bitrate: ".length())));
			if ( line.startsWith( "channels: " ) )
				setChannels(Integer.parseInt(line.substring( "channels: ".length())));
			if ( line.startsWith( "frequency: " ) )
				setFrequency(Float.parseFloat(line.substring( "frequency: ".length())));
		}
	}

}
