package itm.audio;

/*******************************************************************************
 This file is part of the ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Plays an audio file using the system's default sound output device
 * 
 */
public class AudioPlayer {
	
	
	
	
	/**
	 * Constructor
	 */
	public AudioPlayer() {

	}

	/**
	 * Plays audio data from a given input file to the system's default sound
	 * output device
	 * 
	 * @param input
	 *            the audio file
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	protected void playAudio(File input) throws IOException, LineUnavailableException {

		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");

		AudioInputStream audio = null;
		try {
			audio = openAudioInputStream(input);
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("could not open audio file " + input
					+ ". Encoding / file format not supported");
		}
		
		try {
			rawplay(audio);
		} catch (LineUnavailableException e) {
			throw new IOException("Error when playing sound from file "
					+ input.getName() + ". Sound output device unavailable");
		}
		
		audio.close();

	}

	/**
	 * Decodes an encoded audio file and returns a PCM input stream
	 * 
	 * Supported encodings: MP3, OGG (requires SPIs to be in the classpath)
	 * 
	 * @param input
	 *            a reference to the input audio file
	 * @return a PCM AudioInputStream
	 * @throws UnsupportedAudioFileException
	 *             an audio file's encoding is not supported
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	private AudioInputStream openAudioInputStream(File input)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		AudioInputStream din = null;
		AudioInputStream in = AudioSystem.getAudioInputStream(input); //gets audioinputstream from the input
		AudioFormat format = in.getFormat();
		AudioFormat decoded = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,//setting up format for raw audio file
				format.getSampleRate(), 
				16, 
				format.getChannels(),
				format.getChannels()*2, 
				format.getSampleRate(), 
				false);
		
		din = AudioSystem.getAudioInputStream(decoded, in);
		
 
		return din;
	}

	/**
	 * Writes audio data from an AudioInputStream to a SourceDataline
	 * 
	 * @param audio
	 *            the audio data
	 * @throws IOException
	 *             error when writing audio data to source data line
	 * @throws LineUnavailableException
	 *             system's default source data line is not available
	 */
	
	//http://edwin.baculsoft.com/2010/11/how-to-play-mp3-files-with-java/
	//http://docs.oracle.com/javase/tutorial/sound/playing.html
	private void rawplay(AudioInputStream audio) throws IOException,
			LineUnavailableException {

		AudioFormat format = audio.getFormat(); //retrieves format from above..
		
		DataLine.Info dl = new DataLine.Info(SourceDataLine.class, format); //.. and retrieves dataline
		SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(dl); 
		
		sdl.open(format);
		sdl.start();
		int BUFFER_SIZE = sdl.getBufferSize();
		int bytesread = -1;
		byte[] buffer = new byte[BUFFER_SIZE];
		
		while((bytesread = audio.read(buffer)) != -1){
			sdl.write(buffer, 0, bytesread);
		}
		
		sdl.drain(); //properly closing everything
		sdl.close();
		audio.close();
		
		
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// get audio format
		
		// get a source data line
		
		// read samples from audio and write them to the data line 

		// properly close the line!
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {
		
		if (args.length < 1) {
			System.out
					.println("usage: java itm.audio.AudioPlayer <input-audioFile>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		
		AudioPlayer player = new AudioPlayer();
		player.playAudio(new File(args[0]));
		System.exit(0);

	}

}
