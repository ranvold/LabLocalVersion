package org.dbms.database.ui;

import java.awt.BorderLayout;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import org.dbms.database.DBManager;
import org.dbms.database.component.Row;
import org.dbms.database.component.Table;
import org.dbms.database.component.column.ColorInvlColumn;
import org.dbms.database.component.column.ColumnType;

public class DBManagementSystem {

    private static DBManagementSystem instance;
    private static String databaseName = "Open or create DB";
    JFrame frame;
    public JTabbedPane tabbedPane;
    public JMenuBar menuBar;

    public JMenuItem renameDBMenuItem;
    public JMenuItem deleteTableMenuItem;
    public JMenuItem addRowMenuItem;
    public JMenuItem addColumnMenuItem;
    public JMenuItem deleteRowMenuItem;
    public JMenuItem deleteColumnMenuItem;
    public JMenuItem createTableMenuItem;
    public JMenuItem renameTableMenuItem;
    public JMenuItem renameColumnMenuItem;
    public JMenuItem changeColumnTypeMenuItem;
    public JMenuItem loadDB;
    public JMenuItem tablesIntersection;
    public JMenuItem createDB;
    public JMenuItem deleteDB;
    public JMenuItem saveDB;

    public JMenu tableMenu = new JMenu("Table");
    public JMenu columnMenu = new JMenu("Column");
    public JMenu rowMenu = new JMenu("Row");

    public JLabel databaseLabel;


    public static DBManagementSystem getInstance(){
        if (instance == null){
            instance = new DBManagementSystem();

            instance.frame = new JFrame("DBMS");
            instance.menuBar = new JMenuBar();

            instance.tabbedPane = new JTabbedPane();

            instance.renameDBMenuItem = new JMenuItem("Rename");
            instance.createTableMenuItem = new JMenuItem("Create");
            instance.deleteTableMenuItem = new JMenuItem("Delete");
            instance.renameTableMenuItem = new JMenuItem("Rename");
            instance.addRowMenuItem = new JMenuItem("Add");
            instance.deleteRowMenuItem = new JMenuItem("Delete");
            instance.addColumnMenuItem = new JMenuItem("Add");
            instance.deleteColumnMenuItem = new JMenuItem("Delete");
            instance.renameColumnMenuItem = new JMenuItem("Rename");
            instance.changeColumnTypeMenuItem = new JMenuItem("Change type");
            instance.loadDB = new JMenuItem("Open");
            instance.createDB = new JMenuItem("Create");
            instance.deleteDB = new JMenuItem("Delete");
            instance.tablesIntersection = new JMenuItem("Intersection");
            instance.saveDB = new JMenuItem("Save");
        }
        return instance;
    }

