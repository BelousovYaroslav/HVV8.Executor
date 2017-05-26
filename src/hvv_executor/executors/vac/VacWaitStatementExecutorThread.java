/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.vac;

import JProg.vacuum.JProgVacuumWait;
import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import hvv_executor.comm.CommandItem;
import hvv_executor.DlgVacWaitExecution;
import hvv_resources.HVV_Resources;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class VacWaitStatementExecutorThread extends AStatementExeThread {

    static Logger logger = Logger.getLogger( VacWaitStatementExecutorThread.class);            
    
    private final JProgVacuumWait m_ExeStatement;
    
    private int m_nGotAnswer;
    private double m_dblValue;
    private boolean m_bTimeOut;
    private DlgVacWaitExecution dlg;
    
    public VacWaitStatementExecutorThread( HVV_Executor app, JProg.vacuum.JProgVacuumWait statement) {
        super( app);
        m_ExeStatement = statement;
    }

    @Override
    public void run() {
        LinkedList lst = new LinkedList();                
        lst.addLast( "GET");
        lst.addLast( m_ExeStatement.GetOperableDevice().getID() + "." + m_ExeStatement.getExpectedParam());

        
        dlg = new DlgVacWaitExecution( null, false);
        dlg.setTitle( "Ожидание параметра");
    
        String strParamName = m_ExeStatement.GetOperableDevice().getID() + ".";
        strParamName += m_ExeStatement.getExpectedParam() + ".";
        strParamName += m_ExeStatement.GetOperableDevice().m_strName + ".";
        strParamName += m_ExeStatement.GetOperableDevice().m_mapParameters.get( m_ExeStatement.getExpectedParam());
        
        dlg.lblParamName.setText( strParamName);
        dlg.lblParamValueExpected.setText( "" + m_ExeStatement.getExpected());
        switch( m_ExeStatement.getApproximation()) {
            case JProgVacuumWait.VAC_WAIT_APPROACH_FROM_DOWNSIDE:
                dlg.lblParamValueExpected.setIcon( HVV_Resources.getInstance().getIconUp());
            break;
            case JProgVacuumWait.VAC_WAIT_APPROACH_FROM_UPSIDE:
                dlg.lblParamValueExpected.setIcon( HVV_Resources.getInstance().getIconDown());
            break;
            case JProgVacuumWait.VAC_WAIT_APPROACH_FROM_NEVERMIND:
                dlg.lblParamValueExpected.setIcon( HVV_Resources.getInstance().getIconMinus());
            break;
            default:
                dlg.lblParamValueExpected.setIcon( null);
        }
        dlg.lblParamValueError.setText( "" + m_ExeStatement.getError());
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
    
        dlg.setLocation( rect.width - dlg.getWidth(), 0);
        dlg.setVisible( true);
        
        
        boolean bWaiting = true;
        while( bWaiting) {
            
            
            
            logger.trace( "Waiting for response or timeout");
            m_dblValue = theApp.GetFromPoller( m_ExeStatement.GetOperableDevice().getID() + "."
                                + m_ExeStatement.getExpectedParam());

            //%.2f для всех
            //%.3e для датчиков 4(A,B,C),5   
            if( m_ExeStatement.GetOperableDevice().m_strID.startsWith( "04") ||
                m_ExeStatement.GetOperableDevice().m_strID.equals("005") )
                
                dlg.lblParamValueCurent.setText( String.format( "%.3e", m_dblValue));
            else
                dlg.lblParamValueCurent.setText( String.format( "%.2f", m_dblValue));

            if( Double.isNaN( m_dblValue)) {
                logger.warn( "Waiting got in response 'NaN'");
                continue;
            }

            switch( m_ExeStatement.getApproximation()) {
                case JProgVacuumWait.VAC_WAIT_APPROACH_FROM_DOWNSIDE:
                    if( m_dblValue < m_ExeStatement.getExpected() - m_ExeStatement.getError()) {
                        logger.debug( "Подход снизу. Число меньше нижней границы.");
                    }
                    else {
                        logger.debug( "Подход снизу. Ок.");
                        bWaiting = false;
                    }
                break;

                case JProgVacuumWait.VAC_WAIT_APPROACH_FROM_UPSIDE:
                    if( m_dblValue > m_ExeStatement.getExpected() + m_ExeStatement.getError()) {
                        logger.debug( "Подход сверху. Число больше ожидаемого плюс ошибку.");
                    }
                    else {
                        logger.debug( "Подход снизу. Ок.");
                        bWaiting = false;
                    }
                break;

                case JProgVacuumWait.VAC_WAIT_APPROACH_FROM_NEVERMIND:
                    if( m_dblValue <= m_ExeStatement.getExpected() + m_ExeStatement.getError() &&
                            m_dblValue >= m_ExeStatement.getExpected() - m_ExeStatement.getError()) {
                        logger.debug( "Подход неважен. Попали в диапазон.");
                        bWaiting = false;
                    }
                    else {
                        logger.debug( "Подход неважен. Число вне диапазона.");
                    }
                break;

                default:
                    logger.warn( "Странный тип приближения к ожидаемому значению: " + m_ExeStatement.getApproximation());
            }
            
        }
        
        dlg.dispose();
    }
    
    @Override
    public void processResponse( int nCode) {
        logger.debug( "processResponse( " + nCode + ") call for VacWaitStatementExecutorThread.");
    }

    @Override
    public void processTimeOut() {
        logger.info( "processTimeOut() call for VacWaitStatementExecutorThread. Keep Waiting????");
        m_bTimeOut = true;
    }
    
    
}
