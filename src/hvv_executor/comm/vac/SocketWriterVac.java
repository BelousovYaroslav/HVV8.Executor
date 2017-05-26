/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.vac;

import hvv_executor.comm.CommandItem;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class SocketWriterVac implements Runnable 
{
    static Logger logger = Logger.getLogger(SocketWriterVac.class);
    OutputStream out;
        
    private boolean m_bContinue;
    public void StopThread() {
        m_bContinue = false;
    }
                
    private LinkedList cmdQueue;
    TwoWaySocketServerCommVac pParent;
    
    
    public SocketWriterVac ( OutputStream out, LinkedList queue, TwoWaySocketServerCommVac parent)
    {
        this.out = out;
        this.cmdQueue = queue;
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

                if( ( pParent.pCommandInAction == null) &&                        //нет команды в обработке
                        ( !cmdQueue.isEmpty()) &&                                       //в очереди что-то есть
                        ( pParent.GetTimeoutThread().GetInProgress() != true)) {        //таймаут поток погашен
                    
                    pItem = ( CommandItem) cmdQueue.poll();
                    
                    if( pItem != null) {
                        logger.debug( "Item from queue: '" + pItem + "'!");
                        logger.debug( "Queue length: " + cmdQueue.size());
                    
                        byte [] btId = new byte[1];
                        btId[0] = (byte) ( Integer.parseInt( pItem.GetCommandId()) % 128);
                        String strFormedCommandString = new String( btId);
                        
                        LinkedList lst = pItem.GetParcel();
                        if( lst != null) {
                            Iterator it = lst.iterator();
                            while( it.hasNext()) {
                                strFormedCommandString += ";";
                                strFormedCommandString += ( String) it.next();
                            }
                        }
                        
                        
                        byte [] buffer = strFormedCommandString.getBytes();
                        String strR = "\"";
                        strR += "" + String.format( "%x", buffer[ 0]);
                    
                        for( int i=1; i<strFormedCommandString.length(); i++) {
                            strR += ", " + String.format( "%x", buffer[i]);
                        }
                    
                        strR += "\"";
                        logger.debug( "Formed command string=[" + strR + "]");
                        
                        this.out.write( strFormedCommandString.length());
                        byte [] btsToSend = strFormedCommandString.getBytes();
                        this.out.write( strFormedCommandString.getBytes());
                        this.out.flush();
                        
                        pParent.pCommandInAction = pItem;
                    
                        //logger.debug( "Timeout thread isAlive(): " + pParent.GetTimeoutThread().isAlive());
                        //logger.debug( "Timeout thread getState(): " + pParent.GetTimeoutThread().getState());
                        //logger.debug( "Timeout thread GetInProgress(): " + pParent.GetTimeoutThread().GetInProgress());


                        pParent.CreateNewTimeoutThread();
                        pParent.GetTimeoutThread().start();
                    }
                    else {
                        logger.debug( "Item from queue: 'NULL'! Wow!");
                        logger.debug( "Item from queue is 'null'! Queue length: " + cmdQueue.size());
                    }
                }
                else {
                    if( pParent.pCommandInAction != null) {
                        //logger.trace( "Есть команда в обработке!");
                    }
                    if( cmdQueue.isEmpty()) {
                        //logger.trace( "Очередь команды пустая!");
                    }
                    if( pParent.GetTimeoutThread().GetInProgress() == true) {
                        //logger.trace( "Поток таймаута не закончен!");
                    }
                }
                
                Thread.sleep( 20);
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