package com.mycompany.jauza;

public class Jauza {
    public static void main(String[] args) {
        // Cek koneksi di latar belakang
        koneksi.getConnection();

        // Membuka form login (panelform)
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                javax.swing.JFrame frame = new javax.swing.JFrame("Admin Login - Jauza");
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new panelform());
                frame.pack();
                frame.setLocationRelativeTo(null); // Agar form muncul di tengah layar
                frame.setVisible(true);
            }
        });
    }
}
