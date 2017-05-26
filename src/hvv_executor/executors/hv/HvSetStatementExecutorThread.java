/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.hv;

import JProg.hv.JWrapHvSet;
import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import hvv_executor.comm.CommandItem;
import hvv_timeouts.HVV_TimeoutsManager;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HvSetStatementExecutorThread extends AStatementExeThread {

    static Logger logger = Logger.getLogger( HvSetStatementExecutorThread.class);            
    
    private final JWrapHvSet m_ExeStatement;
    
    private boolean m_bSent;
    private boolean m_bGotAnswer;
    private boolean m_bTimeOut;
    
    public HvSetStatementExecutorThread( HVV_Executor app, JWrapHvSet statement) {
        super( app);
        m_ExeStatement = statement;
    }

    @Override
    public void run() {
        m_bSent = false;
        m_bGotAnswer = false;
        m_bTimeOut = false;
        
        if( m_bSent == false) {
            LinkedList lst = new LinkedList();

            lst.addLast( "SET");
            lst.addLast( m_ExeStatement.GetObject());
            lst.addLast( m_ExeStatement.GetValue());

            CommandItem item = new CommandItem( this, lst);
            theApp.GetCommHv().GetRxTx().AddCommandToQueue( item);
        }
            
        boolean bProcessing = true;
        
        long lTimeOutId = HVV_TimeoutsManager.getInstance().StartTimeout( 5000);
        int nOwnTimeouts = 0;
        
        while( bProcessing) {    
            if( m_bGotAnswer == true || m_bTimeOut == true) {
                HVV_TimeoutsManager.getInstance().RemoveId( lTimeOutId);
                bProcessing = false;
                break;
            }
            
            if( HVV_TimeoutsManager.getInstance().CheckTimeout( lTimeOutId) == true) {
                HVV_TimeoutsManager.getInstance().RemoveId( lTimeOutId);
                if( ++nOwnTimeouts == 5) {
                    logger.warn( "OWN TIMEOUT #" + nOwnTimeouts + " CAUGHT!");
                    logger.warn( "Trying to send command again!");
                    
                    
                    LinkedList lst = new LinkedList();

                    lst.addLast( "SET");
                    lst.addLast( m_ExeStatement.GetObject());
                    lst.addLast( m_ExeStatement.GetValue());

                    CommandItem item = new CommandItem( this, lst);
                    theApp.GetCommHv().GetRxTx().AddCommandToQueue( item);
                    
                    lTimeOutId = HVV_TimeoutsManager.getInstance().StartTimeout( 5000);
                }
                else {
                    logger.error( "Было получено 5 собственных 5sec-таймаутов.");
                    logger.error( "То есть, на отправляемые команды я не получаю ни ответа ни таймаута от объекта связи с HV модулем.");
                    logger.error( "Обратите внимание на эту ситуацию!");
                    logger.error( "После нажатия ОК попытки отправить команду будут продолжены.");
                    
                    HVV_Executor.MessageBoxInfo( "Было получено 5 собственных 5sec-таймаутов.<br>То есть, на отправляемые команды я не получаю ни ответа ни таймаута от объекта связи с HV модулем.<br>Обратите внимание на эту ситуацию!<br>После нажатия ОК попытки отправить команду будут продолжены.", "Проблема связи с HV модулем!");
                    
                    LinkedList lst = new LinkedList();

                    lst.addLast( "SET");
                    lst.addLast( m_ExeStatement.GetObject());
                    lst.addLast( m_ExeStatement.GetValue());

                    CommandItem item = new CommandItem( this, lst);
                    theApp.GetCommHv().GetRxTx().AddCommandToQueue( item);
                    
                    lTimeOutId = HVV_TimeoutsManager.getInstance().StartTimeout( 5000);
                }
            }
            
            try {
                sleep( 10);
            } catch (InterruptedException ex) {
                logger.error( "InterruptedException caught!", ex);
            }
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
