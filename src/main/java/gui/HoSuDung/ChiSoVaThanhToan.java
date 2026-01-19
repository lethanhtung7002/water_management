package gui.HoSuDung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.CustomerDao;
import dao.HoSuDungDao;
import model.Customer;
import model.HoSuDung;

public class ChiSoVaThanhToan extends JFrame{
    HoSuDungDao hoSuDungDao = new HoSuDungDao();

    JButton btnAdd = new JButton("Thêm Chỉ Số Nước");
    JButton btnInfo = new JButton("Xem thông tin thanh toán");

    public ChiSoVaThanhToan(HoSuDung hsd){
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        add(createInfoPanel(hsd), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        add(createTablePanel(hsd), BorderLayout.SOUTH);

    }

    private JPanel createInfoPanel(HoSuDung hsd){
        JPanel panel = new JPanel(new GridLayout(2, 4));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin Hộ Sử Dụng"));

        addInfo(panel,"Mã Hộ Sử Dụng" , String.valueOf(hsd.getID_HoSuDung()));
        addInfo(panel,"Mã Khách Hàng Của hộ" , String.valueOf(hsd.getID_Customer()));
        addInfo(panel,"Khu Vuc" , hsd.getKhuVuc());
        addInfo(panel,"Địa Chỉ" , hsd.getDiaChi());
        addInfo(panel,"Trạng Thái Hoạt Động" , hsd.getTrangThai() ==1 ? "Đang sử Dụng" : "Ngừng Hoạt Động");

        return panel;
    }

    private void addInfo(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label + " " + value);
        panel.add(lbl);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3));

        panel.add(btnAdd);
        panel.add(btnInfo);

        return panel;
    }

    private JScrollPane createTablePanel(HoSuDung hsd) {
        String[] columns = { "Ngày ghi", "Chỉ Số Củ", "Chỉ Số Mới", "Tình Trạng Thanh Toán" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setForeground(Color.WHITE);

        // Load dữ liệu từ database
        // loadHoSuDung(model, customerId);

        return new JScrollPane(table);
    }
}