    public static void main(String[] args) {
        DBManagementSystem instance = DBManagementSystem.getInstance();
        instance.frame.setSize(800, 600);
        instance.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        instance.setMenusVisibility(false);

        JMenu dbMenu = new JMenu("Database");
        dbMenu.add(instance.createDB);
        dbMenu.add(instance.deleteDB);
        dbMenu.add(instance.saveDB);
        dbMenu.add(instance.loadDB);
        dbMenu.add(instance.renameDBMenuItem);

        instance.tableMenu.add(instance.createTableMenuItem);
        instance.tableMenu.add(instance.deleteTableMenuItem);
        instance.tableMenu.add(instance.renameTableMenuItem);
        instance.tableMenu.add(instance.tablesIntersection);


        instance.columnMenu.add(instance.addColumnMenuItem);
        instance.columnMenu.add(instance.deleteColumnMenuItem);
        instance.columnMenu.add(instance.renameColumnMenuItem);
        instance.columnMenu.add(instance.changeColumnTypeMenuItem);

        instance.rowMenu.add(instance.addRowMenuItem);
        instance.rowMenu.add(instance.deleteRowMenuItem);

        instance.menuBar.add(dbMenu);
        instance.menuBar.add(instance.tableMenu);
        instance.menuBar.add(instance.columnMenu);
        instance.menuBar.add(instance.rowMenu);

        DBManagementSystem.instance.frame.setJMenuBar(DBManagementSystem.instance.menuBar);

        instance.frame.getContentPane().add(DBManagementSystem.getInstance().tabbedPane, BorderLayout.CENTER);

        instance.databaseLabel = new JLabel(databaseName, SwingConstants.CENTER);
        instance.frame.getContentPane().add(instance.databaseLabel, BorderLayout.NORTH);

        instance.frame.setLocationRelativeTo(null);

        instance.frame.setVisible(true);

        instance.createDB.addActionListener(e -> {
            if (!DBManager.getInstance().existDB()) {
                String name = JOptionPane.showInputDialog(instance.frame, "Enter DB name:");
                if (name != null && !name.isEmpty()) {
                    DBManager.getInstance().createDB(name);
                    instance.setMenusVisibility(true);
                }
            } else {
                int result = JOptionPane.showConfirmDialog(instance.frame, "DB already exists. Want to create new one?", "Confirm DB creation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    String name = JOptionPane.showInputDialog(instance.frame, "Enter DB name:");
                    if (name != null && !name.isEmpty()) {
                        DBManager.getInstance().deleteDB();
                        DBManager.getInstance().createDB(name);
                    }
                }
            }
        });

        instance.deleteDB.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(instance.frame, "You sure?", "Confirm DB deletion", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                DBManager.getInstance().deleteDB();
                instance.databaseLabel.setText("Open or create DB");
                instance.setMenusVisibility(false);

            }
        });

        instance.saveDB.addActionListener(e -> {
            String path = JOptionPane.showInputDialog(instance.frame, "Enter path:");
            System.out.println(path);
            DBManager.getInstance().saveDB(path);
        });


        instance.loadDB.addActionListener(e -> {
            String strPath = JOptionPane.showInputDialog(instance.frame, "Enter path:");
            if (strPath != null) {
                Path path = Paths.get(strPath);
                if (Files.exists(path)){
                    DBManager.getInstance().deleteDB();
                    DBManager.getInstance().openDB(strPath);
                    instance.setMenusVisibility(true);
                } else {
                    JOptionPane.showMessageDialog(DBManagementSystem.instance.frame, "File doesn't exist");
                }
            }
        });

        instance.renameDBMenuItem.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(instance.frame, "Enter DB new name:");
            DBManager.getInstance().renameDB(newName);
        });

        instance.createTableMenuItem.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog(instance.frame, "Enter new table name:");
            DBManager.getInstance().addTable(tableName);
        });

        instance.deleteTableMenuItem.addActionListener(e -> {
            int selectedIndex = instance.tabbedPane.getSelectedIndex();
            DBManager.getInstance().deleteTable(selectedIndex);
        });

        instance.renameTableMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                String newName = JOptionPane.showInputDialog(instance.frame, "Enter table new name:");
                DBManager.getInstance().renameTable(selectedTab, newName);
            }
        });

        instance.addColumnMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                String newColumnName = JOptionPane.showInputDialog(instance.frame, "Enter new column name:");

                if (newColumnName != null && !newColumnName.isEmpty()) {
                    ColumnType selectedDataType = (ColumnType) JOptionPane.showInputDialog(
                            instance.frame,
                            "Choose new column type:",
                            "Add column",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            ColumnType.values(),
                            ColumnType.INT
                    );

                    if (selectedDataType != null) {
                        if (selectedDataType == ColumnType.COLORINVL) {
                            DoubleInput dialog = new DoubleInput();
                            DoubleInput.InputResult doubleInputResult = dialog.showInputDialog();
                            if (doubleInputResult != null) {
                                String min = doubleInputResult.getMin();
                                String max = doubleInputResult.getMax();
                                if (ColorInvlColumn.validateMinMax(min,max) ){
                                    DBManager.getInstance().addColumn(selectedTab, newColumnName, selectedDataType,min,max);
                                }
                            }
                        } else {
                            DBManager.getInstance().addColumn(selectedTab, newColumnName, selectedDataType);
                        }

                    }
                    if (selectedDataType != null) {
                    }
                }
            }
        });

        instance.deleteColumnMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                MyTableModel tableModel = (MyTableModel) table.getModel();

                int selectedColumn = table.getSelectedColumn();
                DBManager.getInstance().deleteColumn(selectedTab, selectedColumn, tableModel);
            }
        });

        instance.renameColumnMenuItem.addActionListener(e -> {

            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                String newColumnName = JOptionPane.showInputDialog(instance.frame, "Enter column new name:");

                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();

                int selectedColumn = table.getSelectedColumn();
                DBManager.getInstance().renameColumn(selectedTab, selectedColumn, newColumnName, table);
            }

        });

        instance.changeColumnTypeMenuItem.addActionListener(e -> {

            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                ColumnType selectedDataType = (ColumnType) JOptionPane.showInputDialog(
                    instance.frame,
                    "Choose column new type:",
                    "Edit column",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ColumnType.values(),
                    ColumnType.INT
                );

                if (selectedDataType != null) {

                    JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                    JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                    JTable table = (JTable) scrollPane.getViewport().getView();
                    int selectedColumn = table.getSelectedColumn();


                    if (selectedDataType == ColumnType.COLORINVL) {
                        DoubleInput dialog = new DoubleInput();
                        DoubleInput.InputResult doubleInputResult = dialog.showInputDialog();
                        if (doubleInputResult != null) {
                            String min = doubleInputResult.getMin();
                            String max = doubleInputResult.getMax();
                            if (ColorInvlColumn.validateMinMax(min,max) ){
                                DBManager.getInstance().changeColumnType(selectedTab, selectedColumn, selectedDataType,min,max, table);
                            }
                        }
                    } else {
                        DBManager.getInstance().changeColumnType(selectedTab, selectedColumn, selectedDataType, table);
                    }
                }
            }
        });

        instance.addRowMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            DBManager.getInstance().addRow(selectedTab, new Row());
        });

        instance.deleteRowMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                MyTableModel tableModel = (MyTableModel) table.getModel();

                int selectedRow = table.getSelectedRow();
                DBManager.getInstance().deleteRow(selectedTab, selectedRow, tableModel);
            }
        });


        instance.tablesIntersection.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            List<Table> selectorTables = new ArrayList<>();
            List<Table> tables = DBManager.database.tables;

            for (int i = 0; i < tables.size(); i++){
                if (i != selectedTab) selectorTables.add(tables.get(i));
            }
            List<String> tableNames = new ArrayList<>();
            for (Table table : tables) {
                if (!table.name.equals(DBManager.database.tables.get(selectedTab).name)) tableNames.add(table.name);
            }
            String[] tableNamesArray = tableNames.toArray(new String[0]);
            String tableName = (String) JOptionPane.showInputDialog(
                instance.frame,
                "Choose second table for intersection:",
                "Tables intersection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tableNamesArray,
                tableNamesArray[0]
            );

            int intersectTable = -1;
            for (int i = 0; i < tables.size(); i++){
                if (DBManager.database.tables.get(i).name.equals(tableName)) intersectTable = i;
            }

            if (selectedTab != -1 && intersectTable != -1) {

                DBManager.getInstance().tablesIntersection(selectedTab, intersectTable);
                DBManagementSystem.getInstance().renderCells();
                JOptionPane.showMessageDialog(DBManagementSystem.instance.frame, "Intersection found!");
            }
        });
    }

    public JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();

        MyTable table = new MyTable(model);

        DefaultCellEditor cellEditor = new DefaultCellEditor(new JTextField());

        cellEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int selectedRow = table.getSelectedRow();
                int selectedColumn = table.getSelectedColumn();
                Object updatedValue = table.getValueAt(selectedRow, selectedColumn);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });

        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            table.getColumnModel().getColumn(columnIndex).setCellEditor(cellEditor);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        MyTableModel tableModel = new MyTableModel();

        table.setModel(tableModel);

        return panel;
    }

    private void setMenusVisibility(boolean visible) {
        renameDBMenuItem.setVisible(visible);
        instance.tableMenu.setVisible(visible);
        instance.columnMenu.setVisible(visible);
        instance.rowMenu.setVisible(visible);
        instance.deleteDB.setVisible(visible);
        instance.saveDB.setVisible(visible);
    }

    public void renderCells(){
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            JPanel tablePanel = (JPanel) tabbedPane.getComponentAt(i);
            JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
            MyTable table = (MyTable) scrollPane.getViewport().getView();
            for (int j = 0; j < DBManager.database.tables.get(i).rows.size(); j++) {
                for (int k = 0; k < DBManager.database.tables.get(i).columns.size(); k++) {
                    String data = DBManager.database.tables.get(i).rows.get(j).getAt(k);
                    table.setValueAt(data, j, k);
                }
            }
        }
    }
}