/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.hv;

import hvv_executor.comm.CommandItem;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class SocketWriterHv implements Runnable 
{
    static Logger logger = Logger.getLogger(SocketWriterHv.class);
    ObjectOutputStream out;
        
    private boolean m_bContinue;
    public void StopThread() {
        m_bContinue = false;
    }
                
    TwoWaySocketServerCommHv pParent;
    
    
    public SocketWriterHv ( ObjectOutputStream out, TwoWaySocketServerCommHv parent)
    {
        this.out = out;
        this.pParent = parent;
        
    }
        
    public void run ()
    {
        String strToSend;
        m_bContinue = true;
        
        CommandItem pItem;
        try {
            
            logger.debug("before while");
            
            while( m_bContinue) {

                if( pParent.GetCmdInAction() == null) {                 //если мы в этот момент ничего не обрабатываем
                    if( pParent.GetQueue().isEmpty() == false) {            //в очереди команд на обработку что-то есть
                    
                        pItem = ( CommandItem) pParent.GetQueue().poll();
                    
                        if( pItem != null) {
                            logger.debug( "Item from queue: '" + pItem + "'!");
                            logger.debug( "Queue length: " + pParent.GetQueue().size());
                    
                            this.out.writeObject( pItem.GetCommandId());
                            logger.debug( "HV --->" + pItem.GetCommandId());
                            LinkedList lst = pItem.GetParcel();
                            if( lst != null) {
                                Iterator it = lst.iterator();
                                while( it.hasNext()) {
                                    Object ob = it.next();
                                    this.out.writeObject( ob);
                                    logger.debug( "HV --->" + ob);
                                }
                            }
                        
                            //logger.debug( "Timeout thread isAlive(): " + pParent.GetTimeoutThread().isAlive());
                            //logger.debug( "Timeout thread getState(): " + pParent.GetTimeoutThread().getState());
                            //logger.debug( "Timeout thread GetInProgress(): " + pParent.GetTimeoutThread().GetInProgress());

                            //START TIMEOUT INSTANCE
                            //pParent.CreateNewTimeoutThread();
                            //pParent.GetTimeoutThread().start();
                            pParent.m_lTimeOutId = hvv_timeouts.HVV_TimeoutsManager.getInstance().StartTimeout( 1000);

                            pParent.SetCmdInAction( pItem);
                        }
                        else {
                            logger.debug( "Item from queue: 'NULL'! Wow!");
                            logger.debug( "Item from queue is 'null'! Queue length: " + pParent.GetQueue().size());
                        }
                    }
                    else {
                        logger.trace( "2. Очередь команд пустая!");
                    }
                }
                else {
                    /*
                    if( pParent.pCommandInAction != null) {
                        //logger.debug( "Есть команда в обработке!");
                    }
                    if( cmdQueue.isEmpty()) {
                        //logger.debug( "Очередь команды пустая!");
                    }
                    if( pParent.GetTimeoutThread().GetInProgress() == true) {
                        //logger.debug( "Поток таймаута предыдущей команды не закончен!");
                    }
                    */
                    logger.trace( "1. Команда испущена, а сигнала об окончнии её обработки или таймаута не было!");
                }
                
                Thread.sleep( 2);
            }
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