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
public class HvChannelsEnabledRequest {

    private int m_nAdminRespondHvMask;
    public int GetRespond() { return m_nAdminRespondHvMask;}
        
    public class ProcessorRunnable extends AStatementExeRunnable {
    
        @Override
        public void processResponse( LinkedList lstResponseParcel) {
            //VERIFY length == 1
            logger.debug( theApp.GetCommE2A().m_strMarker + "processResponse(?) call for FinishedStepInformer.");
            int nCode = ( int) lstResponseParcel.get( 0);
            logger.debug( theApp.GetCommE2A().m_strMarker + "processResponse( " + nCode + ") call for FinishedStepInformer.");
            
            m_nAdminRespondHvMask = nCode;
            m_bGotAnswer = true;
        }

        @Override
        public void processTimeOut() {
            logger.warn( theApp.GetCommE2A().m_strMarker + "processTimeOut() call for FinishedStepInformer. Empty statement!");
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
                        logger.debug( theApp.GetCommE2A().m_strMarker + "FINISH_STEP; RESPONDED;");
                        bProcessing = false;
                        m_bContinue = false;

                    }

                    if( m_bTimeOut == true) {
                        logger.warn( theApp.GetCommE2A().m_strMarker + "FINISH_STEP; TIMEOUT;");
                        bProcessing = false;
                        
                        m_bContinue = false;  //!!!!!!!
                    }

                }
                else {
                    m_bGotAnswer = false;
                    m_bTimeOut = false;

                    LinkedList lst = new LinkedList();

                    //START_PROGRAM
                    lst.addLast( "REQ_HV_APPL");
                    
                    //ADDING COMMAND TO OUTPUT QUEUE WITH MENTION ABOUT ITSELF AS A PROCESSOR
                    CommandItem item = new CommandItem( this, lst);
                    theApp.GetCommE2A().GetRxTx().AddCommandToQueue( item);

                    logger.info( theApp.GetCommE2A().m_strMarker + "START_PROGRAM; QUEUED;");
                    bProcessing = true;  
                }    

                try {
                    sleep( 100);
                } catch (InterruptedException ex) {
                    logger.error( theApp.GetCommE2A().m_strMarker + "InterruptedException caught!", ex);
                }

            } while( m_bContinue);
        }
        
    }
    
    static Logger logger = Logger.getLogger(HvChannelsEnabledRequest.class);            
    
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
    
    
    public HvChannelsEnabledRequest( HVV_Executor app) {
        theApp = app;
    }
    
    public void StartThread() {
        m_pProcessor = new ProcessorRunnable();
        m_pThread = new Thread( new ProcessorRunnable());
        m_pThread.start();
    }
}
