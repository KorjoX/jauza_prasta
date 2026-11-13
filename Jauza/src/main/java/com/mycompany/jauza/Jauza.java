package com.mycompany.jauza;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.swing.JFrame;

public class Jauza {
    public static void main(String[] args) {
        System.out.println("Menyalakan Mesin Spring Framework...");
        
        // 1. Inisialisasi Spring (Spring akan membaca AppConfig dan semua kelas DAO secara otomatis)
        ApplicationContext context = new AnnotationConfigApplicationContext("com.mycompany.jauza");
        
        // 2. Menarik DAO yang sudah dibentuk oleh memori Spring
        AdminDAO adminDAO = context.getBean(AdminDAO.class);
        PelangganDAO pelangganDAO = context.getBean(PelangganDAO.class);
        LayananDAO layananDAO = context.getBean(LayananDAO.class);
        TransaksiDAO transaksiDAO = context.getBean(TransaksiDAO.class);
        
        // 3. Menjalankan Tampilan Antarmuka GUI Swing
        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Login - Jauza");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Menyuntikkan DAO ke halaman Login
            frame.getContentPane().add(new panelform(adminDAO, pelangganDAO, layananDAO, transaksiDAO));
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}