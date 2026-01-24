package gui.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Utility class để hiển thị các dialog thông báo.
 * Class này cung cấp các phương thức static để dễ dàng sử dụng ở mọi nơi.
 */
public class DialogHelper {

    // ========================================
    // BASIC DIALOGS
    // ========================================

    /**
     * Hiển thị dialog lỗi
     * 
     * @param parent  Component cha (có thể null)
     * @param message Nội dung thông báo
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Hiển thị dialog cảnh báo
     * 
     * @param parent  Component cha (có thể null)
     * @param message Nội dung thông báo
     */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Hiển thị dialog thành công
     * 
     * @param parent  Component cha (có thể null)
     * @param message Nội dung thông báo
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Hiển thị dialog thông tin
     * 
     * @param parent  Component cha (có thể null)
     * @param message Nội dung thông báo
     */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    // ========================================
    // CONFIRM DIALOGS
    // ========================================

    /**
     * Hiển thị dialog xác nhận
     * 
     * @param parent  Component cha (có thể null)
     * @param message Nội dung xác nhận
     * @return true nếu người dùng chọn Yes
     */
    public static boolean showConfirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * Hiển thị dialog xác nhận với tiêu đề tùy chỉnh
     * 
     * @param parent  Component cha
     * @param message Nội dung xác nhận
     * @param title   Tiêu đề dialog
     * @return true nếu người dùng chọn Yes
     */
    public static boolean showConfirm(Component parent, String message, String title) {
        return JOptionPane.showConfirmDialog(parent, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * Hiển thị dialog xác nhận xóa
     * 
     * @param parent   Component cha
     * @param itemName Tên item cần xóa
     * @return true nếu người dùng chọn Yes
     */
    public static boolean showDeleteConfirm(Component parent, String itemName) {
        return JOptionPane.showConfirmDialog(parent,
                "Bạn có chắc muốn xóa " + itemName + " này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * Hiển thị dialog xác nhận xóa với cảnh báo thêm
     * 
     * @param parent   Component cha
     * @param itemName Tên item cần xóa
     * @param warning  Cảnh báo thêm (ví dụ: "Sẽ xóa cả dữ liệu liên quan")
     * @return true nếu người dùng chọn Yes
     */
    public static boolean showDeleteConfirm(Component parent, String itemName, String warning) {
        String message = "Bạn có chắc muốn xóa " + itemName + " này?\n" + warning;
        return JOptionPane.showConfirmDialog(parent, message,
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }


    // ========================================
    // ADVANCED DIALOGS
    // ========================================

    /**
     * Hiển thị dialog với Yes/No/Cancel
     * 
     * @param parent  Component cha
     * @param message Nội dung
     * @return JOptionPane.YES_OPTION, NO_OPTION, hoặc CANCEL_OPTION
     */
    public static int showYesNoCancelDialog(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Xác nhận",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Hiển thị dialog với custom buttons
     * 
     * @param parent  Component cha
     * @param message Nội dung
     * @param title   Tiêu đề
     * @param options Các lựa chọn button
     * @return Index của button được chọn, hoặc CLOSED_OPTION
     */
    public static int showCustomDialog(Component parent, String message, String title, Object[] options) {
        return JOptionPane.showOptionDialog(parent, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
    }

    // ========================================
    // OVERLOAD METHODS (không cần parent)
    // ========================================

    public static void showError(String message) {
        showError(null, message);
    }

    public static void showWarning(String message) {
        showWarning(null, message);
    }

    public static void showSuccess(String message) {
        showSuccess(null, message);
    }

    public static void showInfo(String message) {
        showInfo(null, message);
    }

    public static boolean showConfirm(String message) {
        return showConfirm(null, message);
    }

    public static boolean showDeleteConfirm(String itemName) {
        return showDeleteConfirm(null, itemName);
    }

}