package itm.util;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class DecodeAndCaptureFrame extends MediaListenerAdapter
{

    public static final double SECONDS_BETWEEN_FRAMES = 1;
    public static final long MICRO_SECONDS_BETWEEN_FRAMES = (long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

    //Time of last frame write.
    private static long mLastPtsWrite = Global.NO_PTS;

    private static int mVideoStreamIndex = -1;

    private File outFile;
    private long captureTime;
    private boolean gotMiddleFrame = false;


    public DecodeAndCaptureFrame(String filename, File outputFile, long captureTime) {

        this.outFile = outputFile;
        this.captureTime = captureTime;

        // create a media reader for processing video
        IMediaReader reader = ToolFactory.makeReader(filename);

        reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        reader.addListener(this);

        // read out the contents of the media file
        // abort if middle frame captured
        while (reader.readPacket() == null && !gotMiddleFrame);
    }
    
    
    /**
     * Called after a video frame has been decoded from a media stream.
     */
    public void onVideoPicture(IVideoPictureEvent event) {

        try {

            if (event.getStreamIndex() != mVideoStreamIndex) {
                if (mVideoStreamIndex == -1)
                    mVideoStreamIndex = event.getStreamIndex();
                else
                    return;
            }

            // if uninitialized, backdate mLastPtsWrite so we get the very
            // first frame
            if (mLastPtsWrite == Global.NO_PTS)
                mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;

            // if it's time to write the next frame

            if (event.getTimeStamp() - mLastPtsWrite >= this.captureTime) {

                // write image to file
                ImageIO.write(event.getImage(), "jpg", this.outFile);
                
                // update last write time
                mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;

                // set flag to finish
                this.gotMiddleFrame = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}