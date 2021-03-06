/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor;

/**
 *
 * @author yaroslav
 */
public class DlgVacWaitExecution extends javax.swing.JDialog {

    public boolean m_bSkip;
    /**
     * Creates new form DlgVacWaitExecution
     */
    public DlgVacWaitExecution(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        m_bSkip = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblParamName = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblParamValueCurent = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblParamValueExpected = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblParamValueError = new javax.swing.JLabel();
        btnSkipWaiting = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(600, 380));
        setMinimumSize(new java.awt.Dimension(600, 380));
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setText("<html><u>Описание ожидаемого параметра:</u></html>");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 380, 20);

        lblParamName.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        lblParamName.setText("-");
        getContentPane().add(lblParamName);
        lblParamName.setBounds(10, 30, 570, 40);

        jLabel3.setText("<html><u>Текущее значение:</u></html>");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 90, 380, 20);

        lblParamValueCurent.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        lblParamValueCurent.setText("-");
        getContentPane().add(lblParamValueCurent);
        lblParamValueCurent.setBounds(10, 110, 380, 40);

        jLabel5.setText("<html><u>Ожидаемое значение</u></html>");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(10, 170, 380, 20);

        lblParamValueExpected.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        lblParamValueExpected.setText("-");
        getContentPane().add(lblParamValueExpected);
        lblParamValueExpected.setBounds(10, 190, 380, 40);

        jLabel7.setText("<html><u>Погрешность:</u></html>");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(10, 250, 380, 20);

        lblParamValueError.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        lblParamValueError.setText("-");
        getContentPane().add(lblParamValueError);
        lblParamValueError.setBounds(10, 270, 380, 40);

        btnSkipWaiting.setText("Пропустить");
        btnSkipWaiting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSkipWaitingActionPerformed(evt);
            }
        });
        getContentPane().add(btnSkipWaiting);
        btnSkipWaiting.setBounds(400, 270, 190, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSkipWaitingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSkipWaitingActionPerformed
        m_bSkip = true;
    }//GEN-LAST:event_btnSkipWaitingActionPerformed

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
            java.util.logging.Logger.getLogger(DlgVacWaitExecution.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgVacWaitExecution.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgVacWaitExecution.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgVacWaitExecution.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgVacWaitExecution dialog = new DlgVacWaitExecution(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSkipWaiting;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    public javax.swing.JLabel lblParamName;
    public javax.swing.JLabel lblParamValueCurent;
    public javax.swing.JLabel lblParamValueError;
    public javax.swing.JLabel lblParamValueExpected;
    // End of variables declaration//GEN-END:variables
}
