/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.poller;

import JProg.hv.JWrapHvSet;
import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import hvv_executor.comm.CommandItem;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class PollerGetStatementExecutorThread extends AStatementExeThread {

    static Logger logger = Logger.getLogger(PollerGetStatementExecutorThread.class);            
    
    private final String m_strReqObject;
    
    private boolean m_bSent;
    private boolean m_bGotAnswer;
    private boolean m_bTimeOut;
    
    public PollerGetStatementExecutorThread( HVV_Executor app, String strObject) {
        super( app);
        m_strReqObject = strObject;
    }

    @Override
    public void run() {
        m_bSent = false;
        m_bGotAnswer = false;
        m_bTimeOut = false;
        
        boolean bProcessing = true;
        while( bProcessing) {
            if( m_bSent == false) {
                LinkedList lst = new LinkedList();
                
                lst.addLast( "GET");
                lst.addLast( m_strReqObject);
                
                CommandItem item = new CommandItem( this, null);
                theApp.GetCommHv().GetRxTx().AddCommandToQueue( item);
                
                m_bSent = true;
            }
            
            if( m_bGotAnswer == true || m_bTimeOut == true)
                bProcessing = false;
        }
    }
    
    @Override
    public void processResponse( int nCode) {
        logger.warn( "processResponse( " + nCode + ") call for HvSetStatementExecutorThread.");
        m_bGotAnswer = true;
    }

    @Override
    public void processTimeOut() {
        logger.warn( "processTimeOut() call for HvSetStatementExecutorThread. Absurd statement!");
        m_bTimeOut = true;
    }
    
    
}
