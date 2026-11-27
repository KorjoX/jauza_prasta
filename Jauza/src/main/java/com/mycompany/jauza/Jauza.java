package com.mycompany.jauza;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.swing.JFrame;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import java.awt.Font;

public class Jauza {
    public static void main(String[] args) {
        // 1. Aktifkan tema FlatLaf Modern (Light)
        try {
            FlatLightLaf.setup();
            
            // Membuat sudut tombol, input teks, dan scrollbar membulat
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 10);
            
            // Warna aksen biru senada dengan laporan Laundry Jauza (#0066CC)
            UIManager.put("Component.accentColor", new java.awt.Color(0, 102, 204));
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
        } catch (Exception e) {
            System.err.println("Gagal memuat tema FlatLaf: " + e.getMessage());
        }
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