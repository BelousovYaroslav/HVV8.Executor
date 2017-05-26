/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.service;

import hvv_executor.executors.AStatementExeThread;
import hvv_executor.DlgPauseExecution;
import hvv_executor.HVV_Executor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import static java.lang.Thread.sleep;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class PauseStatementExecutorThread extends AStatementExeThread{

    JProg.service.JProgServPauseStatement m_ExeStatement;
    static Logger logger = Logger.getLogger( PauseStatementExecutorThread.class);            
    
    public PauseStatementExecutorThread( HVV_Executor app, JProg.service.JProgServPauseStatement statement) {
        super( app);
        m_ExeStatement = statement;
    }

    @Override
    public void run() {
        long lCurrent = System.currentTimeMillis();
        long lFinish = lCurrent + m_ExeStatement.GetDuration();
        
        DlgPauseExecution dlg = new DlgPauseExecution( null, false);
        dlg.setTitle( "Исполнение паузы");
    
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
    
        dlg.setVisible( true);
        dlg.setLocation( rect.width - dlg.getWidth(), 0);
        
        
        do {
            lCurrent = System.currentTimeMillis();
            
            int nHours = ( int)   ( ( lFinish - lCurrent) / 1000. / 60. / 60.);
            int nMinutes = ( int) ( ( lFinish - lCurrent) / 1000. / 60.) % 60;
            int nSeconds = ( int) ( ( ( lFinish - lCurrent) / 1000.) % 60);
            int nMilliSeconds = ( int) ( ( lFinish - lCurrent) % 1000.);
            
            dlg.lblCountDown.setText( String.format( "%02d:%02d:%02d:%03d", nHours, nMinutes, nSeconds, nMilliSeconds));
            
            try {
                sleep( 10);
            } catch (InterruptedException ex) {
                logger.error( "InterruptedException Caught!", ex);
            }
        } while( lCurrent < lFinish);
        
        dlg.dispose();
    }

    @Override
    public void processResponse( int nCode) {
        logger.warn( "processResponse() call for PauseStatementExecutor. Absurd statement!");
    }
    
    @Override
    public void processTimeOut() {
        logger.warn( "processTimeOut() call for PauseStatementExecutor. Absurd statement!");
    }
}
