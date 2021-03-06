/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor;

import java.awt.Font;
import java.util.LinkedList;
import java.util.TreeMap;
import javax.swing.JLabel;

/**
 *
 * @author yaroslav
 */
public class PanelProgramShow extends javax.swing.JPanel {

    /**
     * Creates new form PanelProgramShow
     */
    
    public final LinkedList m_llLabels;
    public final TreeMap m_mapLabels;
    
    private int m_nItems;
    public void SetItems( int nItems) { m_nItems = nItems;}
    
    
    public PanelProgramShow() {
        initComponents();
        
        m_llLabels = new LinkedList();
        m_mapLabels = new TreeMap();
        
        /*for( int i=0; i<100; i++) {
            JLabel lbl = new javax.swing.JLabel();

            lbl.setText( "" + i);
            lbl.setBorder( javax.swing.BorderFactory.createLineBorder( new java.awt.Color(230, 230, 230)));
            add( lbl);
            lbl.setBounds( 0, 40 * i, 490, 40);
            
            m_llLabels.add( lbl);
        }*/
        
        
        m_nItems = 0;
    }

    public void CreateNewPanel( int nLen) {
        m_nItems = nLen;
        
        m_llLabels.clear();
        m_mapLabels.clear();
        
        removeAll();
        
        Font fnt = new Font( "Monospaced", 0, 18);
        
        for( int i=0; i<nLen; i++) {
            JLabel lbl = new javax.swing.JLabel();

            lbl.setText( "");//Строка №" + i);
            lbl.setBorder( javax.swing.BorderFactory.createLineBorder( new java.awt.Color(230, 230, 230)));
            add( lbl);
            lbl.setBounds( 1, 1 + 30 * i, 788, 30);
            
            
            lbl.setFont( fnt);
            lbl.setOpaque(true);
            
            m_llLabels.add( lbl);
        }
        
        setBounds( 5, 2, 790, nLen * 30 + 2);
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

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 0, 0)));
        setMaximumSize(new java.awt.Dimension(790, 800));
        setMinimumSize(new java.awt.Dimension(790, 800));
        setPreferredSize(new java.awt.Dimension(790, 800));
        setLayout(null);

        jLabel1.setBackground(new java.awt.Color(170, 122, 73));
        jLabel1.setFont(new java.awt.Font("DialogInput", 0, 18)); // NOI18N
        jLabel1.setText("Это очень русский текст");
        add(jLabel1);
        jLabel1.setBounds(48, 286, 320, 230);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
