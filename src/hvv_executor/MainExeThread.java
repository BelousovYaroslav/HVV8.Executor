/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor;

import hvv_executor.executors.AStatementExeThread;
import hvv_executor.executors.service.PauseStatementExecutorThread;
import hvv_executor.executors.service.SoundStatementExecutorThread;
import hvv_executor.executors.service.MessageStatementExecutorThread;

import JProg.JProgAStatement;
import JProg.hv.JProgHvChannels;
import JProg.hv.JProgHvFan;
import JProg.hv.JProgHvMainSwitcher;
import JProg.hv.JProgHvPreset;
import JProg.hv.JProgHvVibration;
import JProg.hv.JWrapHvSet;
import JProg.service.JProgServAdminStep;
import JProg.service.JProgServMessageStatement;
import JProg.service.JProgServPauseStatement;
import JProg.service.JProgServSoundStatement;
import JProg.vacuum.JProgVacuumClose;
import JProg.vacuum.JProgVacuumOpen;
import JProg.vacuum.JProgVacuumSet;
import JProg.vacuum.JProgVacuumTurnOff;
import JProg.vacuum.JProgVacuumTurnOn;
import JProg.vacuum.JProgVacuumWait;
import JProg.vacuum.JWrapVacSet;
import hvv_executor.executors.hv.HvChSetStatementExecutorThread;
import hvv_executor.executors.hv.HvSetStatementExecutorThread;
import hvv_executor.executors.service.AdminStepStatementExecutorThread;
import hvv_executor.executors.vac.VacSetStatementExecutorThread;
import hvv_executor.executors.vac.VacWaitStatementExecutorThread;
import java.awt.Color;

import static java.lang.Thread.sleep;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JLabel;
import org.apache.log4j.Logger;


/**
 *
 * @author yaroslav
 */
public class MainExeThread implements Runnable {
    HVV_Executor theApp;
    static Logger logger = Logger.getLogger( MainExeThread.class);
    
    private boolean m_bRunningThread;
    public void StopThread() { m_bRunningThread = false; }
    
    public MainExeThread( HVV_Executor app) {
        theApp = app;
        m_bRunningThread = false;
    }

