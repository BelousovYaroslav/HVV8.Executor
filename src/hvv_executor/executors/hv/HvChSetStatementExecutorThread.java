/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.executors.hv;

import JProg.hv.JProgHvChannels;
import hvv_executor.executors.AStatementExeThread;
import hvv_executor.HVV_Executor;
import hvv_executor.comm.CommandItem;
import hvv_executor.comm.admin.to.HvChannelsEnabledRequest;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HvChSetStatementExecutorThread extends AStatementExeThread {

    static Logger logger = Logger.getLogger(HvChSetStatementExecutorThread.class);            
    
    private final JProgHvChannels m_ExeStatement;
    
    volatile private boolean m_bSent;
    volatile private boolean m_bGotAnswer;
    volatile private boolean m_bTimeOut;
    
    public HvChSetStatementExecutorThread( HVV_Executor app, JProgHvChannels statement) {
        super( app);
        m_ExeStatement = statement;
    }

    @Override
    public void run() {
        int nAdminHvChannelsMask = 0xFF;
        
        //если надо - сперва спросим у админа кого можно зажигать
        if( m_ExeStatement.GetCheckAdmin()) {
            
            HvChannelsEnabledRequest sender = new HvChannelsEnabledRequest( theApp);
            sender.StartThread();
            
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
            
            nAdminHvChannelsMask = sender.GetRespond();
        }
        
        //ANODES
        for( int i=0; i<8; i++) {
            
            if( m_ExeStatement.GetDeviceAnodeAction(i) != JProgHvChannels.ACTION_NO_CHANGE) {
            
                m_bSent = false;
                m_bGotAnswer = false;
                m_bTimeOut = false;
        
                if( m_bSent == false) {
                    LinkedList lst = new LinkedList();

                    lst.addLast( "SET");
                    lst.addLast( "L" + (i+1) + "A");        //TODO: сопрячь с AMS и сделать по форме L(x)A.01 
                    
                    if( m_ExeStatement.GetDeviceAnodeAction(i) == JProgHvChannels.ACTION_TURN_ON) {
                        if( m_ExeStatement.GetCheckAdmin()) {
                            //надо сверится с админом
                            int nMask = 1 << i;
                            
                            logger.debug( "(A): nMask: " + nMask + " " + String.format( "0x%02x", nMask));
                            logger.debug( "(A): nAdminHvChannelsMask: " + nAdminHvChannelsMask +
                                                " " + String.format( "0x%02x", nAdminHvChannelsMask));
                            
                            if( ( nAdminHvChannelsMask & nMask) != 0)
                                lst.addLast( 1);
                            else
                                lst.addLast( 0);
                        }
                        else
                            //сверяться с админом не надо
                            lst.addLast( 1);
                    }
                    if( m_ExeStatement.GetDeviceAnodeAction(i) == JProgHvChannels.ACTION_TURN_OFF) lst.addLast( 0);
                
                    CommandItem item = new CommandItem( this, lst);
                    theApp.GetCommHv().GetRxTx().AddCommandToQueue( item);
                }

                boolean bProcessing = true;
                while( bProcessing) {    
                    if( m_bGotAnswer == true || m_bTimeOut == true)
                        bProcessing = false;

                    try {
                        sleep( 10);
                    } catch (InterruptedException ex) {
                        logger.error( "InterruptedException caught!", ex);
                    }
                }
            }
        }
        
        //TUBULATIONS
        for( int i=0; i<8; i++) {
            
            if( m_ExeStatement.GetDeviceTubulationAction(i) != JProgHvChannels.ACTION_NO_CHANGE) {
            
                m_bSent = false;
                m_bGotAnswer = false;
                m_bTimeOut = false;
        
                if( m_bSent == false) {
                    LinkedList lst = new LinkedList();

                    lst.addLast( "SET");
                    lst.addLast( "L" + (i+1) + "T");
                    if( m_ExeStatement.GetDeviceTubulationAction(i) == JProgHvChannels.ACTION_TURN_ON) {
                        if( m_ExeStatement.GetCheckAdmin()) {
                            //надо сверится с админом
                            int nMask = 1 << i;
                            
                            logger.debug( "(T): nMask: " + nMask + " " + String.format( "0x%02x", nMask));
                            logger.debug( "(T): nAdminHvChannelsMask: " + nAdminHvChannelsMask +
                                                " " + String.format( "0x%02x", nAdminHvChannelsMask));
                            
                            if( ( nAdminHvChannelsMask & nMask) != 0)
                                lst.addLast( 1);
                            else
                                lst.addLast( 0);
                        }
                        else
                            //сверяться с админом не надо
                            lst.addLast( 1);
                    }
                    if( m_ExeStatement.GetDeviceTubulationAction(i) == JProgHvChannels.ACTION_TURN_OFF) lst.addLast( 0);
                
                    CommandItem item = new CommandItem( this, lst);
                    theApp.GetCommHv().GetRxTx().AddCommandToQueue( item);
                }

                boolean bProcessing = true;
                while( bProcessing) {    
                    if( m_bGotAnswer == true || m_bTimeOut == true)
                        bProcessing = false;

                    try {
                        sleep( 10);
                    } catch (InterruptedException ex) {
                        logger.error( "InterruptedException caught!", ex);
                    }
                }
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
