/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.hv;

import hvv_executor.executors.hv.HvPingStatementExecutorThread;
import hvv_executor.HVV_Executor;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HVV_Communication_hv implements Runnable {
    HVV_Executor theApp;
    
    TwoWaySocketServerCommHv m_rxtx;
    public TwoWaySocketServerCommHv GetRxTx() { return m_rxtx; }
            
    static Logger logger = Logger.getLogger(HVV_Communication_hv.class);
    
    private int m_nState;
    public int GetState() { return m_nState; }
    
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTED_OK = 1;
    public static final int STATE_CONNECTED_PROBLEMS = 2;
    public static final int STATE_CONNECTED_IDLE = 3;
    
    public Thread m_Thread;
    boolean m_bContinue;
    
    public HVV_Communication_hv( HVV_Executor app) {
        theApp = app;
        
        m_rxtx = new TwoWaySocketServerCommHv( app);
        
        m_Thread = null;
        
        m_nState = STATE_DISCONNECTED;
    }
    
    public void start() {
        if( m_Thread != null && m_Thread.isAlive() == true)
            return;
        
        m_Thread = new Thread( this);
        m_Thread.start();
    }
    
    public void stop() {
        if( m_Thread != null) {
            try {
                m_bContinue = false;
                m_Thread.join();
                
                m_nState = STATE_DISCONNECTED;
                
                m_Thread = null;
            } catch( InterruptedException ex) {
                logger.warn( "InterruptedException caught!", ex);
            }
        }
    }

    @Override
    public void run() {
        m_bContinue = true;
        
        HvPingStatementExecutorThread ping = new HvPingStatementExecutorThread( theApp);
        Thread pingThread = new Thread( ping);
        do {
            logger.info( "HVV_connector_hv thread cycle in...");
            if( m_rxtx.IsConnected()) {
                //мы подсоединены - всё ок
                m_nState = STATE_CONNECTED_OK;
                
                if( m_rxtx != null &&
                            m_rxtx.thrInput != null && m_rxtx.thrOutput != null &&
                            m_rxtx.thrInput.isAlive() && m_rxtx.thrOutput.isAlive()) {
                    logger.info( "HVV_connector_hv thread is alive, connected, and both reader and writer are running!");
                    
                    if( pingThread.isAlive()) {
                        logger.debug( "pinging hv");
                    }
                    else {
                        if( ping.GetPingOk()) {
                            logger.debug( "ping hv ok");
                            m_nState = STATE_CONNECTED_OK;
                        }
                        if( ping.GetPingTimeOut()) {
                            logger.debug( "ping hv timeout");
                            m_nState = STATE_CONNECTED_IDLE;
                        }
                        logger.debug( "Restarting hv ping");
                        pingThread = new Thread( ping);
                        pingThread.start();
                    }
                    
                    try {
                        sleep( 1000);
                    } catch (InterruptedException ex) {
                        logger.warn( "InterruptedException caught!", ex);
                    }
                    
                    /*
                    if( m_rxtxVac.crclBuffer.isAnswerReady() > 0) {
                        
                    }*/
                            
                }
                else {
                    logger.info( "Connection broken! Disconnecting!");
                    
                    if( pingThread != null && pingThread.isAlive()) {
                        pingThread.interrupt();
                    }
                    
                    try {
                        m_rxtx.disconnect();
                    } catch( Exception ex) {
                        logger.warn( "Exception caught!", ex);
                    }
                    m_nState = STATE_DISCONNECTED;
                }
            }
            else {
                //мы не подсоединены... подсоединяемся
                m_nState = STATE_DISCONNECTED;
                try {
                    m_rxtx.connect();
                } catch( Exception ex) {
                    logger.warn( "Exception caught!", ex);
                }
            
                if( m_rxtx.IsConnected() == false) {
                    logger.warn( "Попытка соединиться неуспешна.");
                    try {
                        sleep( 5000);
                    } catch (InterruptedException ex) {
                        logger.warn( "InterruptedException caught!", ex);
                    }
                }
            }
            
        } while( m_bContinue);
        
        try {
            m_rxtx.disconnect();
        } catch( Exception ex) {
            logger.warn( "Exception caught!", ex);
        }
    }
}
