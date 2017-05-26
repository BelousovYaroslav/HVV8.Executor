/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.poller;

import HVV_Communication.CommandItem;
import HVV_Communication.executors.AStatementExeRunnable;
import hvv_executor.HVV_Executor;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class GetExecutor {

    public class ProcessorRunnable extends AStatementExeRunnable {

        @Override
        public void processResponse( LinkedList lstResponseParcel) {
            int nCode = ( int) lstResponseParcel.get( 0);
            logger.debug( theApp.GetCommE2P().m_strMarker + "processResponse( " + nCode + ") call for GetExecutor.");

            if( nCode == 0) {
                if( lstResponseParcel.size() == 2) {
                    m_dblResult = ( double) lstResponseParcel.get( 1);
                    logger.debug( "GET-command is processed successfully. got value=" + m_dblResult);
                }
                else
                    logger.error( theApp.GetCommE2P().m_strMarker + "RetCode for GET-command = 0, but response length != 2! STRANGE!");
            }
            else if( nCode == 100) {
                logger.info( theApp.GetCommE2P().m_strMarker + "Server want to exit! Disconnecting!");

                try {
                    /*
                    m_pCommThread.m_bStopRequested = true;
                    */
                    LinkedList lstQuitCmd = new LinkedList();
                    lstQuitCmd.addLast( "QUIT");
                    CommandItem quitItem = new CommandItem( null, lstQuitCmd);
                    theApp.GetCommE2P().GetRxTx().AddCommandToQueue( quitItem);

                    theApp.GetCommE2P().m_bServerStopRequested = true;
                } catch( Exception ex) {
                    logger.error( theApp.GetCommE2P().m_strMarker + "Exception caught!", ex);
                }
            }
            else {
                logger.warn( theApp.GetCommE2P().m_strMarker + "Got problem answer! Don't processing! (TODO)");
            }
            m_bGotAnswer = true;
        }

        @Override
        public void processTimeOut() {
            logger.warn( theApp.GetCommE2P().m_strMarker + "processTimeOut() call for GetExecutor. Empty statement!");
            m_bTimeOut = true;
        }

        @Override
        public void run() {
            m_bContinue = true;
            boolean bProcessing = false;

            m_bGotAnswer = false;
            m_bTimeOut = false;

            do {

                if( bProcessing == true) {
                    if( m_bGotAnswer == true) {
                        logger.debug( theApp.GetCommE2P().m_strMarker + "GET; RESPONDED;");
                        bProcessing = false;
                        m_bContinue = false;

                    }

                    if( m_bTimeOut == true) {
                        logger.warn( theApp.GetCommE2P().m_strMarker + "GET; TIMEOUT;");
                        bProcessing = false;
                        
                        m_bContinue = false;  //!!!!!!!
                    }

                }
                else {
                    m_bGotAnswer = false;
                    m_bTimeOut = false;

                    LinkedList lst = new LinkedList();

                    //START_PROGRAM
                    lst.addLast( "GET");
                    lst.addLast(m_strParameter);
                    
                    //ADDING COMMAND TO OUTPUT QUEUE WITH MENTION ABOUT ITSELF AS PROCESSOR
                    CommandItem item = new CommandItem( this, lst);
                    theApp.GetCommE2P().GetRxTx().AddCommandToQueue( item);

                    logger.info( theApp.GetCommE2P().m_strMarker + "GET; QUEUED;");
                    bProcessing = true;  
                }    

                try {
                    sleep( 100);
                } catch (InterruptedException ex) {
                    logger.error( theApp.GetCommE2P().m_strMarker + "InterruptedException caught!", ex);
                }

            } while( m_bContinue);
        }
        
    }
    
    static Logger logger = Logger.getLogger(GetExecutor.class);            
    
    private boolean m_bGotAnswer;
    public boolean IsAnswerReceived() { return m_bGotAnswer; }
    
    private boolean m_bTimeOut;
    public boolean IsTimeOutHappens() { return m_bTimeOut; }

    private boolean m_bContinue;
    public void StopThread() {
        m_bContinue = false;
    }
    
    private double m_dblResult;
    public double GetResult() { return m_dblResult; }
    
    Thread m_pThread;
    ProcessorRunnable m_pProcessor;
    
    final HVV_Executor theApp;
    final String m_strParameter;
    
    public GetExecutor( HVV_Executor app, String strProgram) {
        theApp = app;
        m_strParameter = strProgram;
    }
    
    public void StartThread() {
        m_pProcessor = new ProcessorRunnable();
        m_pThread = new Thread( new ProcessorRunnable());
        m_pThread.start();
    }
}
