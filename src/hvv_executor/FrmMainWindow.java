/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor;

import JProg.JProgAStatement;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author yaroslav
 */
public class FrmMainWindow extends javax.swing.JFrame {
    HVV_Executor theApp;
    static Logger logger = Logger.getLogger( FrmMainWindow.class);
        
    DefaultListModel lstModel;
    
    public PanelProgramShow m_pnlProgram;
    
    /**
     * Inner class for FileOpen dialog filter
     */
    class MyXMLFilter extends FileFilter {

        @Override
        public boolean accept( File f) {
            if (f.isDirectory()) {
                return true;
            }
            
            String extension = Utils.getExtension( f);
            if( extension != null) {
                if( extension.equals( Utils.xml))
                    return true;
                else
                    return false;
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "XML files";
        }
        
    }
    
    private LedsRefreshState ledsRefresher;
            
    /**
     * Creates new form NewJFrame
     * @param app
     */
    public FrmMainWindow( HVV_Executor app) {
        initComponents();
        
        
        theApp = app;
        
        
        m_pnlProgram = new PanelProgramShow();
        m_pnlProgram.CreateNewPanel( 5);
        //m_pnlProgram.setBounds( 5, 5, 490, 800);
        pnlShowFrame.add(m_pnlProgram);
                
        ledsRefresher = new LedsRefreshState( app);
        ledsRefresher.lightLedsStart();
        
        setTitle( "Модуль исполнения программ автоматизации, v.1.0.0.0 (2016.09.09 13:49), (C) ФЛАВТ 2016.");
        lstModel = new DefaultListModel();
        lstProgram.setModel( lstModel);
        
        theApp.m_program = new TreeMap();
        
        /*theApp.m_program.put("10", new JProg.service.JProgServPauseStatement( 3000));
        theApp.m_program.put("20", new JProg.service.JProgServMessageStatement( "Message!",
                "/home/yaroslav/media/level.wav",
                "/home/yaroslav/media/bonus.wav", 5));
        theApp.m_program.put("30", new JProg.service.JProgServPauseStatement( 30));
        theApp.m_program.put("21", new JProg.service.JProgServSoundStatement( "/home/yaroslav/media/train.wav"));*/
        
        
        
        
        
        
        /*theApp.m_program.put("10", new JProg.service.JProgServMessageStatement(
                "Демонстрация открытия-закрытия клапанов.",
                "/home/yaroslav/media/level.wav",
                "/home/yaroslav/media/bonus.wav", 5));
        
        JProgVacuumOpen st = new JProgVacuumOpen(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "14E"));
        theApp.m_program.put("20", st);
        
        theApp.m_program.put("30", new JProg.service.JProgServPauseStatement( 3000));
        
        theApp.m_program.put("40", new JProg.vacuum.JProgVacuumClose(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "14E")));
        */
        
        
        
        /*
        theApp.m_program.put( 10, new JProg.service.JProgServMessageStatement(
                "Демонстрация включения-выключения насосов.",
                "",
                "", 5));
        
        
        JProgVacuumTurnOn st = new JProgVacuumTurnOn(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "001"));
        theApp.m_program.put( 20, st);
        
        theApp.m_program.put( 30, new JProg.service.JProgServPauseStatement( 10000));
        
        theApp.m_program.put( 40, new JProg.vacuum.JProgVacuumTurnOn(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "003")
            ));
        */
        
        /*
        theApp.m_program.put( 45, new JProg.vacuum.JProgVacuumWait(
                "003.02", 80000, 1000, JProgVacuumWait.VAC_WAIT_APPROACH_FROM_DOWNSIDE));
        
        theApp.m_program.put( 50, new JProg.service.JProgServPauseStatement( 5000));
        
        theApp.m_program.put( 60, new JProg.vacuum.JProgVacuumOpen(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "08A")
            ));
        
        theApp.m_program.put( 70, new JProg.service.JProgServPauseStatement( 60000));
        
        theApp.m_program.put( 80, new JProg.vacuum.JProgVacuumClose(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "012")
            ));
        
        theApp.m_program.put( 90, new JProg.service.JProgServPauseStatement( 10000));
        
        theApp.m_program.put( 100, new JProg.vacuum.JProgVacuumOpen(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "08C")
            ));
        
        theApp.m_program.put( 110, new JProg.service.JProgServPauseStatement( 10000));
        
        theApp.m_program.put( 120, new JProg.vacuum.JProgVacuumOpen(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "09A")
            ));
        
        theApp.m_program.put( 130, new JProg.service.JProgServPauseStatement( 10000));
        
        theApp.m_program.put( 140, new JProg.vacuum.JProgVacuumOpen(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "11A")
            ));
        
        theApp.m_program.put( 150, new JProg.service.JProgServPauseStatement( 10000));
        
        theApp.m_program.put( 160, new JProg.vacuum.JProgVacuumClose(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "11A")
            ));
        
        theApp.m_program.put( 170, new JProg.vacuum.JProgVacuumClose(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "08A")
            ));
        
        theApp.m_program.put( 180, new JProg.service.JProgServPauseStatement( 10000));
        
        theApp.m_program.put( 190, new JProg.vacuum.JProgVacuumClose(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "09A")
            ));
        
        theApp.m_program.put( 200, new JProg.vacuum.JProgVacuumClose(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "08C")
            ));
        
        theApp.m_program.put( 210, new JProg.service.JProgServPauseStatement( 10000));
        
        theApp.m_program.put( 220, new JProg.vacuum.JProgVacuumTurnOff(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "001")));
        
        theApp.m_program.put( 230, new JProg.vacuum.JProgVacuumTurnOff(
                ( HVV_VacuumDevice) HVV_VacuumDevices.getInstance().m_devices.get( "003")));
        */
        
        
        /*
        theApp.m_program.put( 10, new JProg.service.JProgServMessageStatement(
                "Демонстрация включения-выключения насосов.",
                "",
                "", 5));
        
        theApp.m_program.put( 20, new JProg.service.JProgServMessageStatement(
                "Демонстрация включения-выключения насосов.",
                "",
                "", 5));
        
        theApp.m_program.put( 100, new JProg.service.JProgServMessageStatement(
                "Демонстрация включения-выключения насосов.",
                "",
                "", 5));
        
        theApp.m_program.put( 200, new JProg.service.JProgServMessageStatement(
                "Демонстрация включения-выключения насосов.",
                "",
                "", 5));
        
        theApp.m_program.put( 1000, new JProg.service.JProgServMessageStatement(
                "Демонстрация включения-выключения насосов.",
                "",
                "", 5));
        
        theApp.m_program.put( 2000, new JProg.service.JProgServMessageStatement(
                "Демонстрация включения-выключения насосов.",
                "",
                "", 5));
        */
        
        if(      Logger.getRootLogger().getLevel() == Level.TRACE) btnTogTrace.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == Level.DEBUG) btnTogDebug.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == Level.INFO)  btnTogInfo.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == Level.WARN)  btnTogWarn.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == Level.ERROR) btnTogError.setSelected( true);
        else btnTogFatal.setSelected( true);

        lstProgram.setVisible( false);
        ShowProgram();
    }

    public final void ShowProgram() {
        m_pnlProgram.CreateNewPanel( theApp.m_program.size());
        
        if( m_pnlProgram.getBounds().height > 860) {
            scrlBarProgram.setVisible( true);
            scrlBarProgram.setMaximum( m_pnlProgram.getBounds().height - 330 + 11);
        }
        else
            scrlBarProgram.setVisible( false);
        
        Set set = theApp.m_program.entrySet();
        Iterator it = set.iterator();
        Iterator itLabels = m_pnlProgram.m_llLabels.iterator();
        
        lstModel.clear();
        
        while( it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int nLine = ( int) entry.getKey();
            JProg.JProgAStatement abstractStatement = ( JProg.JProgAStatement) entry.getValue();
            String strStatement = nLine + " " + abstractStatement.GetAsString();
            lstModel.addElement( strStatement);
            if( itLabels.hasNext()) {
                JLabel lbl = ( JLabel) itLabels.next();
                lbl.setText( strStatement);
                
                m_pnlProgram.m_mapLabels.put( nLine, lbl);
            }
        }
        
        if( lstProgram != null) lstProgram.invalidate();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstProgram = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        btnLoad = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        lblProgramFilePath = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblDateProgramStart = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblDateTimeDuration = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblCurrentExecutableStatement = new javax.swing.JLabel();
        m_lblCaptionHvComm = new javax.swing.JLabel();
        m_lblLedHvComm = new javax.swing.JLabel();
        m_lblLedVacComm = new javax.swing.JLabel();
        m_lblCaptionVacComm = new javax.swing.JLabel();
        m_lblLedPollerComm = new javax.swing.JLabel();
        m_lblCaptionPollerComm = new javax.swing.JLabel();
        btnPause = new javax.swing.JButton();
        m_lblLedAdminTo = new javax.swing.JLabel();
        m_lblCaptionAdminComm = new javax.swing.JLabel();
        m_lblLedAdminFrom = new javax.swing.JLabel();
        btnTogTrace = new javax.swing.JToggleButton();
        btnTogDebug = new javax.swing.JToggleButton();
        btnTogInfo = new javax.swing.JToggleButton();
        btnTogWarn = new javax.swing.JToggleButton();
        btnTogError = new javax.swing.JToggleButton();
        btnTogFatal = new javax.swing.JToggleButton();
        pnlShowFrame = new javax.swing.JPanel();
        scrlBarProgram = new javax.swing.JScrollBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(840, 2147483647));
        setMinimumSize(new java.awt.Dimension(840, 1020));
        setResizable(false);
        getContentPane().setLayout(null);

        lstProgram.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        lstProgram.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(lstProgram);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 1000, 420, 20);

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        jLabel1.setText("<html><b><u>Исполняемая программа автоматизации:</b></u></html>");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(12, 99, 1116, 18);

        btnLoad.setText("<html><center>Загрузить программу</center></html>");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });
        getContentPane().add(btnLoad);
        btnLoad.setBounds(100, 470, 270, 78);

        btnStart.setText("<html><center>Запуск</center></html>");
        btnStart.setEnabled(false);
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        getContentPane().add(btnStart);
        btnStart.setBounds(100, 660, 270, 78);

        btnExit.setText("<html><center>Выход</center></html>");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit);
        btnExit.setBounds(480, 880, 270, 78);

        btnStop.setText("<html><center>Стоп</center></html>");
        btnStop.setEnabled(false);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        getContentPane().add(btnStop);
        btnStop.setBounds(100, 880, 270, 78);
        getContentPane().add(lblProgramFilePath);
        lblProgramFilePath.setBounds(710, 85, 418, 0);

        jLabel2.setText("Дата-время запуска программы:");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(490, 470, 270, 31);

        lblDateProgramStart.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        lblDateProgramStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDateProgramStart.setText("--.--.--     --:--:--");
        getContentPane().add(lblDateProgramStart);
        lblDateProgramStart.setBounds(490, 500, 270, 40);

        jLabel4.setText("Время исполнения программы:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(490, 540, 270, 31);

        lblDateTimeDuration.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        lblDateTimeDuration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDateTimeDuration.setText("-- : -- : --");
        getContentPane().add(lblDateTimeDuration);
        lblDateTimeDuration.setBounds(490, 570, 270, 40);

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        jLabel6.setText("<html><b><u>Текущая исполняемая команда</b></u></html>");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(12, 5, 692, 18);

        lblCurrentExecutableStatement.setFont(new java.awt.Font("Cantarell", 0, 40)); // NOI18N
        getContentPane().add(lblCurrentExecutableStatement);
        lblCurrentExecutableStatement.setBounds(30, 30, 790, 50);

        m_lblCaptionHvComm.setText("<html>Связь с модулем управления<br>высоковольтной частью</html>");
        getContentPane().add(m_lblCaptionHvComm);
        m_lblCaptionHvComm.setBounds(540, 610, 210, 50);

        m_lblLedHvComm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(m_lblLedHvComm);
        m_lblLedHvComm.setBounds(490, 620, 40, 30);

        m_lblLedVacComm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(m_lblLedVacComm);
        m_lblLedVacComm.setBounds(490, 670, 40, 30);

        m_lblCaptionVacComm.setText("<html>Связь с модулем управления<br>вакуумной частью</html>");
        getContentPane().add(m_lblCaptionVacComm);
        m_lblCaptionVacComm.setBounds(540, 660, 210, 50);

        m_lblLedPollerComm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(m_lblLedPollerComm);
        m_lblLedPollerComm.setBounds(490, 720, 40, 30);

        m_lblCaptionPollerComm.setText("<html>Связь с модулем<br>сбора данных</html>");
        getContentPane().add(m_lblCaptionPollerComm);
        m_lblCaptionPollerComm.setBounds(540, 710, 210, 50);

        btnPause.setText("<html><center>Пауза</center></html>");
        btnPause.setEnabled(false);
        btnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });
        getContentPane().add(btnPause);
        btnPause.setBounds(100, 770, 270, 78);

        m_lblLedAdminTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(m_lblLedAdminTo);
        m_lblLedAdminTo.setBounds(490, 810, 40, 30);

        m_lblCaptionAdminComm.setText("<html>Связь с административным<br>модулем</html>");
        getContentPane().add(m_lblCaptionAdminComm);
        m_lblCaptionAdminComm.setBounds(540, 780, 210, 50);

        m_lblLedAdminFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(m_lblLedAdminFrom);
        m_lblLedAdminFrom.setBounds(490, 770, 40, 30);

        buttonGroup1.add(btnTogTrace);
        btnTogTrace.setText("T");
        btnTogTrace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogTraceActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogTrace);
        btnTogTrace.setBounds(480, 850, 40, 28);

        buttonGroup1.add(btnTogDebug);
        btnTogDebug.setText("D");
        btnTogDebug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogDebugActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogDebug);
        btnTogDebug.setBounds(530, 850, 40, 28);

        buttonGroup1.add(btnTogInfo);
        btnTogInfo.setText("I");
        btnTogInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogInfoActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogInfo);
        btnTogInfo.setBounds(580, 850, 40, 28);

        buttonGroup1.add(btnTogWarn);
        btnTogWarn.setText("W");
        btnTogWarn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogWarnActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogWarn);
        btnTogWarn.setBounds(630, 850, 40, 28);

        buttonGroup1.add(btnTogError);
        btnTogError.setText("E");
        btnTogError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogErrorActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogError);
        btnTogError.setBounds(680, 850, 40, 28);

        buttonGroup1.add(btnTogFatal);
        btnTogFatal.setText("F");
        btnTogFatal.setToolTipText("");
        btnTogFatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogFatalActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogFatal);
        btnTogFatal.setBounds(720, 850, 40, 28);

        pnlShowFrame.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210)));
        pnlShowFrame.setMaximumSize(new java.awt.Dimension(795, 332));
        pnlShowFrame.setMinimumSize(new java.awt.Dimension(795, 332));
        pnlShowFrame.setPreferredSize(new java.awt.Dimension(795, 332));
        pnlShowFrame.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                pnlShowFrameMouseWheelMoved(evt);
            }
        });
        pnlShowFrame.setLayout(null);
        getContentPane().add(pnlShowFrame);
        pnlShowFrame.setBounds(10, 120, 795, 332);

        scrlBarProgram.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                scrlBarProgramAdjustmentValueChanged(evt);
            }
        });
        getContentPane().add(scrlBarProgram);
        scrlBarProgram.setBounds(810, 120, 19, 330);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter( new MyXMLFilter());
        
        String strCurrentProgram = theApp.GetAMSRoot() + "/ReadyPrograms";
        fc.setCurrentDirectory( new File( strCurrentProgram));
        
        int returnVal = fc.showOpenDialog( this);
        if( returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            
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
                    ShowProgram();
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
        
        } else {
            logger.info("LoadProgram cancelled.");
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        btnStart.setEnabled( false);
        logger.info( "Starting Exec program");
        new Thread( theApp.m_MainExeThread).start();
        theApp.m_nRunningState = HVV_Executor.RUNNING_STATE_RUN;
        theApp.SetExProgramStartDateAsCurrent();
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        ledsRefresher.lightLedsStop();
        theApp.m_MainExeThread.StopThread();
        
        theApp.m_StateInformerThread.StopThread();
        theApp.GetCommE2A().stop( true);
        
        theApp.GetCommA2E().stop();
        
        theApp.GetCommHv().stop();
        theApp.GetCommVac().stop();
        theApp.GetCommE2P().stop( true);
        
        dispose();
        System.exit( 0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        theApp.m_MainExeThread.StopThread();
        theApp.m_nRunningState = HVV_Executor.RUNNING_STATE_STOP;
        theApp.m_pMainWnd.lblCurrentExecutableStatement.setText( "Программа остановлена пользователем.");
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
        throw new UnsupportedOperationException( "Пауза исполнения программы пока не сделана");
    }//GEN-LAST:event_btnPauseActionPerformed

    private void btnTogTraceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogTraceActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.TRACE) {
            logger.info( "Switching log level to TRACE");
            Logger.getRootLogger().setLevel( Level.TRACE);
        }
    }//GEN-LAST:event_btnTogTraceActionPerformed

    private void btnTogDebugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogDebugActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.DEBUG) {
            logger.info( "Switching log level to DEBUG");
            Logger.getRootLogger().setLevel( Level.DEBUG);
        }
    }//GEN-LAST:event_btnTogDebugActionPerformed

    private void btnTogInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogInfoActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.INFO) {
            logger.info( "Switching log level to INFO");
            Logger.getRootLogger().setLevel( Level.INFO);
        }
    }//GEN-LAST:event_btnTogInfoActionPerformed

    private void btnTogWarnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogWarnActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.WARN) {
            logger.info( "Switching log level to WARN");
            Logger.getRootLogger().setLevel( Level.WARN);
        }
    }//GEN-LAST:event_btnTogWarnActionPerformed

    private void btnTogErrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogErrorActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.ERROR) {
            logger.info( "Switching log level to ERROR");
            Logger.getRootLogger().setLevel( Level.ERROR);
        }
    }//GEN-LAST:event_btnTogErrorActionPerformed

    private void btnTogFatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogFatalActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.FATAL) {
            logger.info( "Switching log level to FATAL");
            Logger.getRootLogger().setLevel( Level.FATAL);
        }
    }//GEN-LAST:event_btnTogFatalActionPerformed

    private void pnlShowFrameMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_pnlShowFrameMouseWheelMoved
        if( scrlBarProgram.isVisible()) {
            int nMin = scrlBarProgram.getMinimum();
            int nMax = scrlBarProgram.getMaximum();
            int nPos = scrlBarProgram.getValue();
            int nStep1 = scrlBarProgram.getBlockIncrement();
            int nStep2 = scrlBarProgram.getUnitIncrement();

            int nNextPos = nPos + evt.getWheelRotation() * nStep1;

            if( nNextPos < nMin) nNextPos = nMin;
            if( nNextPos > nMax) nNextPos = nMax;
            scrlBarProgram.setValue( nNextPos);
        }
    }//GEN-LAST:event_pnlShowFrameMouseWheelMoved

    private void scrlBarProgramAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_scrlBarProgramAdjustmentValueChanged
        if( m_pnlProgram != null)
            m_pnlProgram.setBounds( 5, 2 - scrlBarProgram.getValue(), 790, 330 + scrlBarProgram.getValue());
    }//GEN-LAST:event_scrlBarProgramAdjustmentValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(FrmMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //WRONG WAY - run from ConstrApp
                new FrmMainWindow( new HVV_Executor()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    public javax.swing.JButton btnLoad;
    public javax.swing.JButton btnPause;
    public javax.swing.JButton btnStart;
    public javax.swing.JButton btnStop;
    private javax.swing.JToggleButton btnTogDebug;
    private javax.swing.JToggleButton btnTogError;
    private javax.swing.JToggleButton btnTogFatal;
    private javax.swing.JToggleButton btnTogInfo;
    private javax.swing.JToggleButton btnTogTrace;
    private javax.swing.JToggleButton btnTogWarn;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lblCurrentExecutableStatement;
    public javax.swing.JLabel lblDateProgramStart;
    public javax.swing.JLabel lblDateTimeDuration;
    private javax.swing.JLabel lblProgramFilePath;
    public javax.swing.JList lstProgram;
    private javax.swing.JLabel m_lblCaptionAdminComm;
    private javax.swing.JLabel m_lblCaptionHvComm;
    private javax.swing.JLabel m_lblCaptionPollerComm;
    private javax.swing.JLabel m_lblCaptionVacComm;
    public javax.swing.JLabel m_lblLedAdminFrom;
    public javax.swing.JLabel m_lblLedAdminTo;
    public javax.swing.JLabel m_lblLedHvComm;
    public javax.swing.JLabel m_lblLedPollerComm;
    public javax.swing.JLabel m_lblLedVacComm;
    private javax.swing.JPanel pnlShowFrame;
    public javax.swing.JScrollBar scrlBarProgram;
    // End of variables declaration//GEN-END:variables
}
