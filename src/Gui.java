import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.*;

public class Gui extends JFrame implements ActionListener {
    static JTable table;
    static DefaultTableModel model;
    static JLabel JStatement = new JLabel("SELECT * FROM fs_log WHERE fl_date > sysdate - ");
    static JLabel JStatement_2 = new JLabel("AND fl_text LIKE'");
    static JLabel orderByLbl = new JLabel("'ORDER BY");
    static JTextField likeTextField = new JTextField("%%");
    static JTextField textWhereClause = new JTextField("5/1440");
    static JLabel sumLbl;
    static JLabel statusLbl;
    static JLabel statusLbl2;
    static JLabel chbxAutoUpdateLbl;
    static JLabel timeLbl;
    static JLabel analysisLabel;
    static JComboBox<String> devProd;
    static JComboBox<String> guiTheme;
    static JComboBox<String> comboBoxOrderBy;
    static JComboBox<String> comboBoxSorting;
    static Checkbox autoUpdateCheckBox;
    static JPasswordField passwordField;
    static boolean isSelect;
    static boolean isRun;
    static String[] themeCbx = new String[]{"Gray", "Dos", "Black", "Orange", "Green", "Brown", "Blue", "Pink"};
    static String[] orderByValues = new String[]{"fl_date", "fl_text"};
    static String[] sortingValues = new String[]{"DESC", "ASC"};
    static String[] devProdValues = new String[]{"dev", "test", "prod"};
    static Font fontStyle = new Font("Tahoma", Font.BOLD, 12);
    static Color contrastColor = new Color(113, 255, 76);
    static Color fontColor = new Color(0, 0, 0);
    //---
    ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("res/icons/logo.png")));
    ImageIcon icon_yes = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("res/icons/yes.png")));
    ImageIcon icon_star = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("res/icons/star.png")));
    ImageIcon icon_star2 = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("res/icons/star2.png")));
    JButton scan_btn;
    static JButton color_btn;
    static JComboBox<String> tableNamesBox;
    static JTable uniqueAnalysisTable;
    static DefaultTableModel uniqueAnalysisModel;
    static JScrollPane uniqueAnalysisScrollPane;
    //---
    static JTable executeTable;
    static DefaultTableModel executeModel;
    static JScrollPane executeScrollPane;
    static JTextField whereSelect;
    static JLabel lblWhere;
    static JButton starBtn;
    static JButton connectionBtn;
    static boolean isAddToFavorites;
    static Checkbox favouriteTabCheckBox;
    static boolean isSelectFavouriteTab;
    static JButton showFavoriteTabBtn;
    static JButton logBtn;
    static JProgressBar progressBar;


    public Gui() {
        this.setResizable(false);
        this.setIconImage(icon.getImage());
        this.getContentPane().setBackground(new Color(255, 235, 235));
        this.setTitle("Log monitoring :mrprogre");
        this.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(500, 100, 987, 600);
        this.getContentPane().setBackground(new Color(0x789FBB));
        this.getContentPane().setLayout(null);

        //Action Listener for EXIT_ON_CLOSE
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Main.LOGGER.log(Level.INFO, "Приложение закрыто");
                if (Main.isConnectedToVPN) Main.close();
            }
        });

        // Themes list
        guiTheme = new JComboBox<>();
        guiTheme.setBounds(868, 324, 55, 23);
        guiTheme.setBackground(new Color(238, 238, 238));
        guiTheme.setFont(new Font("Tahoma", Font.BOLD, 12));
        guiTheme.setEditable(false);
        guiTheme.setModel(new DefaultComboBoxModel<>(themeCbx));
        this.getContentPane().add(guiTheme);
        //Listener
        guiTheme.addActionListener((e) -> {
            Object theme = Gui.guiTheme.getSelectedItem();
            if ("Dos".equals(theme)) {
                this.getContentPane().setBackground(new Color(0x0100AB));
                Themes.themeFontColors("Dos");
            } else if ("Black".equals(theme)) {
                this.getContentPane().setBackground(new Color(0x000000));
                Themes.themeFontColors("Black");
            } else if ("Orange".equals(theme)) {
                this.getContentPane().setBackground(new Color(0xFFB273));
                Themes.themeFontColors("Orange");
            } else if ("Green".equals(theme)) {
                this.getContentPane().setBackground(new Color(0xADE9B4));
                Themes.themeFontColors("Green");
            } else if ("Brown".equals(theme)) {
                this.getContentPane().setBackground(new Color(0xDBDBB5));
                Themes.themeFontColors("Green");
            } else if ("Blue".equals(theme)) {
                this.getContentPane().setBackground(new Color(0x8DB1E6));
                Themes.themeFontColors("Blue");
            } else if ("Gray".equals(theme)) {
                this.getContentPane().setBackground(new Color(0x789FBB));
                Themes.themeFontColors("Gray");
            } else if ("Pink".equals(theme)) {
                this.getContentPane().setBackground(new Color(0xF9ABAB));
                Themes.themeFontColors("Pink");
            }
            this.getContentPane().setLayout(null);
        });

        //Table
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 73, 951, 245);
        this.getContentPane().add(scrollPane);
        String[] columns = new String[]{"Num", "Date", "Text"};
        model = new DefaultTableModel(new Object[0][], columns) {
            final boolean[] columnEditables = new boolean[]{false, false, true};
            public boolean isCellEditable(int row, int column) {
                return this.columnEditables[column];
            }
            // Сортировка
            final Class[] types_main = {Integer.class, String.class, String.class};
            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types_main[columnIndex];
            }
        };
        table = new JTable(model) {
            // tooltips
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException ignored) {
                }
                return tip;
            }
        };
        // cell border color
        table.setGridColor(new Color(58, 79, 79));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // table background color
        table.setFillsViewportHeight(true);
        table.setBackground(new Color(250, 252, 255));
        // headers settings
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 13));
        //sorter
        RowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        //cell alignment
        DefaultTableCellRenderer Renderer = new DefaultTableCellRenderer();
        Renderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(Renderer);
        table.getColumnModel().getColumn(1).setCellRenderer(Renderer);
        table.setRowHeight(20);
        table.setColumnSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMinWidth(15);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setMinWidth(125);
        table.getColumnModel().getColumn(2).setPreferredWidth(730);
        table.getColumnModel().getColumn(2).setMinWidth(400);
        //colors
        table.setSelectionBackground(new Color(0, 0, 0, 42));
        scrollPane.setViewportView(table);

        // Statement
        JStatement.setFont(fontStyle);
        JStatement.setForeground(fontColor);
        JStatement.setBounds(10, 7, 322, 20);
        this.getContentPane().add(JStatement);

        // Statement 2
        JStatement_2.setFont(fontStyle);
        JStatement_2.setForeground(fontColor);
        JStatement_2.setBounds(394, 7, 127, 20);
        getContentPane().add(JStatement_2);

        // Where clause
        textWhereClause.setBounds(323, 7, 67, 20);
        textWhereClause.setFont(new Font("Tahoma", Font.BOLD, 12));
        this.getContentPane().add(textWhereClause);
        textWhereClause.setColumns(10);

        // Like Clause
        likeTextField.setFont(new Font("Tahoma", Font.BOLD, 11));
        likeTextField.setBounds(503, 7, 82, 20);
        getContentPane().add(likeTextField);
        likeTextField.setColumns(10);

        // Order by label
        orderByLbl.setFont(fontStyle);
        orderByLbl.setForeground(fontColor);
        orderByLbl.setBounds(588, 7, 79, 20);
        getContentPane().add(orderByLbl);

        // Order by
        comboBoxOrderBy = new JComboBox<>();
        comboBoxOrderBy.setBounds(658, 7, 93, 20);
        comboBoxOrderBy.setBackground(new Color(220, 255, 252));
        comboBoxOrderBy.setFont(fontStyle);
        comboBoxOrderBy.setEditable(false);
        comboBoxOrderBy.setModel(new DefaultComboBoxModel<>(orderByValues));
        this.getContentPane().add(comboBoxOrderBy);

        // Sorting
        comboBoxSorting = new JComboBox<>();
        comboBoxSorting.setBounds(763, 7, 93, 20);
        comboBoxSorting.setBackground(new Color(220, 255, 252));
        comboBoxSorting.setFont(fontStyle);
        comboBoxSorting.setEditable(false);
        comboBoxSorting.setModel(new DefaultComboBoxModel<>(sortingValues));
        this.getContentPane().add(comboBoxSorting);

        // Platform
        devProd = new JComboBox<>();
        devProd.setBounds(10, 36, 67, 20);
        devProd.setFont(new Font("Tahoma", Font.BOLD, 12));
        devProd.setEditable(false);
        devProd.setModel(new DefaultComboBoxModel<>(devProdValues));
        devProd.setBackground(new Color(220, 255, 252));
        getContentPane().add(devProd);

        // Run button
        JButton selectBtn = new JButton("Select");
        selectBtn.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("res/icons/run.png"))));
        selectBtn.setFont(fontStyle);
        selectBtn.setBounds(868, 7, 93, 22);
        selectBtn.setBackground(new Color(199, 236, 255));
        this.getRootPane().setDefaultButton(selectBtn);
        // Select by Enter
        selectBtn.requestFocus();
        selectBtn.doClick();
        this.getContentPane().add(selectBtn);
        //Listener
        selectBtn.addActionListener((e) -> {
            Main.isStop.set(false);
            sumLbl.setText("--------  0");
            statusLbl.setText("");
            if (model.getColumnCount() > 0) model.setRowCount(0);
            if (!isSelect && !Main.isStop.get()) {
                (new Thread(Main::select)).start();
            }
            if (!isRun) {
                (new Thread(() -> {
                    while (isSelect && !Main.isStop.get()) {
                        Main.isStop.set(false);
                        Main.select();
                        if (isSelect && !Main.isStop.get()) {
                            statusLbl.setText("");
                        }
                    }

                })).start();
                isRun = true;
            }

        });

        // Stop button
        JButton stopBtn = new JButton("Stop");
        stopBtn.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("res/icons/stop.png"))));
        stopBtn.setBackground(new Color(255, 191, 183));
        stopBtn.setFont(fontStyle);
        stopBtn.setBounds(868, 36, 93, 22);
        getContentPane().add(stopBtn);
        //Listener
        stopBtn.addActionListener((e) -> {
            try {
                Main.isStop.set(true);
                Main.isSearchFinished.set(true);
                Common.notification("stopped");
                isRun = false;
            } catch (Exception var2) {
                Common.notification("no active threads to stop");
            }

        });

        // Clear button
        JButton clearBtn = new JButton("Clear");
        clearBtn.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("res/icons/clear.png"))));
        clearBtn.setFont(fontStyle);
        clearBtn.setBackground(new Color(209, 250, 206));
        clearBtn.setBounds(763, 36, 93, 22);
        getContentPane().add(clearBtn);
        //Listener
        clearBtn.addActionListener((e) -> {
            if (model.getRowCount() == 0) {
                Common.notification("no data to clear");
            } else {
                model.setRowCount(0);
                sumLbl.setText("--------  0");
                Common.notification("cleared");
            }
        });

        //Export to excel button
        JButton exportBtn = new JButton();
        exportBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
        exportBtn.setText("Export");
        exportBtn.setBackground(new Color(212, 212, 212));
        exportBtn.setBounds(658, 36, 93, 22);
        exportBtn.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("res/icons/excel.png"))));
        getContentPane().add(exportBtn);
        //Listener
        exportBtn.addActionListener((e) -> {
            if (model.getRowCount() != 0) {
                (new Thread(ExportLogIntoExcel::exportToExcel)).start();
                Common.notification("exporting to excel");
            } else {
                Common.notification("no data to export");
            }
        });

        // Connection button
        connectionBtn = new JButton("Connect");
        connectionBtn.setBackground(new Color(135, 206, 250));
        connectionBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
        connectionBtn.setBounds(165, 36, 85, 20);
        connectionBtn.setContentAreaFilled(true);
        connectionBtn.setBorderPainted(true);
        connectionBtn.setFocusable(false);
        getContentPane().add(connectionBtn);
        //Listener
        connectionBtn.addActionListener((e) -> {
            try {
                if (Main.isConnectedToVPN) {
                    Main.isConnectedToVPN = false;
                    if (Main.user_tables.size() > 0) Main.user_tables.clear();
                    if (tableNamesBox.getItemCount() > 0) tableNamesBox.removeAllItems();
                    Main.close();
                }
                new Thread(Main::open).start();
                favouriteTabCheckBox.setState(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //Status
        statusLbl2 = new JLabel("Status:");
        statusLbl2.setFont(fontStyle);
        statusLbl2.setForeground(fontColor);
        statusLbl2.setBounds(255, 36, 48, 20);
        getContentPane().add(statusLbl2);
        // Status label
        statusLbl = new JLabel();
        statusLbl.setBounds(303, 36, 282, 20);
        statusLbl.setFont(fontStyle);
        statusLbl.setForeground(fontColor);
        getContentPane().add(statusLbl);

        //Time label
        timeLbl = new JLabel();
        timeLbl.setFont(new Font("Tahoma", Font.BOLD, 11));
        timeLbl.setBounds(600, 37, 55, 20);
        getContentPane().add(timeLbl);

        // Auto update checkbox
        autoUpdateCheckBox = new Checkbox("", false);
        autoUpdateCheckBox.setBounds(10, 324, 13, 13);
        getContentPane().add(autoUpdateCheckBox);
        //Listener
        autoUpdateCheckBox.addItemListener((e) -> {
            isSelect = autoUpdateCheckBox.getState();
            isRun = false;
        });

        // Checkbox label
        chbxAutoUpdateLbl = new JLabel("auto update data");
        chbxAutoUpdateLbl.setFont(new Font("Tahoma", Font.BOLD, 11));
        chbxAutoUpdateLbl.setBounds(26, 323, 103, 14);
        chbxAutoUpdateLbl.setForeground(fontColor);
        getContentPane().add(chbxAutoUpdateLbl);

        // Amount label
        sumLbl = new JLabel("--------  0");
        sumLbl.setForeground(new Color(0, 0, 205));
        sumLbl.setFont(new Font("Tahoma", Font.BOLD, 11));
        sumLbl.setBounds(257, 55, 103, 14);
        getContentPane().add(sumLbl);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setToolTipText("Password");
        passwordField.setEchoChar('*');
        passwordField.setBounds(83, 36, 76, 20);
        getContentPane().add(passwordField);

        // Mouse right click menu
        final JPopupMenu popup = new JPopupMenu();
        // copy (menu)
        JMenuItem menuCopy = new JMenuItem("Copy");
        menuCopy.addActionListener((e) -> {
            StringBuilder sbf = new StringBuilder();
            int numcols = table.getSelectedColumnCount();
            int numrows = table.getSelectedRowCount();
            int[] rowsselected = table.getSelectedRows();
            int[] colsselected = table.getSelectedColumns();
            for (int i = 0; i < numrows; ++i) {
                for (int j = 0; j < numcols; ++j) {
                    sbf.append(table.getValueAt(rowsselected[i], colsselected[j]));
                    if (j < numcols - 1) {
                        sbf.append("\t");
                    }
                }
                sbf.append("\n");
            }
            StringSelection stsel = new StringSelection(sbf.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stsel, stsel);
        });
        popup.add(menuCopy);

        // Delete rows (menu)
        JMenuItem menuDeleteRow = new JMenuItem("Delete");
        menuDeleteRow.addActionListener((e) -> {
            int[] rows = table.getSelectedRows();
            for (int i = rows.length - 1; i >= 0; --i) {
                model.removeRow(rows[i]);
            }
        });
        popup.add(menuDeleteRow);

        // Mouse right click listener
        table.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JTable source = (JTable) e.getSource();
                    //int row = source.rowAtPoint(e.getPoint());
                    int row = source.convertRowIndexToModel(source.rowAtPoint(e.getPoint()));
                    int column = source.columnAtPoint(e.getPoint());
                    if (!source.isRowSelected(row)) {
                        source.changeSelection(row, column, false, false);
                    }
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        /* *** UNIQUE ANALYSIS *** */
        // label for analysis table
        analysisLabel = new JLabel("All tables. Favorite tables");
        analysisLabel.setBounds(15, 353, 155, 14);
        analysisLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        analysisLabel.setForeground(fontColor);
        getContentPane().add(analysisLabel);

        // Выбрать только избранные таблицы
        favouriteTabCheckBox = new Checkbox("", false);
        favouriteTabCheckBox.setBounds(172, 354, 13, 13);
        getContentPane().add(favouriteTabCheckBox);
        //Listener
        favouriteTabCheckBox.addItemListener((e) -> {
            isSelectFavouriteTab = favouriteTabCheckBox.getState();
            Common.addItemsToFavCombobox();
        });

        // Список всех таблиц в комбобоксе
        tableNamesBox = new JComboBox<>();
        //tableNamesBox.setFocusable(false);
        tableNamesBox.setBounds(15, 371, 210, 22);
        tableNamesBox.setBackground(new Color(238, 238, 238));
        // измененение значения в комбобоксе
        tableNamesBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                starBtn.setIcon(icon_star);
                Common.isTabInFavorites();
            }
        });
        this.getContentPane().add(tableNamesBox);

        // Добавление таблицы в избранные
        starBtn = new JButton();
        starBtn.setFocusable(false);
        starBtn.setContentAreaFilled(false);
        starBtn.setBorderPainted(false);
        starBtn.setToolTipText("Клик правой кнопкой мыши - удалить из избранного");
        starBtn.setIcon(icon_star);
        starBtn.setBounds(224, 371, 31, 22);
        getContentPane().add(starBtn);
        // обработка нажатия на звёздочку
        starBtn.addActionListener(e -> {
            if (tableNamesBox.getItemCount() > 0 && !isAddToFavorites) {
                int rowsCount = Common.countLines();
                String[] favTab = Common.getLinesFromFile(rowsCount);
                String tabName = (String) tableNamesBox.getSelectedItem();

                for (int i = 0; i < rowsCount; i++) {
                    if (favTab[i].equals(tabName)) {
                        starBtn.setIcon(icon_yes);
                        return;
                    }
                }
                isAddToFavorites = true;
                // запись избранной таблицы со средой в файл
                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(Main.favoritesTabPath, true), StandardCharsets.UTF_8)) {
                    // запись всей строки
                    String text = tableNamesBox.getSelectedItem() + "," + devProd.getSelectedItem();
                    writer.write(text);
                    // переход на следующую строку
                    writer.append("\n");
                    writer.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // измененение звёздочки на галочку
        starBtn.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (tableNamesBox.getItemCount() > 0) {
                    starBtn.setIcon(icon_yes);
                    isAddToFavorites = true;
                }
            }
            // удаление файла из избранного
            public void mouseClicked(MouseEvent e) {
                //if (e.getClickCount() == 2) { // двойной клик
                if (e.getButton() == MouseEvent.BUTTON3){ // клик правой кнопкой мыши
                    try {
                        Common.delTabFromFavorites();
                        Common.isTabInFavorites();
                    } catch (IOException ioException){
                        ioException.printStackTrace();
                    }
                    if (!favouriteTabCheckBox.getState()) {
                        starBtn.setIcon(icon_star);
                    }
                    isAddToFavorites = false;
                }
            }
            // наведение мышки на звёздочку
            public void mouseEntered(MouseEvent e){
                if(starBtn.getIcon() == icon_star) {
                    starBtn.setIcon(icon_star2);
                }
            }
            // убрали мышку со звёздочки
            public void mouseExited(MouseEvent e){
                if((starBtn.getIcon() == icon_star2)){
                    starBtn.setIcon(icon_star);
                }

            }
        });

        //Границы таблицы
        Box verticalBox = Box.createVerticalBox();
        verticalBox.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        verticalBox.setBounds(10, 350, 335, 202);
        getContentPane().add(verticalBox);

        // Кнопка анализа данных по выбранной таблице
        scan_btn = new JButton("Go");
        scan_btn.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("res/icons/scan.png"))));
        scan_btn.setFocusable(false);
        scan_btn.addActionListener(this);
        scan_btn.setBackground(new Color(209, 250, 206));
        scan_btn.setBounds(255, 371, 79, 23);
        scan_btn.setFocusable(false);
        getContentPane().add(scan_btn);

        // Шкала прогресса
        progressBar = new JProgressBar();
        progressBar.setFocusable(false);
        progressBar.setMaximum(100);
        progressBar.setBorderPainted(false);
        progressBar.setForeground(Color.RED);
        progressBar.setBackground(new Color(1,1,1));
        progressBar.setBounds(255, 359, 79, 4);
        getContentPane().add(progressBar);

        //Таблица анализа
        uniqueAnalysisScrollPane = new JScrollPane();
        uniqueAnalysisScrollPane.setBounds(15, 404, 324, 142);
        getContentPane().add(uniqueAnalysisScrollPane);

        Object[] uniqueColumns = {"Column", "Type", "Unique items"};
        uniqueAnalysisModel = new DefaultTableModel(new Object[][]{
        }, uniqueColumns) {
            final boolean[] uniqueColumnEditables = new boolean[]{
                    false, false, false
            };
            public boolean isCellEditable(int row, int column) {
                return uniqueColumnEditables[column];
            }
            // Сортировка
            final Class[] types_unique = {String.class, String.class, Integer.class};
            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types_unique[columnIndex];
            }
        };
        uniqueAnalysisTable = new JTable(uniqueAnalysisModel);
        uniqueAnalysisTable.setAutoCreateRowSorter(true);
        //headers
        JTableHeader uniqueHeader = uniqueAnalysisTable.getTableHeader();
        uniqueHeader.setFont(new Font("Tahoma", Font.BOLD, 13));
        //Cell alignment
        DefaultTableCellRenderer analysisRenderer = new DefaultTableCellRenderer();
        Renderer.setHorizontalAlignment(JLabel.CENTER);
        //uniqueAnalysisTable.getColumnmodel2().getColumn(0).setCellRenderer(analysisRenderer);
        uniqueAnalysisTable.getColumnModel().getColumn(1).setCellRenderer(analysisRenderer);
        uniqueAnalysisTable.setRowHeight(20);
        uniqueAnalysisTable.setColumnSelectionAllowed(true);
        uniqueAnalysisTable.setCellSelectionEnabled(true);
        uniqueAnalysisTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        uniqueAnalysisTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        uniqueAnalysisTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        uniqueAnalysisTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        uniqueAnalysisTable.getColumnModel().getColumn(2).setPreferredWidth(20);
        // Colors
        uniqueAnalysisTable.setForeground(Color.black);
        uniqueAnalysisTable.setSelectionForeground(new Color(26, 79, 164));
        uniqueAnalysisTable.setSelectionBackground(new Color(255, 255, 160));
        uniqueAnalysisScrollPane.setViewportView(uniqueAnalysisTable);

        //Кнопка для выбора цвета панели
        color_btn = new JButton();
        ImageIcon icon_color_btn = new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("res/icons/colors.png")));
        color_btn.setIcon(icon_color_btn);
        color_btn.setContentAreaFilled(true);
        color_btn.setBorderPainted(true);
        color_btn.setFocusable(false);
        color_btn.addActionListener(this);
        color_btn.setBackground(new Color(238, 238, 238));
        color_btn.setBounds(929, 324, 31, 23);
        color_btn.setFocusable(false);
        getContentPane().add(color_btn);

        /* Таблица с результатом селекта */
        // границы таблицы с селектом
        Box queryTableBox = Box.createVerticalBox();
        queryTableBox.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        queryTableBox.setBounds(355, 350, 606, 202);
        getContentPane().add(queryTableBox);

        //Таблица селекта
        executeScrollPane = new JScrollPane();
        executeScrollPane.setBounds(366, 404, 583, 142);
        getContentPane().add(executeScrollPane);

        // Select button
        JButton selectFromTableBtn = new JButton("Select");
        selectFromTableBtn.setFont(fontStyle);
        selectFromTableBtn.setBounds(868, 370, 79, 23);
        selectFromTableBtn.setBackground(new Color(255, 241, 133));
        getRootPane().setDefaultButton(selectFromTableBtn);
        selectFromTableBtn.requestFocus(false);
        getContentPane().add(selectFromTableBtn);

        // Текст селекта
        whereSelect = new JTextField();
        whereSelect.setHorizontalAlignment(SwingConstants.LEFT);
        whereSelect.setBounds(366, 371, 492, 23);
        // подсказка для поля where
        whereSelect.addFocusListener(new Common.JTextFieldHintListener(whereSelect, "where rownum < 18"));
        getContentPane().add(whereSelect);

        // условие where
        lblWhere = new JLabel("SELECT * FROM ");
        lblWhere.setForeground(Color.BLACK);
        lblWhere.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblWhere.setBounds(366, 354, 322, 14);
        getContentPane().add(lblWhere);

        // Результат селекта выбранной таблицы
        selectFromTableBtn.addActionListener(e -> {
            if (tableNamesBox.getItemCount() != 0) {
                Main.tableInfo();
                //Main.headers.add("ROWID");
                lblWhere.setText("SELECT * FROM " + tableNamesBox.getSelectedItem());
                executeModel = new DefaultTableModel(new Object[][]{
                }, Main.headers.toArray()) {
                    // Сортировка в любой таблице по любому типу столбца
                    final Class[] types = Common.typeClass(Main.types);

                    @Override
                    public Class getColumnClass(int columnIndex) {
                        return this.types[columnIndex];
                    }
                };
                //System.out.println(Arrays.toString(Common.typeClass(Main.types)));
                executeTable = new JTable(executeModel);
                executeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                executeTable.setAutoCreateRowSorter(true);
                //headers
                JTableHeader executeHeader = executeTable.getTableHeader();
                executeHeader.setFont(new Font("Tahoma", Font.BOLD, 13));
                //Cell alignment
                DefaultTableCellRenderer executeRenderer = new DefaultTableCellRenderer();
                executeRenderer.setHorizontalAlignment(JLabel.CENTER);
                executeTable.setRowHeight(20);
                executeTable.setColumnSelectionAllowed(true);
                executeTable.setCellSelectionEnabled(true);
                executeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                executeTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
                // ширина всех столбцов
                for (int i = 0; i < Main.headers.size(); i++) {
                    switch (Main.types.get(i)) {
                        case "NUMBER":
                        case "INTEGER":
                            executeTable.getColumnModel().getColumn(i).setPreferredWidth(60);
                            executeTable.getColumnModel().getColumn(i).setCellRenderer(executeRenderer);
                            break;
                        case "DATE":
                            executeTable.getColumnModel().getColumn(i).setPreferredWidth(125);
                            executeTable.getColumnModel().getColumn(i).setCellRenderer(executeRenderer);
                            break;
                        case "BLOB":
                        case "CLOB":
                            executeTable.getColumnModel().getColumn(i).setPreferredWidth(65);
                            executeTable.getColumnModel().getColumn(i).setCellRenderer(new ButtonRenderer());
                            executeTable.getColumnModel().getColumn(i).setCellEditor(new ButtonEditor(new JCheckBox()));
                            break;
                        default:
                            executeTable.getColumnModel().getColumn(i).setPreferredWidth(200);
                            break;
                    }
                }
                // Colors
                executeTable.setForeground(Color.black);
                executeTable.setSelectionForeground(new Color(26, 79, 164));
                executeTable.setSelectionBackground(new Color(255, 255, 160));
                executeScrollPane.setViewportView(executeTable);
                Main.isStop.set(false);
                if (executeModel.getColumnCount() > 0) executeModel.setRowCount(0);
                Main.isStop.set(false);
                statusLbl.setText("");
                if (!Main.isStop.get()) {
                    (new Thread(Main::selectFromTable)).start();
                }
            }
        });

        // Диалоговое окно со списком избранных таблиц
        showFavoriteTabBtn = new JButton();
        showFavoriteTabBtn.setToolTipText("Список избранных таблиц");
        showFavoriteTabBtn.setBounds(191, 354, 14, 14);
        showFavoriteTabBtn.setBackground(new Color(0, 0, 0));
        getContentPane().add(showFavoriteTabBtn);
        showFavoriteTabBtn.addActionListener((e) -> new Dialogs("favTabListDlg"));

        // Диалоговое окно лога
        logBtn = new JButton();
        logBtn.setToolTipText("Log");
        logBtn.setBackground(new Color(138, 44, 44));
        logBtn.setBounds(211, 354, 14, 14);
        getContentPane().add(logBtn);
        logBtn.addActionListener(e -> new Dialogs("logDlg"));

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Main.isConnectedToVPN && e.getSource() == scan_btn) {
            // анализ таблицы на уникальные значения по стобцам
            if (uniqueAnalysisModel.getColumnCount() > 0) uniqueAnalysisModel.setRowCount(0);
            new Thread(Main::tableInfo).start();
            new Thread(Main::selectUniqueItems).start();
            new Thread(Gui::fill).start();
            //Main.tableInfo();
            //Main.selectUniqueItems();
        } else if (e.getSource() == color_btn) {
            // выбор любого цвета
            Color color = JColorChooser.showDialog(null, "Color", Color.black);
            this.getContentPane().setBackground(color);
            Themes.themeFontColors("Default");
        }
    }

    // Шкала прогресса
    static void fill(){
        int counter = 0;
        while (!Main.isAnalyseFihished){
            if (counter == 99) {
                counter = 0;
            }
            progressBar.setValue(counter);
            try {
                Thread.sleep(10);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            counter++;
        }
        Main.isAnalyseFihished = false;
    }
}
