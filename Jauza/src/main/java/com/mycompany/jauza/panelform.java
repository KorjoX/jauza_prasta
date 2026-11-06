package com.mycompany.jauza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class panelform extends javax.swing.JPanel {

    public panelform() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jLabel1.setText("username :");
        jLabel2.setText("password  ;");

        jButton1.setText("Kirim");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Admin Login");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jLabel3)))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(66, 66, 66)
                .addComponent(jButton1)
                .addContainerGap(90, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String username = jTextField1.getText();
        String password = jTextField2.getText(); 

        try {
            Connection conn = koneksi.getConnection();
            
            if (conn != null) {
                String sql = "SELECT * FROM admin WHERE username=? AND password=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, username);
                pst.setString(2, password);
                
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Berhasil!");
                    
                    // --- KODE PINDAH HALAMAN ---
                    // 1. Menutup form login saat ini
                    javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
                    
                    // 2. Membuka form menu utama
                    new MenuUtama().setVisible(true);
                    // ---------------------------
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Username atau Password salah!");
                }
                
                rs.close();
                pst.close();
                conn.close();
            } else {
                JOptionPane.showMessageDialog(this, "Koneksi ke database Laragon terputus!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi Error: " + e.getMessage());
        }
    }                                        

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration                   
}