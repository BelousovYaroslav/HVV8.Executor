/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.service;

import JProg.service.JProgServMessageStatement;
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
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.dom4j.Element;

/**
 *
 * @author yaroslav
 */
public class MessageStatementExecutorThread extends AStatementExeThread implements LineListener {

    @Override
    public void processResponse(int nCode) {
        logger.warn( "processResponse() call for MessageStatementExecutor. Absurd statement!");
    }

    @Override
    public void processTimeOut() {
        logger.warn( "processTimeOut() call for MessageStatementExecutor. Absurd statement!");
    }

    class ReminderThread implements Runnable, LineListener {
        public boolean m_bContinue;
        private String m_strWavFile;
        private int m_nPeriod;
        public boolean m_bPlayCompleted;
        
        public ReminderThread( String str, int n) {
            m_strWavFile = str;
            m_nPeriod = n;
        }
        
        @Override
        public void run() {
            m_bContinue = true;
            
            
            String gongFile = m_ExeStatement.GetReminderWav();
            if( gongFile == null || gongFile.isEmpty()) {
                return;
            }
            
            File audioFile = new File( gongFile);
            do {
                
                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream( audioFile);
                    AudioFormat format = audioStream.getFormat();
            
                    DataLine.Info info = new DataLine.Info( Clip.class, format);
            
                    Clip audioClip = ( Clip) AudioSystem.getLine( info);
                    audioClip.addLineListener( this);
                    audioClip.open( audioStream);
                
                    for( int i=0; i<m_nPeriod; i++) {
                        Thread.sleep( 1000);
                        if( m_bContinue == false)
                            break;
                    }
                    
                    if( m_bContinue == false)
                        break;
                    
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
                
                }
                catch( Exception ex) {
                    logger.error( "Caught Exception", ex);
                } 
            } while( m_bContinue);
        }

        @Override
        public void update(LineEvent event) {
            LineEvent.Type type = event.getType();
         
            if (type == LineEvent.Type.START) {
                //System.out.println("Playback started.");
            } else if (type == LineEvent.Type.STOP) {
                m_bPlayCompleted = true;
                //System.out.println("Playback completed.");
            }
        }
        
    }
    
    JProgServMessageStatement m_ExeStatement;
    static Logger logger = Logger.getLogger( FrmMainWindow.class);            
    
    public MessageStatementExecutorThread( HVV_Executor app, JProgServMessageStatement statement) {
        super( app);
        m_ExeStatement = statement;
    }

    public boolean m_bPlayCompleted;
    @Override
    public void run() {
        String gongFile = m_ExeStatement.GetSoundWavFile();
        try {
            if( gongFile != null && !gongFile.isEmpty()) {
                File audioFile = new File( gongFile);
            
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
            }
            
            ReminderThread thrReminder = null;
            Thread thr = null;
            if( m_ExeStatement.GetReminderWav() != null && m_ExeStatement.GetReminderWav().isEmpty() == false &&
                    m_ExeStatement.GetReminderPeriod() != 0) {
                thrReminder = new ReminderThread(
                        m_ExeStatement.GetReminderWav(),
                        m_ExeStatement.GetReminderPeriod());
                thr = new Thread( thrReminder);
                thr.start();
            }
            
            
            JOptionPane.showMessageDialog( null, m_ExeStatement.GetMessage(), "Программное сообщение", JOptionPane.INFORMATION_MESSAGE);
            
            
            if( thrReminder != null) 
                thrReminder.m_bContinue = false;
            
            if( thr != null)
                thr.join();
            
        } catch( UnsupportedAudioFileException ex) {
            logger.error( "The specified audio file is not supported.", ex);
        } catch( LineUnavailableException ex) {
            logger.error( "Audio line for playing back is unavailable.", ex);
        } catch( IOException ex) {
            logger.error( "Error playing the audio file.", ex);
        } catch( InterruptedException ex) {
            logger.error( "InterruptedException on join()", ex);
        }
        
    }
    
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            //System.out.println("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            m_bPlayCompleted = true;
            //System.out.println("Playback completed.");
        }
    }
    
}
