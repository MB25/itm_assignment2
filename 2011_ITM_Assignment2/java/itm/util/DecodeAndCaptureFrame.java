package itm.util;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DecodeAndCaptureFrame extends MediaListenerAdapter
{
    //Time of last frame write.
    private long mLastPtsWrite;
    private int mVideoStreamIndex;

    private long microSecondsBetweenFrame;

    private File outFile;
    private long captureTime;

    private boolean captureMiddleFrame;
    private boolean gotMiddleFrame;

    private List<BufferedImage> capturedFrames;

    private IMediaReader reader;

    public DecodeAndCaptureFrame() {}

    public void init(String filename) {

        this.mVideoStreamIndex          = -1;
        this.mLastPtsWrite              = Global.NO_PTS;
        this.microSecondsBetweenFrame   = Global.DEFAULT_PTS_PER_SECOND;
        this.capturedFrames             = new ArrayList<>();
        this.captureMiddleFrame         = false;
        this.gotMiddleFrame             = false;

        // create a media reader for processing video
        this.reader = ToolFactory.makeReader(filename);
        this.reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        this.reader.addListener(this);
    }

    public void captureMiddleFrame(File outputFile, long captureTime) {
        this.outFile = outputFile;
        this.captureTime = captureTime;
        this.captureMiddleFrame = true;

        while (reader.readPacket() == null && !gotMiddleFrame);
    }

    public List<BufferedImage> getFramesByInterval(int interval) {
        // set interval
        this.microSecondsBetweenFrame = (Global.DEFAULT_PTS_PER_SECOND * interval);

        // read packets
        while (reader.readPacket() == null);

        return capturedFrames;
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
                mLastPtsWrite = event.getTimeStamp() - microSecondsBetweenFrame;


            if(this.captureMiddleFrame) {

                // only capture middle frame
                if (event.getTimeStamp() - mLastPtsWrite >= this.captureTime) {

                    // write image to file
                    ImageIO.write(event.getImage(), "jpg", this.outFile);

                    // update last write time
                    mLastPtsWrite += microSecondsBetweenFrame;

                    // set flag to finish
                    this.gotMiddleFrame = true;
                }
            } else {
                // capture frames according to interval
                if (event.getTimeStamp() - mLastPtsWrite >= microSecondsBetweenFrame) {

                    // write image to file
                    this.capturedFrames.add(event.getImage());

                    // update last write time
                    mLastPtsWrite += microSecondsBetweenFrame;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}