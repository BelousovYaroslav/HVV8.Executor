/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.service;

import JProg.service.JProgServSoundStatement;
import hvv_executor.executors.AStatementExeThread;
import hvv_executor.FrmMainWindow;
import hvv_executor.HVV_Executor;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.log4j.Logger;
import org.dom4j.Element;

/**
 *
 * @author yaroslav
 */
public class SoundStatementExecutorThreadOld extends AStatementExeThread implements LineListener {

    JProgServSoundStatement m_ExeStatement;
    static Logger logger = Logger.getLogger(SoundStatementExecutorThreadOld.class);            
    
    public boolean m_bPlayCompleted;
    public SoundStatementExecutorThreadOld( HVV_Executor app, JProgServSoundStatement statement) {
        super( app);
        m_ExeStatement = statement;
    }

    @Override
    public void run() {
        String gongFile = m_ExeStatement.GetWavFile();
        
        /*
        // open the sound file as a Java input stream
        
        InputStream in;
        try {
            in = new FileInputStream( gongFile);

 
            // create an audiostream from the inputstream
            AudioStream audioStream = new AudioStream(in);
 
            // play the audio clip with the audioplayer class
            AudioPlayer.player.start( audioStream);
            
            
            
        } catch( Exception ex) {
            logger.debug( "Exception while playing audio ", ex);
        }
        */
        
        
        File audioFile = new File( gongFile);
 
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream( audioFile);
            AudioFormat format = audioStream.getFormat();
            
            DataLine.Info info = new DataLine.Info( Clip.class, format);
            
            Clip audioClip = (Clip) AudioSystem.getLine( info);
            
            audioClip.addLineListener( this);
            audioClip.open( audioStream);
            audioClip.start();
            
            m_bPlayCompleted = false;
            while( !m_bPlayCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep( 100);
                } catch ( InterruptedException ex) {
                    logger.error( "InterruptedException caught!", ex);
                }
            }
             
            audioClip.close();
             
        } catch (UnsupportedAudioFileException ex) {
            logger.error( "The specified audio file is not supported.", ex);
        } catch (LineUnavailableException ex) {
            logger.error( "Audio line for playing back is unavailable.", ex);
        } catch (IOException ex) {
            logger.error( "Error playing the audio file.", ex);
        }
    }

    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            logger.debug("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            m_bPlayCompleted = true;
            logger.debug( "Playback completed.");
        }
    }

    @Override
    public void processResponse( int nCode) {
        logger.warn( "processResponse() call for SoundStatementExecutor. Absurd statement!");
    }
    
    @Override
    public void processTimeOut() {
        logger.warn( "processTimeOut() call for SoundStatementExecutor. Absurd statement!");
    }
}
