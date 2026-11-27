package com.mycompany.jauza;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

public class ReportManager {

    private static final String REPORT_PATH = "/reports/LaporanTransaksi.jasper";
    private static final String NOTA_PATH = "/reports/NotaTransaksi.jasper";

    public static void tampilkanLaporan() {
        try (Connection conn = KoneksiReport.getConnection()) {
            InputStream reportStream = ReportManager.class.getResourceAsStream(REPORT_PATH);

            if (reportStream == null) {
                JOptionPane.showMessageDialog(null,
                        "File laporan tidak ditemukan di: " + REPORT_PATH,
                        "Error File", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, new HashMap<>(), conn);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Laporan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void cetakNota(int idTransaksi) {
        try (Connection conn = KoneksiReport.getConnection()) {
            InputStream reportStream = ReportManager.class.getResourceAsStream(NOTA_PATH);

            if (reportStream == null) {
                JOptionPane.showMessageDialog(null,
                        "File nota tidak ditemukan di: " + NOTA_PATH,
                        "Error File", JOptionPane.ERROR_MESSAGE);
                return;
            }

            HashMap<String, Object> params = new HashMap<>();
            params.put("P_ID_TRANSAKSI", idTransaksi);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, conn);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Cetak Nota: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void eksporKePDF(JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Sebagai PDF");
        fileChooser.setSelectedFile(new java.io.File("Laporan_Transaksi_Jauza.pdf"));

        if (fileChooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            String outputPath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!outputPath.toLowerCase().endsWith(".pdf")) {
                outputPath += ".pdf";
            }

            try (Connection conn = KoneksiReport.getConnection()) {
                InputStream reportStream = ReportManager.class.getResourceAsStream(REPORT_PATH);
                if (reportStream == null) {
                    JOptionPane.showMessageDialog(parentFrame, "File .jasper tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, new HashMap<>(), conn);
                JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

                JOptionPane.showMessageDialog(parentFrame, "Laporan berhasil diekspor ke:\n" + outputPath, "Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(parentFrame, "Gagal ekspor PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}