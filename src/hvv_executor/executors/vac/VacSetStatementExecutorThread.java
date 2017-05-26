/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.vac;

import JProg.hv.JWrapHvSet;
import JProg.vacuum.JWrapVacSet;
import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import hvv_executor.comm.CommandItem;
import java.util.LinkedList;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class VacSetStatementExecutorThread extends AStatementExeThread {

    static Logger logger = Logger.getLogger( VacSetStatementExecutorThread.class);            
    
    private final JWrapVacSet m_ExeStatement;
    
    private boolean m_bGotAnswer;
    private boolean m_bTimeOut;
    
    public VacSetStatementExecutorThread( HVV_Executor app, JWrapVacSet statement) {
        super( app);
        m_ExeStatement = statement;
    }

    @Override
    public void run() {
        m_bGotAnswer = false;
        m_bTimeOut = false;
        
        LinkedList lst = new LinkedList();
                
        
        switch( m_ExeStatement.GetObject()) {
            case "001":
                lst.addLast( "SET");
                lst.addLast( "nXSD15iR");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "002":
                lst.addLast( "SET");
                lst.addLast( "HiPace_300");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "003":
                lst.addLast( "SET");
                lst.addLast( "HiCube_80");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "08A":
                lst.addLast( "SET");
                lst.addLast( "valve_8a");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "08B":
                lst.addLast( "SET");
                lst.addLast( "valve_8b");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "08C":
                lst.addLast( "SET");
                lst.addLast( "valve_8c");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "09A":
                lst.addLast( "SET");
                lst.addLast( "valve_9a");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "09B":
                lst.addLast( "SET");
                lst.addLast( "valve_9b");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "09C":
                lst.addLast( "SET");
                lst.addLast( "valve_9c");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "09D":
                lst.addLast( "SET");
                lst.addLast( "valve_9d");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            
            case "10A":
                lst.addLast( "SET");
                lst.addLast( "valve_10a");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "10B":
                lst.addLast( "SET");
                lst.addLast( "valve_10b");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "10C":
                lst.addLast( "SET");
                lst.addLast( "valve_10c");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "11A":
                lst.addLast( "SET");
                lst.addLast( "valve_11A");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "11B":
                lst.addLast( "SET");
                lst.addLast( "valve_11a");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "012":
                lst.addLast( "SET");
                lst.addLast( "valve_12");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "013":
                lst.addLast( "SET");
                lst.addLast( "valve_13");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;    
            
            case "14A":
                lst.addLast( "SET");
                lst.addLast( "valve_14a");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "14B":
                lst.addLast( "SET");
                lst.addLast( "valve_14b");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "14C":
                lst.addLast( "SET");
                lst.addLast( "valve_14c");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "14D":
                lst.addLast( "SET");
                lst.addLast( "valve_14d");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "14E":
                lst.addLast( "SET");
                lst.addLast( "valve_14e");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            
            case "15A":
                lst.addLast( "SET");
                lst.addLast( "valve_15a");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "15B":
                lst.addLast( "SET");
                lst.addLast( "valve_15b");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "15C":
                lst.addLast( "SET");
                lst.addLast( "valve_15c");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
                
            case "16A":
                lst.addLast( "SET");
                if( m_ExeStatement.GetParam().equals( "01")) lst.addLast( "valve_16a");
                else if( m_ExeStatement.GetParam().equals( "02")) lst.addLast( "Ctrl_16a");
                else {
                    logger.error( "Для объекта 16А параметр не 1 (состояние) и не 2 (уставка)");
                }
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "16B":
                lst.addLast( "SET");
                if( m_ExeStatement.GetParam().equals( "01")) lst.addLast( "valve_16b");
                else if( m_ExeStatement.GetParam().equals( "02")) lst.addLast( "Ctrl_16b");
                else {
                    logger.error( "Для объекта 16B параметр не 1 (состояние) и не 2 (уставка)");
                }
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "16C":
                lst.addLast( "SET");
                if( m_ExeStatement.GetParam().equals( "01")) lst.addLast( "valve_16c");
                else if( m_ExeStatement.GetParam().equals( "02")) lst.addLast( "Ctrl_16c");
                else {
                    logger.error( "Для объекта 16C параметр не 1 (уставка) и не 2 (состояние)");
                }
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            
            case "17A":
                lst.addLast( "SET");
                if( m_ExeStatement.GetParam().equals( "01")) lst.addLast( "valve_17a");
                else if( m_ExeStatement.GetParam().equals( "02")) lst.addLast( "Ctrl_17a");
                else {
                    logger.error( "Для объекта 17А параметр не 1 (уставка) и не 2 (состояние)");
                }
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "17B":
                lst.addLast( "SET");
                if( m_ExeStatement.GetParam().equals( "01")) lst.addLast( "valve_17b");
                else if( m_ExeStatement.GetParam().equals( "02")) lst.addLast( "Ctrl_17b");
                else {
                    logger.error( "Для объекта 17B параметр не 1 (уставка) и не 2 (состояние)");
                }
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            
            case "018":
                lst.addLast( "SET");
                if( m_ExeStatement.GetParam().equals( "01")) lst.addLast( "valve_18");
                else if( m_ExeStatement.GetParam().equals( "02")) lst.addLast( "Ctrl_18");
                else {
                    logger.error( "Для объекта 18 параметр не 1 (уставка) и не 2 (состояние)");
                }
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
                        
            case "23A":
                lst.addLast( "SET");
                lst.addLast( "valve_23a");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
            case "23B":
                lst.addLast( "SET");
                lst.addLast( "valve_23b");
                lst.addLast( "" + m_ExeStatement.GetValue());
            break;
                
            default:
                logger.error("Unknown object");
        } 
        
        
                
        CommandItem item = new CommandItem( this, lst);
        theApp.GetCommVac().GetRxTx().AddCommandToQueue( item);
                
        boolean bProcessing = true;
        while( bProcessing) {
            
            //logger.trace( "Waiting for response or timeout");
            
            if( m_bGotAnswer == true || m_bTimeOut == true)
                bProcessing = false;
            try {
                Thread.sleep( 5);
            } catch (InterruptedException ex) {
                logger.warn( "InterruptedException caught!", ex);
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
