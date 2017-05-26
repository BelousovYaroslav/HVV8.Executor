/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.vac;

import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 * Thread-class for COM-port listener
 */
public class SocketReaderVac implements Runnable 
{
    static Logger logger = Logger.getLogger(SocketReaderVac.class);
    
    InputStream in;
    TwoWaySocketServerCommVac pParent;
    
    private boolean m_bContinue;

    public void StopThread() { m_bContinue = false;}
    
    public SocketReaderVac( InputStream in, TwoWaySocketServerCommVac parent)
    {
        this.in = in;
        pParent = parent;
    }
        
    public void run ()
    {
        m_bContinue = true;
        byte[] buffer = new byte[ CircleBuffer.BUFFER_LEN];
        int len = -1;
        
        logger.debug("In");
        
        try {
            
            logger.debug("before while");
            
            while( m_bContinue) {
                
                if( this.in.available() > 0) {
                    len = this.in.read(buffer);
                
                    logger.trace("after this.in.read. len=" + len);
                
                    String strResponse1 = new String( buffer, 0, len - 1);
                    logger.debug( "COM-INTERACTION Response1: [commented]");//" + strResponse1);
                
                    pParent.crclBuffer.AddBytes( buffer, len);
                    
                    
                    String strR = "\"";
                    strR += "" + String.format( "%x", buffer[ 0]);
                    
                    for( int i=1; i<len; i++) {
                        strR += ", " + String.format( "%x", buffer[i]);
                    }
                    
                    strR += "\"";
                    logger.info( strR);
                    
                    //if( pParent.strCommandInAction != null) {
                        len = pParent.crclBuffer.isAnswerReady();
                        if( len > 0) {
                            byte [] answer = new byte[len];
                            if( pParent.crclBuffer.getAnswer( len, answer) == 0) {
                            
                                boolean bCorrectResponse = true;
                        
                                //check if strResponse is valid
                                //1. 0xff на конце
                                if( answer[len-1] != -1) {
                                    logger.warn( "Пришедший ответ без (0xFF) на конце");
                                    bCorrectResponse = false;
                                }
                            
                                if( bCorrectResponse) {
                                    pParent.GetTimeoutThread().interrupt();
                                
                                    logger.info( "ОБРАБОТКА ОТВЕТА!");
                                    if( pParent != null) {
                                        if( pParent.pCommandInAction != null) {
                                            if( pParent.pCommandInAction.GetProcessor() != null) {
                                                pParent.pCommandInAction.GetProcessor().processResponse( 0);
                                                pParent.pCommandInAction = null;
                                            }
                                            else {
                                                logger.warn( "Получен ответ на существующую команду, но у которой нет обработчика! Skipping");
                                            }
                                        }
                                        else {
                                            logger.warn( "Получен ответ на несуществующую команду! Skipping");
                                        }
                                    }
                                    else {
                                        logger.error( "pParent = null при обработке ответа!");
                                        m_bContinue = false;
                                    }
                                    
                                }
                                
                            }
                            else
                                logger.error( "CircleBuffer.getAnswer returns not 0");
                        }
                        else
                            logger.debug( "CircleBuffer ответил что ответ не готов!");
                    
                    //}
                    //else {
                    //    logger.warn( "Входящие данные при отсутствии запрашивающей команды!");
                    //}
                        
                }
                else {
                    //logger.debug( "No RX data");
                }
                
                Thread.sleep( 50);
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