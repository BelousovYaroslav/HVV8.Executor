/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.vac;

import hvv_executor.executors.vac.VacPingStatementExecutorThread;
import hvv_executor.HVV_Executor;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HVV_Communication_vac implements Runnable {
    HVV_Executor theApp;
    TwoWaySocketServerCommVac m_rxtx;
    public TwoWaySocketServerCommVac GetRxTx() { return m_rxtx; }
    
    static Logger logger = Logger.getLogger(HVV_Communication_vac.class);
    
    private int m_nState;
    public int GetState() { return m_nState; }
    
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTED_OK = 1;
    public static final int STATE_CONNECTED_PROBLEMS = 2;
    public static final int STATE_CONNECTED_IDLE = 3;
    
    public Thread m_Thread;
    boolean m_bContinue;
    
    private int m_nReconnections;
    public int GetReconnections() { return m_nReconnections;}
    
    private int m_nPingTimeouts;
    public int GetPingTimeouts() { return m_nPingTimeouts;}
    
    public HVV_Communication_vac( HVV_Executor app) {
        theApp = app;
        m_rxtx = new TwoWaySocketServerCommVac( app);
        m_Thread = null;
        m_nState = STATE_DISCONNECTED;
        m_nReconnections = -1;
        m_nPingTimeouts = 0;
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
        
        VacPingStatementExecutorThread ping = new VacPingStatementExecutorThread( theApp);
        Thread pingThread = new Thread( ping);
        do {
            logger.info( "HVV_Communication_vac thread cycle in...");
            if( m_rxtx.IsConnected()) {
                //мы подсоединены - всё ок
                if( m_rxtx != null &&
                            m_rxtx.thrInput != null && m_rxtx.thrOutput != null &&
                            m_rxtx.thrInput.isAlive() && m_rxtx.thrOutput.isAlive()) {
                    logger.info( "HVV_Communication_vac thread is alive, connected, and both reader and writer are running!");
                    
                    if( pingThread.isAlive()) {
                        logger.debug( "pinging vac... TimeOutThread.ia=" + m_rxtx.GetTimeoutThread().isAlive());
                    }
                    else {
                        if( ping.GetPingOk()) {
                            m_nPingTimeouts = 0;
                            logger.debug( "ping vac ok");
                            m_nState = STATE_CONNECTED_OK;
                        }
                        
                        if( ping.GetPingTimeOut()) {
                            logger.warn( "ping vac timeout");
                            m_nPingTimeouts++;
                            
                            if( m_nPingTimeouts > 10) {
                                //IDLE?
                                m_nState = STATE_CONNECTED_IDLE;
                            }
                            
                            if( m_nPingTimeouts > 20) {
                                //BREAKUP
                                try {
                                    m_rxtx.disconnect();
                                } catch( Exception ex) {
                                    logger.warn( "Exception caught!", ex);
                                }
                                m_nState = STATE_DISCONNECTED;
                                
                                try {
                                    Thread.sleep( 1000);
                                } catch (InterruptedException ex) {
                                    logger.warn( "InterruptedException caught!", ex);
                                }
                                
                                continue;
                            }
                            
                        }
                        
                        logger.debug( "Restarting vac ping");
                        pingThread = new Thread( ping);
                        pingThread.start();
                    }
                
                    try {
                        Thread.sleep( 1000);
                    } catch (InterruptedException ex) {
                        logger.warn( "InterruptedException caught!", ex);
                    }
                    
                    /*
                    if( m_rxtxVac.crclBuffer.isAnswerReady() > 0) {
                        
                    }*/
                            
                }
                else {
                    logger.info( "Connection broken! Disconnecting!");
                    m_nState = STATE_CONNECTED_PROBLEMS;
                    if( pingThread.isAlive()) {
                        ping.m_bStopOnFail = true;
                        try {
                            pingThread.join();
                        } catch (InterruptedException ex) {
                            logger.warn( "InterruptedException caught!", ex);
                        }
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
                m_nReconnections++;
                try {
                    m_rxtx.connect();
                } catch( Exception ex) {
                    logger.warn( "Exception caught!", ex);
                }
            
                if( m_rxtx.IsConnected() == false)
                    logger.warn( "Попытка соединиться неуспешна.");
                
                try {
                    Thread.sleep( 5000);
                } catch (InterruptedException ex) {
                    logger.warn( "InterruptedException caught!", ex);
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
