/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor;

import HVV_Communication.client.HVV_Comm_client;
import hvv_executor.comm.admin.from.HVV_Comm_srv_A2E;
import hvv_executor.comm.admin.to.SendCurrentStateToAdminThread;
import hvv_executor.comm.hv.HVV_Communication_hv;
import hvv_executor.comm.poller.GetExecutor;
import hvv_executor.comm.vac.HVV_Communication_vac;
import hvv_resources.HVV_Resources;
import java.io.File;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author yaroslav
 */
public class HVV_Executor {

    private final String m_strAMSrootEnvVar;
    public String GetAMSRoot() { return m_strAMSrootEnvVar; }
    
    public FrmMainWindow m_pMainWnd;
    public TreeMap m_program;
    static Logger logger = Logger.getLogger( HVV_Executor.class);
    public static final org.apache.log4j.Level LOG_LEVEL = org.apache.log4j.Level.DEBUG;
    
    private final HVV_ExecutorSettings m_pSettings;
    public HVV_ExecutorSettings GetSettings() { return m_pSettings; }
    
    private final HVV_Resources m_pResources;
    public HVV_Resources GetResources() { return m_pResources; }
    
    
    private final HVV_Communication_hv m_comm_hv;
    public HVV_Communication_hv GetCommHv() { return m_comm_hv; }
    
    private final HVV_Communication_vac m_comm_vac;
    public HVV_Communication_vac GetCommVac() { return m_comm_vac; }
    
    /*
    private final HVV_Communication_poller m_poller_communicator;
    public HVV_Communication_poller GetPollerCommunicator() { return m_poller_communicator; }
    */
    private final HVV_Comm_client m_comm_E2P;
    public HVV_Comm_client GetCommE2P() { return m_comm_E2P; }
    
    private final HVV_Comm_srv_A2E m_comm_admin_from;
    public HVV_Comm_srv_A2E GetCommA2E() { return m_comm_admin_from; }
    
    private final HVV_Comm_client m_comm_admin_to;
    public HVV_Comm_client GetCommE2A() { return m_comm_admin_to; }
    SendCurrentStateToAdminThread m_StateInformerThread;
    
    
    static final public int RUNNING_STATE_STOP = 1;
    static final public int RUNNING_STATE_RUN = 2;
    static final public int RUNNING_STATE_PAUSE = 3;
    
    public int m_nRunningState;
    
    public String m_strCurrentProgram;
    
    private ServerSocket m_pSingleInstanceSocketServer;
    
    private Date m_dtStartExProgram;
    public void SetExProgramStartDateAsCurrent() {
        m_dtStartExProgram = GetLocalDate();
    }
    public Date GetExProgramStart() { return m_dtStartExProgram; }
    
    public MainExeThread m_MainExeThread;
    
    public HVV_Executor() {
        m_strAMSrootEnvVar = System.getenv( "AMS_ROOT");
        
        m_dtStartExProgram = null;
        
        //SETTINGS
        m_pSettings = new HVV_ExecutorSettings( m_strAMSrootEnvVar);
        
        //ПРОВЕРКА ОДНОВРЕМЕННОГО ЗАПУСКА ТОЛЬКО ОДНОЙ КОПИИ ПРОГРАММЫ
        try {
            m_pSingleInstanceSocketServer = new ServerSocket( GetSettings().GetSingleInstanceSocketServerPort());
        }
        catch( Exception ex) {
            MessageBoxError( "Модуль исполнения программ автоматизации уже запущен.\nПоищите на других \"экранах\".", "Модуль исполнения программ автоматизации");
            logger.error( "Не смогли открыть сокет для проверки запуска только одной копии программы! Программа уже запущена?", ex);
            m_pSingleInstanceSocketServer = null;
            m_pResources = null;
            
            m_comm_hv = null;
            m_comm_vac = null;
            m_comm_E2P = null;
            m_comm_admin_from = null;
            m_comm_admin_to = null;
            
            return;
        }
        
        
        //RESOURCES
        m_pResources = HVV_Resources.getInstance();
        
        //COMMUNICATOR.HV
        m_comm_hv = new HVV_Communication_hv( this);
        m_comm_hv.start();
        
        //COMMUNICATOR.VAC
        m_comm_vac = new HVV_Communication_vac( this);
        m_comm_vac.start();
        
        
        //COMMUNICATOR.POLLER
        /*
        m_poller_communicator = new HVV_Communication_poller( this);
        m_poller_communicator.start();
        */
        m_comm_E2P = new HVV_Comm_client( "E2P_CLI: ", m_pSettings.GetPollerPartHost(), m_pSettings.GetPollerPartPort());
        m_comm_E2P.start();
        
        
        //ADMIN.FROM
        m_comm_admin_from = new HVV_Comm_srv_A2E( m_pSettings.GetAdminFromPartPort(), this);
        m_comm_admin_from.start();
                
        //ADMIN.TO
        m_comm_admin_to = new HVV_Comm_client( "E2A_CLI: ", m_pSettings.GetAdminToPartHost(), m_pSettings.GetAdminToPartPort());
        m_comm_admin_to.start();
        m_StateInformerThread = new SendCurrentStateToAdminThread( this);
        m_StateInformerThread.StartThread();
        
        m_MainExeThread = new MainExeThread( this);
        
        m_nRunningState = RUNNING_STATE_STOP;
    }
    
