/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.hv;

import JProg.JProgAStatement;
import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import org.dom4j.Element;

/**
 *
 * @author yaroslav
 */
public class HvExecutableStatementExecutor extends AStatementExeThread {

    JProgAStatement m_StatementToExecute;
    
    public HvExecutableStatementExecutor( HVV_Executor app, JProg.JProgAStatement statement) {
        super( app);
    }

    private boolean m_bSent;
    private boolean m_bGotAnswer;
        
    @Override
    public void run() {
        m_bSent = false;
        m_bGotAnswer = false;
                
        boolean bProcessing = true;
        while( bProcessing) {
            if( m_bSent == false) {
                //theApp.m_HvInteractor.SendCommand();
            }
            
            if( m_bGotAnswer == true)
                bProcessing = false;
        }
    }

    @Override
    public void processResponse( int nCode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*
        if( el == "CORRECT ANSWER")
            m_bGotAnswer = true;
        */
    }
    
    @Override
    public void processTimeOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*
        if( el == "CORRECT ANSWER")
            m_bGotAnswer = true;
        */
    }
    
}
