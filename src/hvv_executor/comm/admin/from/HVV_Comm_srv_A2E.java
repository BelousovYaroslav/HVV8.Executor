/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm.admin.from;

import JProg.JProgAStatement;
import hvv_executor.HVV_Executor;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author yaroslav
 */
public class HVV_Comm_srv_A2E extends HVV_Communication.server.HVV_Comm_Server {
    final HVV_Executor theApp;
    static Logger logger = Logger.getLogger(HVV_Comm_srv_A2E.class);
    
    private String m_strExecutorState;
    public String GetExecutorState() { return m_strExecutorState; }
    
    private String m_strExecutorProgram;
    public String GetExecutorProgram() { return m_strExecutorProgram; }
    
    public HVV_Comm_srv_A2E( int nServerPort, HVV_Executor app) {
        super( "A2E_SRV: ", nServerPort);
        theApp = app;
        
        m_strExecutorState = "";
        m_strExecutorProgram = "";
    }
    
    @Override
    public void processIncomingCommand( String strReqId, LinkedList lstIncomingParcel) throws Exception {
        String strCmd;
        
        int nRetCode = 0;
        
        strCmd = ( String) lstIncomingParcel.get( 0);
        if( strCmd != null) {
            switch( strCmd) {

                case "START_PROGRAM":
                    String strProgramToLoad = ( String) lstIncomingParcel.get( 1);
                    
                    logger.debug( "INCOMING: [" + strReqId + ";START_PROGRAM;" + strProgramToLoad + "]");
                    
                    File file = new File( theApp.GetAMSRoot() + "/ReadyPrograms/AdminSteps/" + strProgramToLoad);
                    
                    //This is where a real application would open the file.
                    logger.info( "LoadProgram opening: " + file.getName());
                    theApp.m_strCurrentProgram = file.getName();
                    
                    TreeMap newProgram = new TreeMap();
                    boolean bResOk = true;
                    try {
                        SAXReader reader = new SAXReader();
                        URL url = file.toURI().toURL();
                        Document document = reader.read( url);
                        Element program = document.getRootElement();
                        if( program.getName().equals( "Program")) {
                            // iterate through child elements of root
                            for ( Iterator i = program.elementIterator(); i.hasNext(); ) {
                                Element element = ( Element) i.next();
                                String name = element.getName();
                                String strLineNumber = element.getTextTrim();
                                int nLineNumber = Integer.parseInt( strLineNumber);
                                JProgAStatement statement = JProgAStatement.parse( element);
                                if( statement != null)
                                    newProgram.put( nLineNumber, statement);


                                logger.debug( "Pairs: [" + name + " : " + strLineNumber + "]");
                            }

                            theApp.m_program = newProgram;
                            theApp.m_pMainWnd.ShowProgram();
                        }
                        else
                            logger.error( "There is no 'program' root-tag in pointed XML");
                        
                    } catch( MalformedURLException ex) {
                        logger.error( "MalformedURLException caught while loading settings!", ex);
                        bResOk = false;
                    } catch( DocumentException ex) {
                        logger.error( "DocumentException caught while loading settings!", ex);
                        bResOk = false;
                    }
                    
                    
                    theApp.m_pMainWnd.btnStart.setEnabled( false);
                    logger.info( "Starting Exec program");
                    new Thread( theApp.m_MainExeThread).start();
                    theApp.m_nRunningState = HVV_Executor.RUNNING_STATE_RUN;
                    theApp.SetExProgramStartDateAsCurrent();
        
                    nRetCode = 0;
                break;
                    
                case "PING":
                    logger.debug( "INCOMING: [" + strReqId + ";PING;" + "]");
                    nRetCode = 0;
                    
                    if( m_nStopRequested == 1) {
                        nRetCode = 100;
                        m_nStopRequested = 2;
                    }
                break;

                case "QUIT":
                    logger.info( "'QUIT' processing");
                    SetState( STATE_DISCONNECTED);
                    return;

                default:
                    logger.error( "" + strReqId + ": Unknown command '" + strCmd + "'. RetCode 3");
                    nRetCode = 3;
                break;
            }
        }
        else {
            logger.error( "" + strReqId + ": Command is null. RetCode 2");
            nRetCode = 2;
        }


        //RESPOND
        logger.debug( "RESPOND [ " + strReqId + ";" + nRetCode + "]");

        GetObjectOutputStream().writeObject( strReqId);
        GetObjectOutputStream().writeInt( 1);
        GetObjectOutputStream().writeObject( nRetCode);
        GetObjectOutputStream().flush();
    }
    
}