    public void start() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger( FrmMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( FrmMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( FrmMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( FrmMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        m_pMainWnd = new FrmMainWindow( this);
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                m_pMainWnd.setVisible( true);
            }
        });
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //logger.setLevel( LOG_LEVEL);
        
        //главная переменная окружения
        String strAMSrootEnvVar = System.getenv( "AMS_ROOT");
        if( strAMSrootEnvVar == null) {
            MessageBoxError( "Не задана переменная окружения AMS_ROOT!", "AMS");
            return;
        }
        
        //настройка логгера
        String strlog4jPropertiesFile = strAMSrootEnvVar + "/etc/log4j.executor.properties";
        File file = new File( strlog4jPropertiesFile);
        if(!file.exists())
            System.out.println("It is not possible to load the given log4j properties file :" + file.getAbsolutePath());
        else
            PropertyConfigurator.configure( file.getAbsolutePath());

        logger.info( "");
        logger.info( "");
        logger.info( "");
        logger.info( "******");
        logger.info( "******");
        logger.info( "******");
            
        //запуск программы
        HVV_Executor appInstance = new HVV_Executor();
        if( appInstance.m_pSingleInstanceSocketServer != null) {
            logger.info( "HVV_Executor::main(): Start point!");
            appInstance.start();
        }
    }
    
    /**
     * Функция для сообщения пользователю информационного сообщения
     * @param strMessage сообщение
     * @param strTitleBar заголовок
     */
    public static void MessageBoxInfo( String strMessage, String strTitleBar)
    {
        JOptionPane.showMessageDialog( null, strMessage, strTitleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Функция для сообщения пользователю сообщения об ошибке
     * @param strMessage сообщение
     * @param strTitleBar заголовок
     */
    public static void MessageBoxError( String strMessage, String strTitleBar)
    {
        JOptionPane.showMessageDialog( null, strMessage, strTitleBar, JOptionPane.ERROR_MESSAGE);
    }
    
    public Date GetLocalDate() {
        Date dt = new Date( System.currentTimeMillis() - 1000 * 60 * 60 * GetSettings().GetTimeZoneShift());
        return dt;
    }
    
    public String NiceFormatDateTime( Date dt) {
        String strResult;
                
        GregorianCalendar clndr = new GregorianCalendar();
        clndr.setTime( dt);

        strResult = String.format( "%02d.%02d.%02d %02d:%02d:%02d",
                clndr.get(Calendar.DAY_OF_MONTH),
                clndr.get(Calendar.MONTH) + 1,
                clndr.get(Calendar.YEAR),
                clndr.get(Calendar.HOUR_OF_DAY),
                clndr.get(Calendar.MINUTE),
                clndr.get(Calendar.SECOND));
        
        return strResult;
    }
    
    public double GetFromPoller( String strParam) {
        double dblResult = Double.NaN;
        GetExecutor executor = new GetExecutor( this, strParam);
        executor.StartThread();
        
        boolean bContinue = true;
        int nCounter = 0;
        
        do {
            if( executor.IsAnswerReceived()) {
                dblResult = executor.GetResult();
                break;
            }
            
            if( executor.IsTimeOutHappens())
                break;
            
            if( ++nCounter == 50)
                break;
            
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                logger.error( "InterruptedException caught: ", ex);
                bContinue = false;
            }
            
        } while( bContinue);
        
        return dblResult;
    }
}