    @Override
    public void run() {
        m_bRunningThread = true;
        logger.info( "Начинаем исполнять программу!");
        
        LinkedList lst  = new LinkedList( theApp.m_program.keySet());
        
        AStatementExeThread pCurrentExecutor;
        Thread pCurrentExecution = null;
        
        Iterator it = lst.iterator();
        int nLineNumberToExecute = -1;
        
        do {
            try {
                
                if( pCurrentExecution != null && pCurrentExecution.isAlive()) {
                    
                    logger.trace( "Идёт исполнение операции.. Ожидаем её завершения... isA=" + pCurrentExecution.isAlive());
                }
                else {
                    logger.debug( "Исполнение операции закончилось... Переходим к следующей");
                    
                    if( nLineNumberToExecute != -1) {
                        JLabel lbl = ( JLabel) theApp.m_pMainWnd.m_pnlProgram.m_mapLabels.get( nLineNumberToExecute);
                        lbl.setBackground( null);
                    }
                    
                    if( it.hasNext() == false) {
                        logger.info( "Программа закончилась! Стоп!");
                        theApp.m_pMainWnd.lblCurrentExecutableStatement.setText( "Программа закончилась.");
                        theApp.m_nRunningState = HVV_Executor.RUNNING_STATE_STOP;
                        break;
                    }
                    
                    nLineNumberToExecute = ( int) it.next();
                    logger.debug( "Строка к исполнению " + nLineNumberToExecute);
                    
                    JLabel lbl = ( JLabel) theApp.m_pMainWnd.m_pnlProgram.m_mapLabels.get( nLineNumberToExecute);
                    lbl.setBackground( new Color( 220, 200, 200));
                        
                    JProgAStatement statement = ( JProgAStatement) theApp.m_program.get( nLineNumberToExecute);
                    logger.debug( "Строка к исполнению '" + statement.GetAsString() + "'");
                    
                    theApp.m_pMainWnd.lblCurrentExecutableStatement.setText( nLineNumberToExecute + " " + statement.GetAsString());
                            
                    //highlight it in the main window
                    String strStatement = nLineNumberToExecute + " " + statement.GetAsString();                    
                    int nIndx = theApp.m_pMainWnd.lstModel.indexOf( strStatement);
                    if( nIndx != -1) {
                        int nWithFollowingItems = nIndx + 5;
                        if( nWithFollowingItems > theApp.m_pMainWnd.lstProgram.getModel().getSize())
                            nWithFollowingItems = theApp.m_pMainWnd.lstProgram.getModel().getSize();
                        //theApp.m_pMainWnd.lstProgram.ensureIndexIsVisible( nWithFollowingItems);
                        
                        //theApp.m_pMainWnd.lstProgram.setSelectedIndex( nIndx);
                    }
                    
                    //scroll it in labels window
                    if( theApp.m_program.size() > 11) {
                        LinkedList set = new LinkedList( theApp.m_pMainWnd.m_pnlProgram.m_mapLabels.keySet());
                        int nPosition = set.indexOf( nLineNumberToExecute) + 1;
                        if( nPosition > 6) {
                            theApp.m_pMainWnd.scrlBarProgram.setValue( ( nPosition - 6) * 30);
                            logger.info( "");
                            logger.info( "");
                            logger.info( "");
                            logger.info( "LINE: " + nLineNumberToExecute + "     N: " + nPosition + "     POS: " + ( nPosition - 6) * 30);
                            logger.info( "");
                            logger.info( "");
                            logger.info( "");
                        }
                        else
                            theApp.m_pMainWnd.scrlBarProgram.setValue( 0);
                    }
                    
                    
                    
                    switch( statement.getStatementType()) {
                        //********************************************************************************** SERVICE
                        case JProgAStatement.STATEMENT_TYPE_SERVICE_PAUSE:
                            pCurrentExecutor = new PauseStatementExecutorThread( theApp, ( JProgServPauseStatement) statement);
                        break;
                        case JProgAStatement.STATEMENT_TYPE_SERVICE_SOUND:
                            pCurrentExecutor = new SoundStatementExecutorThread( theApp, ( JProgServSoundStatement) statement);
                        break;
                        case JProgAStatement.STATEMENT_TYPE_SERVICE_MESSAGE:
                            pCurrentExecutor = new MessageStatementExecutorThread( theApp, ( JProgServMessageStatement) statement);
                        break;
                        case JProgAStatement.STATEMENT_TYPE_SERVICE_ADMIN_STEP:
                            pCurrentExecutor = new AdminStepStatementExecutorThread( theApp, ( JProgServAdminStep) statement);
                        break;
                        
                        //********************************************************************************** VACUUM
                        case JProgAStatement.STATEMENT_TYPE_VACUUM_TURN_ON:
                            pCurrentExecutor = new VacSetStatementExecutorThread( theApp,
                                    new JWrapVacSet( ( JProgVacuumTurnOn) statement));
                        break;
                        case JProgAStatement.STATEMENT_TYPE_VACUUM_TURN_OFF:
                            pCurrentExecutor = new VacSetStatementExecutorThread( theApp,
                                    new JWrapVacSet( ( JProgVacuumTurnOff) statement));
                        break;
                            
                        case JProgAStatement.STATEMENT_TYPE_VACUUM_OPEN:
                            pCurrentExecutor = new VacSetStatementExecutorThread( theApp,
                                    new JWrapVacSet( ( JProgVacuumOpen) statement));
                        break;
                        case JProgAStatement.STATEMENT_TYPE_VACUUM_CLOSE:
                            pCurrentExecutor = new VacSetStatementExecutorThread( theApp,
                                    new JWrapVacSet( ( JProgVacuumClose) statement));
                        break;
                        
                        case JProgAStatement.STATEMENT_TYPE_VACUUM_SET:
                            pCurrentExecutor = new VacSetStatementExecutorThread( theApp,
                                    new JWrapVacSet( ( JProgVacuumSet) statement));
                        break;
                                
                        case JProgAStatement.STATEMENT_TYPE_VACUUM_WAIT:
                            pCurrentExecutor = new VacWaitStatementExecutorThread( theApp,
                                    ( JProgVacuumWait) statement);
                        break;
                        
                        //********************************************************************************** HV
                        case JProgAStatement.STATEMENT_TYPE_HV_VIBRATON:
                            pCurrentExecutor = new HvSetStatementExecutorThread( theApp,
                                    new JWrapHvSet( ( JProgHvVibration) statement));
                        break;
                            
                        case JProgAStatement.STATEMENT_TYPE_HV_FAN:
                            pCurrentExecutor = new HvSetStatementExecutorThread( theApp,
                                    new JWrapHvSet( ( JProgHvFan) statement));
                        break;
                            
                        case JProgAStatement.STATEMENT_TYPE_HV_PRESET:
                            pCurrentExecutor = new HvSetStatementExecutorThread( theApp,
                                    new JWrapHvSet( ( JProgHvPreset) statement));
                        break;
                            
                        case JProgAStatement.STATEMENT_TYPE_HV_MAIN_SWITCHER:
                            pCurrentExecutor = new HvSetStatementExecutorThread( theApp,
                                    new JWrapHvSet( ( JProgHvMainSwitcher) statement));
                        break;
                        
                        case JProgAStatement.STATEMENT_TYPE_HV_CHANNELS:
                            pCurrentExecutor = new HvChSetStatementExecutorThread( theApp,
                                    ( JProgHvChannels) statement);
                        break;
                        //********************************************************************************** REST (ERROR)
                        default:
                            throw new UnsupportedOperationException( "Not implemented yet!");
                    }
                    
                    if( pCurrentExecutor != null) {
                        pCurrentExecution = new Thread( pCurrentExecutor);
                        pCurrentExecution.start();
                    }
                }
                
                
                sleep( 10);
                
            } catch (InterruptedException ex) {
                logger.fatal( "InterruptedExecption caught", ex);
            }
        } while( m_bRunningThread);
    }
}
