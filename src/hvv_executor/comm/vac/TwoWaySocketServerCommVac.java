package hvv_executor.comm.vac;


import hvv_executor.comm.CommandItem;
import hvv_executor.HVV_Executor;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class TwoWaySocketServerCommVac
{
    public final hvv_executor.HVV_Executor theApp;
    static Logger logger = Logger.getLogger(TwoWaySocketServerCommVac.class);
    
    public Thread thrInput;
    public Thread thrOutput;
            
    Socket pSocket;
    
    private CmdRespondTimeoutThreadVac pTimeoutThread;
    public CmdRespondTimeoutThreadVac GetTimeoutThread() { return pTimeoutThread;}
    public void CreateNewTimeoutThread() {
        if( pTimeoutThread == null) {
            logger.warn( "CreateNewTimeoutThread(): Previous thread object was null! Strange situation.");
        }
        else if( pTimeoutThread.getState() != Thread.State.TERMINATED) {
            logger.warn( "CreateNewTimeoutThread(): Previous thread object state = " + pTimeoutThread.getState());
        }
        pTimeoutThread = new CmdRespondTimeoutThreadVac( this);
    }
            
    public CommandItem pCommandInAction;

    public CircleBuffer crclBuffer;
    
    private int m_nCmdCounter;
    
    SocketReaderVac sr;
    SocketWriterVac sw;
    
    /* ********************************************************** */
    /* *************** COMMAND QUEUE **************************** */
    //private Stack cmdQueue;
    private LinkedList cmdQueue;
    public synchronized void AddCommandToQueueEmergent( CommandItem pCmd) {
        pCmd.SetCommandId( "" + m_nCmdCounter);
        cmdQueue.addFirst( pCmd);
        
        logger.debug( "AddCommandToQueueEmergent(" + pCmd + ", " + m_nCmdCounter + "): queue length: " + cmdQueue.size());
        
        m_nCmdCounter = ( ++m_nCmdCounter) % 254;
        if( m_nCmdCounter == 0x3b) m_nCmdCounter++;
    }
    
    public synchronized void AddCommandToQueue( CommandItem pCmd) {
        pCmd.SetCommandId( "" + m_nCmdCounter);
        cmdQueue.addLast( pCmd);
        
        logger.debug( "AddCommandToQueue(" + pCmd + ", " + m_nCmdCounter + "): queue length: " + cmdQueue.size());
        
        m_nCmdCounter = ( ++m_nCmdCounter) % 254;
        if( m_nCmdCounter == 0x3b) m_nCmdCounter++;
    }
    
    public synchronized int GetCommandQueueLen() { return cmdQueue.size(); }
    /* ********************************************************** */
    
    
    public TwoWaySocketServerCommVac( HVV_Executor app)
    {
        cmdQueue = new LinkedList();
        crclBuffer = new CircleBuffer();
        pTimeoutThread = new CmdRespondTimeoutThreadVac( this);
        pSocket = null;
        theApp = app;
        m_nCmdCounter = 0x37;
    }
    
    public boolean connect( /*COMPortSettings pSettings*/) throws Exception
    {
        if( pSocket != null && !pSocket.isClosed()) {
            logger.error( "Socket is already open!");
            return false;
        }
        
        int nVacPort = theApp.GetSettings().GetVacuumPartPort();
        String strVacHost = theApp.GetSettings().GetVacuumPartHost();
        
        logger.info( "Trying connect to: " + strVacHost + ":" + nVacPort);
        
        try {
            pSocket = new Socket( InetAddress.getByName( strVacHost), nVacPort);
        }
        catch( SocketException ex) {
            //logger.info( "SocketException caught", ex);
            logger.info( "SocketException: " + ex.getLocalizedMessage());
            pSocket = null;
            return false;
        }
        
        InputStream in = pSocket.getInputStream();
        OutputStream out = pSocket.getOutputStream();
                
        sr = new SocketReaderVac( in, this);
        thrInput = new Thread( sr);
        thrInput.start();
        
        sw = new SocketWriterVac( out, cmdQueue, this);
        thrOutput = new Thread( sw);
        thrOutput.start();
        
        return true;
    }
    
    public boolean IsConnected() {
        if( pSocket == null) return false;
        //logger.debug( "Socket isConnected:" + pSocket.isConnected());
        return pSocket.isConnected();
    }
    
    public void disconnect( /*COMPortSettings pSettings*/) throws Exception {
        if( pTimeoutThread != null)
            pTimeoutThread.interrupt();
        
        if( sr != null) {
            sr.StopThread();
            thrInput.join();
            thrInput = null;
            sr = null;
        }
        
        if( sw != null) {
            sw.StopThread();
            thrOutput.join();
            thrOutput = null;
            sw = null;
        }
        
        if( pSocket != null) {
            pSocket.close();
            pSocket = null;
        }
    }
}