/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors;

import hvv_executor.HVV_Executor;

/**
 *
 * @author yaroslav
 */
abstract public class AStatementExeThread implements Runnable {

    protected HVV_Executor theApp;
    
    public AStatementExeThread( HVV_Executor app) {
        theApp = app;
    }
    
    abstract public void processResponse( int nCode);
    abstract public void processTimeOut();
}
