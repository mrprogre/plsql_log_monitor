import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        //button.setText(label);
        button.setText("");
        button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("res/icons/apply.png"))));
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            JFileChooser save_to = new JFileChooser();
            save_to.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
            int ret = save_to.showDialog(null, "Save");
            if (ret == 0) {
                try {
                    File selected = save_to.getSelectedFile();
                    String file_absolute_path = selected.getAbsolutePath();

                    String v_row = Oracle.row_ids.get(Gui.executeTable.getSelectedRow());
                    String v_column = Oracle.headers.get(Gui.executeTable.getSelectedColumn());

                    String v_sql = "SELECT " + v_column + " FROM " + Gui.tableNamesBox.getSelectedItem()
                            + " where ROWID = '" + v_row + "'";
                    Statement st = Oracle.connect.createStatement();
                    ResultSet rs = st.executeQuery(v_sql);

                    while (rs.next()) {
                        String v_header = Oracle.headers.get(Gui.executeTable.getSelectedColumn());
                        switch (Oracle.types.get(Gui.executeTable.getSelectedColumn())) {
                            case "BLOB":
                                Blob blob = rs.getBlob(v_header);
                                Common.getBlobFromTable(file_absolute_path, blob);
                                break;
                            case "CLOB":
                                Clob clob = rs.getClob(v_header);
                                Common.getBlobFromTable(file_absolute_path, clob);
                                break;
                        }
                    }
                    Common.notification("file saved");
                } catch (NullPointerException n){
                    Common.notification("file is empty");
                } catch (Exception e) {
                    e.printStackTrace();
                    Common.notification("file save error");
                }
            } else {
                Common.notification("file save canceled");
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}