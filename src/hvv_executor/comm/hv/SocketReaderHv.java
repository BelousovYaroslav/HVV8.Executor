/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.hv;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import org.apache.log4j.Logger;

/**
 * Thread-class for COM-port listener
 */
public class SocketReaderHv implements Runnable 
{
    static Logger logger = Logger.getLogger(SocketReaderHv.class);
    
    InputStream m_is;
    ObjectInputStream m_ois;
    TwoWaySocketServerCommHv pParent;
    
    private boolean m_bContinue;

    public void StopThread() { m_bContinue = false;}
    
    public SocketReaderHv( InputStream is, ObjectInputStream in, TwoWaySocketServerCommHv parent)
    {
        this.m_is = is;
        this.m_ois = in;
        pParent = parent;
    }
        
    public void run ()
    {
        m_bContinue = true;
        
        logger.debug("In");
        
        try {
            
            logger.debug("before while");
            
            while( m_bContinue) {
                
                /**TODO
                 * QUIT processing
                 */
                /*
                if( pParent != null &&
                                pParent.GetCmdInAction() != null &&
                                pParent.GetCmdInAction().equals( "QUIT")) {
                        
                        m_bContinue = false;
                        break;
                    }
                */
                
                if( pParent.GetCmdInAction() != null) {
                    if( hvv_timeouts.HVV_TimeoutsManager.getInstance().CheckTimeout( pParent.m_lTimeOutId) == true) {
                        logger.info( "TimeOut happens for id=" + pParent.m_lTimeOutId + "  'REQ." + pParent.GetCmdInAction() + "' command!");
                        hvv_timeouts.HVV_TimeoutsManager.getInstance().RemoveId(pParent.m_lTimeOutId);
                        pParent.m_lTimeOutId = 0;
                        pParent.SetCmdInAction( null);
                        pParent.m_nTimeoutCounter++;
                    }
                    else {
                        if( m_is.available() > 0) {
                            try {
                                String strId = ( String) this.m_ois.readObject();

                                logger.debug( "GOT RESPOND ID: " + strId);
                                //TODO 1 sec timeout for second object?
                                int nResult = this.m_ois.readInt();
                                logger.debug( "GOT RESPOND CODE: " + nResult);

                                if( pParent.GetCmdInAction().GetCommandId().equals( strId) ) {
                                    logger.debug( "Got an answer!");
                                    
                                    //pParent.GetTimeoutThread().interrupt();
                                    hvv_timeouts.HVV_TimeoutsManager.getInstance().RemoveId(pParent.m_lTimeOutId);
                                    pParent.m_lTimeOutId = 0;
                                    pParent.m_nTimeoutCounter = 0;
                                    
                                    pParent.GetCmdInAction().GetProcessor().processResponse( nResult);
                                    
                                    //сбросим текущую транзакцию обмена командами
                                    pParent.SetCmdInAction( null);
                                }
                                else {
                                    logger.error( "ID текущей активной команды=" + pParent.GetCmdInAction().GetCommandId());
                                    logger.error( "ID команды, на которую получен ответ=" + strId);
                                    logger.error( "Тем не менее, продолжаем ждать ответ на текущую активную команду (или таймаут)!");
                                }
                            }
                            catch( ClassNotFoundException ex) {
                                logger.error( "ClassNotFoundException caught!", ex);
                                m_bContinue = false;
                            }
                            catch( EOFException ex) {
                                logger.error( "EOFException caught!", ex);
                                m_bContinue = false;
                            }
                        }
                        else {
                            logger.trace( "Команда отправлена. Ждём ответ. Available bytes = 0");
                        }
                    }
                }
                else {
                    logger.trace( "Нет отправленной команды");
                }
                
                Thread.sleep( 1);
            }
            
            //something new
            //something new2
            logger.debug("after while");
            
        }
        catch ( IOException ex) {
            logger.error( "IOException caught!", ex);
        }
        catch ( InterruptedException ex) {
            logger.error( "InterruptedException caught!", ex);
        }
        
        logger.debug("Out");
    }
}