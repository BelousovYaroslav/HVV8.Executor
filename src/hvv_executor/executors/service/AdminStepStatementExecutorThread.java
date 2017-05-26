/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.service;

import HVV_Communication.CommandItem;
import JProg.service.JProgServAdminStep;
import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import hvv_executor.comm.admin.to.FinishedStepInformer;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class AdminStepStatementExecutorThread extends AStatementExeThread {

    static Logger logger = Logger.getLogger( AdminStepStatementExecutorThread.class);            
    
    private final JProgServAdminStep m_ExeStatement;
    
    private boolean m_bSent;
    
    public AdminStepStatementExecutorThread( HVV_Executor app, JProgServAdminStep statement) {
        super( app);
        m_ExeStatement = statement;
    }

    @Override
    public void run() {
        m_bSent = false;
        
        FinishedStepInformer sender = new FinishedStepInformer( theApp, m_ExeStatement.GetFinishedStep());
        
        if( m_bSent == false) {
            sender.StartThread();
        }
            
        boolean bProcessing = true;
        while( bProcessing) {    
            if( sender.IsAnswerReceived() == true || sender.IsTimeOutHappens() == true)
                bProcessing = false;
            
            try {
                sleep( 10);
            } catch (InterruptedException ex) {
                logger.error( "InterruptedException caught!", ex);
            }
        }
    }
    
    @Override
    public void processResponse( int nCode) {
        logger.warn( "processResponse( " + nCode + ") call for AdminStepStatementExecutorThread. Should not get here?");
    }

    @Override
    public void processTimeOut() {
        logger.warn( "processTimeOut() call for AdminStepStatementExecutorThread.  Should not get here?");
    }
    
    
}
