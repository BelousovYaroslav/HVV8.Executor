/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor;

import HVV_Communication.client.HVV_Comm_client;
import hvv_executor.comm.hv.HVV_Communication_hv;
import hvv_executor.comm.vac.HVV_Communication_vac;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import org.apache.log4j.Logger;
import static java.util.concurrent.TimeUnit.*;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author yaroslav
 */
public class LedsRefreshState {
    static final Logger logger = Logger.getLogger( LedsRefreshState.class);
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final HVV_Executor theApp;

    ScheduledFuture <?> lighterHandle;
        
    private final Border m_border;
    
    public LedsRefreshState( HVV_Executor app) {
        theApp = app;
        m_border = LineBorder.createBlackLineBorder();
    }

    public void lightLedsStart() {
     
        final Runnable lighter;
        lighter = new Runnable() {
            public void run() {
                
                //HV
                ImageIcon ic = null;
                String strState = "---";
                switch( theApp.GetCommHv().GetState()) {
                    case HVV_Communication_hv.STATE_DISCONNECTED:
                        ic = theApp.GetResources().getIconBigBlack();
                        strState = "DIS";
                        break;
                        
                    case HVV_Communication_hv.STATE_CONNECTED_OK:
                        ic = theApp.GetResources().getIconBigGreen();
                        strState = "OK";
                        break;
                        
                    case HVV_Communication_hv.STATE_CONNECTED_IDLE:
                        ic = theApp.GetResources().getIconBigBlue();
                        strState = "IDLE";
                        break;
                        
                    case HVV_Communication_hv.STATE_CONNECTED_PROBLEMS:
                        ic = theApp.GetResources().getIconBigRed();
                        strState = "PRBL";
                        break;
                }
                
                if( ic != null) {
                    theApp.m_pMainWnd.m_lblLedHvComm.setBorder( null);
                    theApp.m_pMainWnd.m_lblLedHvComm.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedHvComm.setText( "");
                }
                else {
                    theApp.m_pMainWnd.m_lblLedHvComm.setBorder( m_border);
                    theApp.m_pMainWnd.m_lblLedHvComm.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedHvComm.setText( strState);
                }
                
                //VAC
                ic = null;
                strState = "---";
                switch( theApp.GetCommVac().GetState()) {
                    case HVV_Communication_vac.STATE_DISCONNECTED:
                        ic = theApp.GetResources().getIconBigBlack();
                        strState = "DIS";
                        break;
                        
                    case HVV_Communication_vac.STATE_CONNECTED_OK:
                        ic = theApp.GetResources().getIconBigGreen();
                        strState = "OK";
                        break;
                        
                    case HVV_Communication_vac.STATE_CONNECTED_IDLE:
                        ic = theApp.GetResources().getIconBigBlue();
                        strState = "IDLE";
                        break;
                        
                    case HVV_Communication_vac.STATE_CONNECTED_PROBLEMS:
                        ic = theApp.GetResources().getIconBigRed();
                        strState = "PRBL";
                        break;
                }
                
                if( ic != null) {
                    theApp.m_pMainWnd.m_lblLedVacComm.setBorder( null);
                    theApp.m_pMainWnd.m_lblLedVacComm.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedVacComm.setText( "");
                }
                else {
                    theApp.m_pMainWnd.m_lblLedVacComm.setBorder( m_border);
                    theApp.m_pMainWnd.m_lblLedVacComm.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedVacComm.setText( strState);
                }
                
                
                //POLLER
                ic = null;
                strState = "---";
                switch( theApp.GetCommE2P().GetState()) {
                    case HVV_Communication.client.HVV_Comm_client.STATE_DISCONNECTED:
                        ic = theApp.GetResources().getIconBigBlack();
                        strState = "DIS";
                        break;
                        
                    case HVV_Communication.client.HVV_Comm_client.STATE_CONNECTED_OK:
                        ic = theApp.GetResources().getIconBigGreen();
                        strState = "OK";
                        break;
                        
                    case HVV_Communication.client.HVV_Comm_client.STATE_CONNECTED_IDLE:
                        ic = theApp.GetResources().getIconBigBlue();
                        strState = "IDLE";
                        break;
                        
                    case HVV_Communication.client.HVV_Comm_client.STATE_CONNECTED_PROBLEMS:
                        ic = theApp.GetResources().getIconBigRed();
                        strState = "PRBL";
                        break;
                }
                
                if( ic != null) {
                    theApp.m_pMainWnd.m_lblLedPollerComm.setBorder( null);
                    theApp.m_pMainWnd.m_lblLedPollerComm.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedPollerComm.setText( "");
                }
                else {
                    theApp.m_pMainWnd.m_lblLedPollerComm.setBorder( m_border);
                    theApp.m_pMainWnd.m_lblLedPollerComm.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedPollerComm.setText( strState);
                }
                
                
                //ADMIN.FROM
                ic = null;
                strState = "---";
                switch( theApp.GetCommA2E().GetState()) {
                    case HVV_Comm_client.STATE_DISCONNECTED:
                        ic = theApp.GetResources().getIconBigBlack();
                        strState = "DIS";
                        break;
                        
                    case HVV_Comm_client.STATE_CONNECTED_OK:
                        ic = theApp.GetResources().getIconBigGreen();
                        strState = "OK";
                        break;
                        
                    case HVV_Comm_client.STATE_CONNECTED_IDLE:
                        ic = theApp.GetResources().getIconBigBlue();
                        strState = "IDLE";
                        break;
                        
                    case HVV_Comm_client.STATE_CONNECTED_PROBLEMS:
                        ic = theApp.GetResources().getIconBigRed();
                        strState = "PRBL";
                        break;
                }
                
                if( ic != null) {
                    theApp.m_pMainWnd.m_lblLedAdminFrom.setBorder( null);
                    theApp.m_pMainWnd.m_lblLedAdminFrom.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedAdminFrom.setText( "");
                }
                else {
                    theApp.m_pMainWnd.m_lblLedAdminFrom.setBorder( m_border);
                    theApp.m_pMainWnd.m_lblLedAdminFrom.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedAdminFrom.setText( strState);
                }
                
                
                //ADMIN.TO
                ic = null;
                strState = "---";
                switch( theApp.GetCommE2A().GetState()) {
                    case HVV_Communication.server.HVV_Comm_Server.STATE_DISCONNECTED:
                        ic = theApp.GetResources().getIconBigBlack();
                        strState = "DIS";
                        break;
                        
                    case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_OK:
                        ic = theApp.GetResources().getIconBigGreen();
                        strState = "OK";
                        break;
                        
                    case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_IDLE:
                        ic = theApp.GetResources().getIconBigBlue();
                        strState = "IDLE";
                        break;
                        
                    case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_PROBLEMS:
                        ic = theApp.GetResources().getIconBigRed();
                        strState = "PRBL";
                        break;
                }
                
                if( ic != null) {
                    theApp.m_pMainWnd.m_lblLedAdminTo.setBorder( null);
                    theApp.m_pMainWnd.m_lblLedAdminTo.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedAdminTo.setText( "");
                }
                else {
                    theApp.m_pMainWnd.m_lblLedAdminTo.setBorder( m_border);
                    theApp.m_pMainWnd.m_lblLedAdminTo.setIcon( ic);
                    theApp.m_pMainWnd.m_lblLedAdminTo.setText( strState);
                }
                
                
                //BUTTONS RUN, PAUSE, STOP
                switch( theApp.m_nRunningState) {
                    case HVV_Executor.RUNNING_STATE_PAUSE:
                        theApp.m_pMainWnd.btnLoad.setEnabled( false);
                        theApp.m_pMainWnd.btnStart.setEnabled( false);
                        theApp.m_pMainWnd.btnPause.setEnabled( true);
                        theApp.m_pMainWnd.btnStop.setEnabled( true);
                        break;
                    case HVV_Executor.RUNNING_STATE_RUN:
                        theApp.m_pMainWnd.btnLoad.setEnabled( false);
                        theApp.m_pMainWnd.btnStart.setEnabled( false);
                        theApp.m_pMainWnd.btnPause.setEnabled( true);
                        theApp.m_pMainWnd.btnStop.setEnabled( true);
                        break;
                    case HVV_Executor.RUNNING_STATE_STOP:
                        theApp.m_pMainWnd.btnLoad.setEnabled( true);
                        theApp.m_pMainWnd.btnStart.setEnabled( !theApp.m_program.isEmpty());
                        theApp.m_pMainWnd.btnPause.setEnabled( false);
                        theApp.m_pMainWnd.btnStop.setEnabled( false);
                        break;
                }
                
                //EXEXCUTABLE.PROGRAM START DATE
                //if( theApp.m_nRunningState != HVV_Executor.RUNNING_STATE_STOP) {
                if( theApp.GetExProgramStart() != null) {
                    String strDt = theApp.NiceFormatDateTime( theApp.GetExProgramStart());
                    strDt = strDt.replaceAll( " ", "     ");
                    theApp.m_pMainWnd.lblDateProgramStart.setText( strDt);
                }
                else
                    theApp.m_pMainWnd.lblDateProgramStart.setText( "--.--.-- --:--:--");
                
                //EXECUTABLE.PROGRAM DURATION
                
                if( theApp.GetExProgramStart() != null) {
                    if( theApp.m_nRunningState != HVV_Executor.RUNNING_STATE_STOP) {
                        Date dt1 = theApp.GetExProgramStart();
                        Date dt2 = theApp.GetLocalDate();

                        long lSpan = ( dt2.getTime() - dt1.getTime()) / 1000;

                        int nHou = ( int)   lSpan / 3600;
                        int nMin = ( int) ( lSpan % 3600) / 60;
                        int nSec = ( int) ( lSpan % 60);

                        String strDuration = String.format( "%02d:%02d:%02d", nHou, nMin, nSec);
                        theApp.m_pMainWnd.lblDateTimeDuration.setText( strDuration);
                    }
                }
                else
                    theApp.m_pMainWnd.lblDateTimeDuration.setText( "--:--:--");
            }
        };
     
        lighterHandle = scheduler.scheduleAtFixedRate( lighter, 1, 1, SECONDS);
        
        /*
        scheduler.schedule( new Runnable() {
            public void run() { beeperHandle.cancel(true); }
        }, 60 * 60, SECONDS);
        */
    }
    
    public void lightLedsStop() {
        lighterHandle.cancel(true);
    }
 
}
