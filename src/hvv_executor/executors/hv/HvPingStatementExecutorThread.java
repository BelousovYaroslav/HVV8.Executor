/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.hv;

import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import hvv_executor.comm.CommandItem;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HvPingStatementExecutorThread extends AStatementExeThread {

    static Logger logger = Logger.getLogger(HvPingStatementExecutorThread.class);            
    
    private boolean m_bGotAnswer;
    public boolean GetPingOk() { return m_bGotAnswer; }
    
    private boolean m_bTimeOut;
    public boolean GetPingTimeOut() { return m_bTimeOut; }
    
    public HvPingStatementExecutorThread( HVV_Executor app) {
        super( app);
    }

    @Override
    public void run() {
        m_bGotAnswer = false;
        m_bTimeOut = false;
        
        boolean bProcessing = true;
        
        LinkedList lst = new LinkedList();
                
        lst.addLast( "PING");
                
        CommandItem item = new CommandItem( this, lst);
        theApp.GetCommHv().GetRxTx().AddCommandToQueue( item);
        
        while( bProcessing) {    
            if( m_bGotAnswer == true || m_bTimeOut == true)
                bProcessing = false;
            
            try {
                sleep( 100);
            } catch (InterruptedException ex) {
                logger.error( "InterruptedException caught!", ex);
            }
        }
    }
    
    @Override
    public void processResponse( int nCode) {
        logger.warn( "processResponse( " + nCode + ") call for HvPingStatementExecutorThread.");
        m_bGotAnswer = true;
    }

    @Override
    public void processTimeOut() {
        logger.warn( "processTimeOut() call for HvPingStatementExecutorThread. Absurd statement!");
        m_bTimeOut = true;
    }
    
    
}
