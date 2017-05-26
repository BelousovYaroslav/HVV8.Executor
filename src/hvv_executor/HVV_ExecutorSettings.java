/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author yaroslav
 */
public class HVV_ExecutorSettings {
    static Logger logger = Logger.getLogger( HVV_ExecutorSettings.class);
    
    //VAC
    private String m_strVacuumPartHost;
    public String GetVacuumPartHost() { return m_strVacuumPartHost;}
    
    private int m_nVacuumPartPort;
    public int GetVacuumPartPort() { return m_nVacuumPartPort;}
    
    //HV
    private String m_strHvPartHost;
    public String GetHvPartHost() { return m_strHvPartHost;}
    
    private int m_nHvPartPort;
    public int GetHvPartPort() { return m_nHvPartPort;}
    
    //POLLER
    private String m_strPollerPartHost;
    public String GetPollerPartHost() { return m_strPollerPartHost;}
    
    private int m_nPollerPartPort;
    public int GetPollerPartPort() { return m_nPollerPartPort;}
    
    //ADMIN.FROM
    private String m_strAdminFromPartHost;
    public String GetAdminFromPartHost() { return m_strAdminFromPartHost;}
    
    private int m_nAdminFromPartPort;
    public int GetAdminFromPartPort() { return m_nAdminFromPartPort;}
    
    //ADMIN.TO
    private String m_strAdminToPartHost;
    public String GetAdminToPartHost() { return m_strAdminToPartHost;}
    
    private int m_nAdminToPartPort;
    public int GetAdminToPartPort() { return m_nAdminToPartPort;}
    
    //SINGLE-INSTANCE-RUN
    private final int m_nSingleInstanceSocketServerPort;
    public int GetSingleInstanceSocketServerPort() { return m_nSingleInstanceSocketServerPort;}
    
    private int m_nTimeZoneShift;
    public int GetTimeZoneShift() { return m_nTimeZoneShift;}

    
    public HVV_ExecutorSettings( String strAMSRoot) {
        //VAC.EMULATOR
        m_strVacuumPartHost = "localhost";
        m_nVacuumPartPort = 5310;
        
        //VAC.REAL
        //m_strVacuumPartHost = "192.168.1.1";
        //m_nVacuumPartPort = 5310;
        
        //HV
        m_strHvPartHost = "localhost";
        m_nHvPartPort = 6343;
        
        //POLLER
        m_strPollerPartHost = "localhost";
        m_nPollerPartPort = 6342;
        
        //ADMIN.FROM
        m_strAdminFromPartHost = "localhost";
        m_nAdminFromPartPort = 6345;
        
        //ADMIN.TO
        m_strAdminToPartHost = "localhost";
        m_nAdminToPartPort = 6346;
        
        //SINGLE-INSTANCE-RUN
        m_nSingleInstanceSocketServerPort = 10001;
        
        //TIME ZONE SHIFT
        m_nTimeZoneShift = 1;
        
        ReadSettings();
    }
    
    private boolean ReadSettings() {
        boolean bResOk = true;
        try {
            SAXReader reader = new SAXReader();
            
            String strSettingsFilePathName = System.getenv( "AMS_ROOT") + "/etc/settings.executor.xml";
            URL url = ( new java.io.File( strSettingsFilePathName)).toURI().toURL();
            
            Document document = reader.read( url);
            
            Element root = document.getRootElement();

            // iterate through child elements of root
            for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                String name = element.getName();
                String value = element.getText();
                
                //logger.debug( "Pairs: [" + name + " : " + value + "]");
                
                if( "vacuum.host".equals( name)) m_strVacuumPartHost = value;
                if( "vacuum.port".equals( name)) m_nVacuumPartPort = Integer.parseInt( value);
                
                if( "hv.host".equals( name)) m_strHvPartHost = value;
                if( "hv.port".equals( name)) m_nHvPartPort = Integer.parseInt( value);
                
                if( "poller.host".equals( name)) m_strPollerPartHost = value;
                if( "poller.port".equals( name)) m_nPollerPartPort = Integer.parseInt( value);
                
                if( "admin.from.host".equals( name)) m_strAdminFromPartHost = value;
                if( "admin.from.port".equals( name)) m_nAdminFromPartPort = Integer.parseInt( value);
                
                if( "admin.to.host".equals( name)) m_strAdminToPartHost = value;
                if( "admin.to.port".equals( name)) m_nAdminToPartPort = Integer.parseInt( value);
                
                if( "timezone".equals( name)) m_nTimeZoneShift = Integer.parseInt( value);
            }
            
        } catch( MalformedURLException ex) {
            logger.error( "MalformedURLException caught while loading settings!", ex);
            bResOk = false;
        } catch( DocumentException ex) {
            logger.error( "DocumentException caught while loading settings!", ex);
            bResOk = false;
        }
        
        return bResOk;
    }
}
