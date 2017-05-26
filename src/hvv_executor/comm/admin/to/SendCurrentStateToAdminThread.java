/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.admin.to;

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
public class SendCurrentStateToAdminThread {

    public class ProcessorRunnable extends AStatementExeRunnable {

        @Override
        public void processResponse( LinkedList lstResponseParcel) {
            //VERIFY length == 1
            logger.debug( theApp.GetCommE2A().m_strMarker + "processResponse(?) call for SendCurrentStateToAdminThread.");
            int nCode = ( int) lstResponseParcel.get( 0);
            logger.debug( theApp.GetCommE2A().m_strMarker + "processResponse( " + nCode + ") call for SendCurrentStateToAdminThread.");
            m_bGotAnswer = true;
        }

        @Override
        public void processTimeOut() {
            logger.warn( theApp.GetCommE2A().m_strMarker + "processTimeOut() call for SendCurrentStateToAdminThread. Empty statement!");
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
                        logger.debug( theApp.GetCommE2A().m_strMarker + "EXECUTOR_STATE; RESPONDED;");
                        bProcessing = false;

                    }

                    if( m_bTimeOut == true) {
                        logger.warn( theApp.GetCommE2A().m_strMarker + "EXECUTOR_STATE; TIMEOUT;");
                        bProcessing = false;
                    }

                }
                else {
                    m_bGotAnswer = false;
                    m_bTimeOut = false;

                    if( theApp.GetCommE2A().GetState() != HVV_Communication.client.HVV_Comm_client.STATE_DISCONNECTED) {
                        LinkedList lst = new LinkedList();

                        //EXECUTOR_STATE
                        lst.addLast( "EXECUTOR_STATE");
                        switch( theApp.m_nRunningState) {
                            case HVV_Executor.RUNNING_STATE_STOP: lst.addLast( "STOP"); break;
                            case HVV_Executor.RUNNING_STATE_RUN: lst.addLast( "RUN"); break;
                            case HVV_Executor.RUNNING_STATE_PAUSE: lst.addLast( "PAUSE"); break;
                            default: lst.addLast( "UNKNOWN"); break;
                        }
                        lst.addLast( theApp.m_strCurrentProgram);
                        
                        //ADDING COMMAND TO OUTPUT QUEUE WITH MENTION ABOUT ITSELF AS PROCESSOR
                        CommandItem item = new CommandItem( this, lst);
                        theApp.GetCommE2A().GetRxTx().AddCommandToQueue( item);

                        logger.info( theApp.GetCommE2A().m_strMarker + "EXECUTOR_STATE; QUEUED;");
                        bProcessing = true;
                    }
                    else
                        logger.warn( "do nothing. not connected.");
                }    

                try {
                    sleep( 500);
                } catch (InterruptedException ex) {
                    logger.error( theApp.GetCommE2A().m_strMarker + "InterruptedException caught!", ex);
                }

            } while( m_bContinue);
        }
        
    }
    
    static Logger logger = Logger.getLogger(SendCurrentStateToAdminThread.class);            
    
    private boolean m_bGotAnswer;
    public boolean IsAnswerReceived() { return m_bGotAnswer; }
    
    private boolean m_bTimeOut;
    public boolean IsTimeOutHappens() { return m_bTimeOut; }

    private boolean m_bContinue;
    public void StopThread() {
        m_bContinue = false;
    }
    
    Thread m_pThread;
    ProcessorRunnable m_pProcessor;
    
    final HVV_Executor theApp;
    
    public SendCurrentStateToAdminThread( HVV_Executor app) {
        theApp = app;
    }
    
    public void StartThread() {
        m_pProcessor = new ProcessorRunnable();
        m_pThread = new Thread( new ProcessorRunnable());
        m_pThread.start();
    }
}
