/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.hv;

import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import org.dom4j.Element;

/**
 *
 * @author yaroslav
 */
public class HvConditionalStatementExecutor extends AStatementExeThread{

    private boolean m_bSent;
    private boolean m_bGotAnswer;
    
    public HvConditionalStatementExecutor( HVV_Executor app) {
        super( app);
    }

    @Override
    public void run() {
        m_bSent = false;
        m_bGotAnswer = false;
                
        boolean bProcessing = true;
        while( bProcessing) {
            if( m_bSent == false) {
                /*
                theApp.m_PollerInteractor.SendCommand();
                */
            }
            
            if( m_bGotAnswer == true) {
                bProcessing = false;
                
                /*
                MAIN IF EXECUTION
                */
            }
        }
    }

    @Override
    public void processResponse( int nCode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processTimeOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
