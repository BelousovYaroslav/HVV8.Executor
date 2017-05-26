package hvv_executor.comm.hv;


import hvv_executor.comm.CommandItem;
import hvv_executor.HVV_Executor;
import hvv_timeouts.HVV_TimeoutsManager;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

public class TwoWaySocketServerCommHv
{
    public final hvv_executor.HVV_Executor theApp;
    static Logger logger = Logger.getLogger(TwoWaySocketServerCommHv.class);
    
    public Thread thrInput;
    public Thread thrOutput;
            
    Socket pSocket;
    
    /*
    private CmdRespondTimeoutThreadHv pTimeoutThread;
    public CmdRespondTimeoutThreadHv GetTimeoutThread() { return pTimeoutThread;}
    public void CreateNewTimeoutThread() {
        if( pTimeoutThread == null) {
            logger.warn( "CreateNewTimeoutThread(): Previous thread object was null! Strange situation.");
        }
        else if( pTimeoutThread.getState() != Thread.State.TERMINATED) {
            logger.warn( "CreateNewTimeoutThread(): Previous thread object state = " + pTimeoutThread.getState());
        }
        pTimeoutThread = new CmdRespondTimeoutThreadHv( this);
    }
    */
            
    volatile public long m_lTimeOutId;
    volatile int m_nTimeoutCounter;
    volatile private CommandItem m_pCommandInAction;

    synchronized CommandItem GetCmdInAction() { return m_pCommandInAction; }
    synchronized void SetCmdInAction( CommandItem pNewAction) { m_pCommandInAction = pNewAction; }
    
    int m_nCmdCounter;
    
    SocketReaderHv sr;
    SocketWriterHv sw;
    
    /* ********************************************************** */
    /* *************** COMMAND QUEUE **************************** */
    //private Stack cmdQueue;
    private final ConcurrentLinkedQueue cmdQueue;

    public synchronized ConcurrentLinkedQueue GetQueue() { return cmdQueue; }
        
    public synchronized void AddCommandToQueue( CommandItem item) {
        item.SetCommandId( "" + m_nCmdCounter);
        cmdQueue.add( item);
        
        logger.debug( "AddCommandToQueue(" + item + ", " + m_nCmdCounter + "): queue length: " + cmdQueue.size());
        
        m_nCmdCounter = ( ++m_nCmdCounter) % 254;
    }
    
    /* ********************************************************** */
    
    
    public TwoWaySocketServerCommHv( HVV_Executor app)
    {
        cmdQueue = new ConcurrentLinkedQueue();
        
        m_lTimeOutId = 0;
        m_nTimeoutCounter = 0;
        
        pSocket = null;
        theApp = app;
        m_nCmdCounter = 0;
    }
    
    public boolean connect( /*COMPortSettings pSettings*/) throws Exception
    {
        if( pSocket != null && !pSocket.isClosed()) {
            logger.error( "Socket is already open!");
            return false;
        }
        
        int nHvPort = theApp.GetSettings().GetHvPartPort();
        String strHvHost = theApp.GetSettings().GetHvPartHost();
        
        try {
            pSocket = new Socket( InetAddress.getByName( strHvHost), nHvPort);
        }
        catch( SocketException ex) {
            //logger.info( "SocketException caught", ex);
            logger.info( "SocketException: " + ex.getLocalizedMessage());
            pSocket = null;
            return false;
        }
        
        InputStream is = pSocket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream( is);
        ObjectOutputStream oos = new ObjectOutputStream( pSocket.getOutputStream());
                
        sr = new SocketReaderHv( is, ois, this);
        thrInput = new Thread( sr);
        thrInput.start();
        
        sw = new SocketWriterHv( oos, this);
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
        //if( pTimeoutThread != null)
        //    pTimeoutThread.interrupt();
        if( m_lTimeOutId != 0) {            
            HVV_TimeoutsManager.getInstance().RemoveId(m_lTimeOutId);
            m_lTimeOutId = 0;
        }
        
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