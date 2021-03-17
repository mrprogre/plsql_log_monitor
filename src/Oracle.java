import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Oracle {
    static AtomicBoolean isStop = new AtomicBoolean(false);
    static boolean isConnectedToVPN = false;
    static AtomicBoolean isSearchFinished;
    static Connection connect;
    static double searchTime;
    static double timeStart;
    static int amount;
    static int max;
    static int max2;
    static String column_name;
    static ArrayList<String> user_tables = new ArrayList<>();
    static ArrayList <String> headers = new ArrayList<>();
    static int column_count;
    static int columnCount;
    static String data_type;
    static int data_type_count;
    static ArrayList<String> types = new ArrayList<>();
    static Set<String> blobs = new LinkedHashSet<>();
    static ArrayList<String> row_ids = new ArrayList<>();
    static Object platformValue;
    static boolean isAnalyseFihished;
    static String user = "";
    static String password = "";

    // открытие соединения
    static void open() {
        try {
            //String password = String.valueOf(Gui.passwordField.getPassword());
            new String(Gui.passwordField.getPassword());
            Class.forName("oracle.jdbc.driver.OracleDriver");
            platformValue = Gui.devProd.getSelectedItem();
            if (platformValue != null) {
                if (platformValue.equals("dev")) {
                    connect = DriverManager.getConnection("jdbc:oracle:thin:@10.8.0.1:1521/orcl", user, password);
                    Common.notification("connected to DEV");
                    isConnectedToVPN = true;
                } else if (platformValue.equals("test")) {
                    connect = DriverManager.getConnection("jdbc:oracle:thin:@172.16.46.12:1521/asuab", user, password);
                    Common.notification("connected to TEST");
                    isConnectedToVPN = true;
                } else {
                    connect = DriverManager.getConnection("jdbc:oracle:thin:@172.16.46.10:1521/asuabsys", user, password);
                    Common.notification("connected to PROD");
                    isConnectedToVPN = true;
                }
            }
            if (isConnectedToVPN){
                getUserTables();
                Common.addItemsToCombobox();
                Common.isTabInFavorites();
            }
            Gui.passwordField.setText("");
        } catch (Exception e) {
            Common.notification("check VPN or input correct password");
        }
    }

    // закрытие соединения
    static void close() {
        try {
            if (isConnectedToVPN) connect.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // выборка данных из лога
    static void select() {
        try {
            if (isConnectedToVPN) {
                timeStart = (double)System.currentTimeMillis();
                isSearchFinished = new AtomicBoolean(false);
                String where_clause = Gui.textWhereClause.getText();
                Object order_by = Gui.comboBoxOrderBy.getSelectedItem();
                Object sorting = Gui.comboBoxSorting.getSelectedItem();
                if (Gui.isSelect) {
                    Common.notification("auto update data");
                    Statement statement = connect.createStatement();
                    String sql = "select max(fl_id) from fs_log where fl_date > sysdate - " + where_clause;
                    ResultSet rs_id = statement.executeQuery(sql);
                    if (rs_id.next()) {
                        max = rs_id.getInt(1);
                    }
                    statement.close();

                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException var15) {
                        var15.printStackTrace();
                    }

                    Statement statement2 = connect.createStatement();
                    String sql2 = "select max(fl_id) from fs_log where fl_date > sysdate - " + where_clause;
                    ResultSet rs_id2 = statement2.executeQuery(sql2);
                    if (rs_id2.next()) {
                        max2 = rs_id2.getInt(1);
                    }

                    statement2.close();
                    if (max != max2) {
                        PreparedStatement st = connect.prepareStatement(Gui.JStatement.getText() + where_clause + " AND fl_text LIKE '" + Gui.likeTextField.getText() + "' ORDER BY " + order_by + " " + sorting);
                        ResultSet rs = st.executeQuery();

                        while(rs.next()) {
                            if (isStop.get()) {
                                return;
                            }
                            amount = Gui.model.getRowCount();
                            Gui.sumLbl.setText("--------  " + amount);
                            String fl_date = rs.getString("fl_date");
                            String fl_text = rs.getString("fl_text");
                            int fl_id = rs.getInt("fl_id");
                            if (fl_id > max) {
                                Object[] row = new Object[]{amount + 1, fl_date, fl_text};
                                Gui.model.addRow(row);
                            }
                        }
                        st.close();
                        isSearchFinished.set(true);
                    }
                } else {
                    Common.Searching(isSearchFinished);
                    PreparedStatement st = connect.prepareStatement(Gui.JStatement.getText() + where_clause + " AND fl_text LIKE '" + Gui.likeTextField.getText() + "' ORDER BY " + order_by + " " + sorting);
                    ResultSet rs = st.executeQuery();

                    while(rs.next()) {
                        if (isStop.get()) {
                            return;
                        }

                        amount = Gui.model.getRowCount() + 1;
                        Gui.sumLbl.setText("--------  " + amount);
                        String fl_date = rs.getString("fl_date");
                        String fl_text = rs.getString("fl_text");
                        Object[] row = new Object[]{amount, fl_date, fl_text};
                        Gui.model.addRow(row);
                    }

                    st.close();
                    isSearchFinished.set(true);
                    double timeEnd = (double)System.currentTimeMillis();
                    searchTime = (timeEnd - timeStart) / 1000.0D;
                    DecimalFormat f = new DecimalFormat("#0.00");
                    Common.notification("search completed in " + f.format(searchTime) + " s.");
                }
            } else {
                isStop.set(true);
                Common.notification("no connection to VPN");
            }
        } catch (NullPointerException var16) {
            isStop.set(true);
            isSearchFinished.set(true);
            Common.notification("nullPointerException");
        } catch (SQLException var17) {
            isStop.set(true);
            isSearchFinished.set(true);
            Common.notification("VPN connection closed");
        }
    }

    //Список всех таблиц текущего пользователя
    static void getUserTables() {
        try {
            PreparedStatement st = connect.prepareStatement("SELECT table_name FROM user_tables ORDER BY table_name");
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                String tables = rs.getString("table_name");
                user_tables.add(tables);
            }
            st.close();
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }

    // Анализ данных на уникальность
    static synchronized void selectUniqueItems() {
        try {
            List<String> columns = new ArrayList<>();
            if (types.size() > 0) types.clear();
            PreparedStatement st = connect.prepareStatement("SELECT column_name, data_type from user_tab_columns WHERE upper(table_name) = '" +
                    Gui.tableNamesBox.getSelectedItem() + "'");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                column_name = rs.getString("column_name");
                columns.add(column_name);
                data_type = rs.getString("data_type");
                types.add(data_type);
            }
            st.close();

            data_type_count = 0;
            for (String s : columns) {
                if (types.get(data_type_count).equals("BLOB")||types.get(data_type_count).equals("CLOB")) s = null;
                PreparedStatement st_count = connect.prepareStatement("select count(distinct " + s + ") from " + Gui.tableNamesBox.getSelectedItem());
                if (s == null) s = headers.get(data_type_count);
                ResultSet rs_count = st_count.executeQuery();

                while (rs_count.next()) {
                    int rows_count = rs_count.getInt(1);

                    Object[] row = new Object[]{
                            s,
                            types.get(data_type_count),
                            rows_count
                    };
                    Gui.uniqueAnalysisModel.addRow(row);
                    data_type_count++;
                }
                st_count.close();
            }
            isAnalyseFihished = true;
            Gui.progressBar.setValue(100);
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }

    // Выборка имён столбцов и их типов
    static synchronized void tableInfo() {
        try {
            if (headers.size() > 0) headers.clear();
            if (types.size() > 0) types.clear();
            Statement st_tab = connect.createStatement();
            String sql_tab = "SELECT * FROM user_tab_columns WHERE table_name = '" + Gui.tableNamesBox.getSelectedItem() + "' order by column_id";
            ResultSet rs_tab = st_tab.executeQuery(sql_tab);
            while (rs_tab.next()) {
                column_name = rs_tab.getString(2);
                headers.add(column_name);
                data_type = rs_tab.getString("data_type");
                types.add(data_type);
                column_count++;
            }
            st_tab.close();
        } catch (SQLException sql) {
            sql.getErrorCode();
        }
    }

    // Селект из выбранной таблицы
    static synchronized void selectFromTable() {
        try {
            if (isConnectedToVPN) {
                PreparedStatement st = connect.prepareStatement("SELECT t.*, t.rowid FROM " + Gui.tableNamesBox.getSelectedItem()
                        + " t "
                        + Gui.whereSelect.getText().replace("where clause", ""));
                ResultSet rs = st.executeQuery();
                columnCount = rs.getMetaData().getColumnCount() - 1;
                if (blobs.size() > 0) blobs.clear();
                if (row_ids.size() > 0) row_ids.clear();
                while (rs.next()) {
                    if (isStop.get()) {
                        return;
                    }
                    String row_id = rs.getString("ROWID");
                    row_ids.add(row_id);

                    Object[] row = new Object[columnCount];
                    Arrays.setAll(row, x -> {
                        try {
                            switch (Oracle.types.get(x)) {
                                case "NUMBER":
                                case "INTEGER":
                                    return rs.getInt(x + 1);
                                case "BLOB":
                                    if (rs.getBlob(x + 1) == null){
                                        return "-";
                                    }
                                    return "+";
                                case "CLOB":
                                    if (rs.getClob(x + 1) == null){
                                        return "-";
                                    }
                                    return "+";
                                default:
                                    return rs.getString(x + 1);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
                    Gui.executeModel.addRow(row);
                }
                st.close();
            }
            else {
                isStop.set(true);
            }
        } catch (SQLException | NullPointerException s) {
            s.printStackTrace();
            isStop.set(true);
        }
    }
}
