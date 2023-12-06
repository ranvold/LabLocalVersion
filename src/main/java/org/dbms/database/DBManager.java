package org.dbms.database;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.dbms.database.component.Column;
import org.dbms.database.component.Database;
import org.dbms.database.component.Row;
import org.dbms.database.component.Table;
import org.dbms.database.component.column.CharColumn;
import org.dbms.database.component.column.ColorColumn;
import org.dbms.database.component.column.ColorInvlColumn;
import org.dbms.database.component.column.ColumnType;
import org.dbms.database.component.column.IntegerColumn;
import org.dbms.database.component.column.RealColumn;
import org.dbms.database.component.column.StringColumn;
import org.dbms.database.file.DatabaseExporter;
import org.dbms.database.file.DatabaseImporter;
import org.dbms.database.ui.DBManagementSystem;
import org.dbms.database.ui.MyTable;
import org.dbms.database.ui.MyTableModel;

public class DBManager {
    private static DBManager instance;
    public static DBManagementSystem instanceCSW;

    private DBManager(){
    }

    public static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
            instanceCSW = DBManagementSystem.getInstance();
        }
        return instance;
    }

    public static Database database;

    public void openDB(String path){
        DatabaseImporter.importDatabase(path);
    }

    public void renameDB(String name){
        if (name != null && !name.isEmpty()) {
            database.setName(name);
            instanceCSW.databaseLabel.setText(database.name);
        }
    }

    public void saveDB(String path) {
        DatabaseExporter.exportDatabase(database, path);
    }

    public void deleteDB() {
        database = null;
        while (instanceCSW.tabbedPane.getTabCount() > 0) {
            instanceCSW.tabbedPane.removeTabAt(0);
        }
    }

    public void createDB(String name) {
        database = new Database(name);
        instanceCSW.databaseLabel.setText(database.name);
    }

    public boolean existDB(){
        return database != null;
    }

    public void addTable(String name){
        if (name != null && !name.isEmpty()) {
            JPanel tablePanel = instanceCSW.createTablePanel();

            DBManagementSystem.getInstance().tabbedPane.addTab(name, tablePanel);
            Table table = new Table(name);
            database.addTable(table);
        }
    }

    public void renameTable(int tableIndex, String name){
        if (name != null && !name.isEmpty() && tableIndex != -1) {
            instanceCSW.tabbedPane.setTitleAt(tableIndex,name);
            database.tables.get(tableIndex).setName(name);
        }
    }

    public void deleteTable(int tableIndex){

        if (tableIndex != -1) {
            instanceCSW.tabbedPane.removeTabAt(tableIndex);

            database.deleteTable(tableIndex);
        }
    }
    public void addColumn(int tableIndex, String columnName, ColumnType columnType){
        addColumn(tableIndex,columnName,columnType,"","");
    }

    public void addColumn(int tableIndex, String columnName, ColumnType columnType, String min, String max){
        if (columnName != null && !columnName.isEmpty()) {
            if (tableIndex != -1) {
                JPanel tablePanel = (JPanel) instanceCSW.tabbedPane.getComponentAt(tableIndex);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                MyTableModel tableModel = (MyTableModel) table.getModel();

                tableModel.addColumn(columnName + " (" + columnType.name() + ")");

                switch (columnType) {
                    case INT -> {
                        Column columnInt = new IntegerColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnInt);
                    }
                    case REAL -> {
                        Column columnReal = new RealColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnReal);
                    }
                    case STRING -> {
                        Column columnStr = new StringColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnStr);
                    }
                    case CHAR -> {
                        Column columnChar = new CharColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnChar);
                    }
                    case COLOR -> {
                        Column columnComplexInt = new ColorColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnComplexInt);
                    }
                    case COLORINVL -> {
                        Column columnComplexReal = new ColorInvlColumn(columnName,min,max);
                        database.tables.get(tableIndex).addColumn(columnComplexReal);
                    }
                }
                for (Row row: database.tables.get(tableIndex).rows) {
                    row.values.add("");
                }

            }
        }
    }

    public void renameColumn(int tableIndex, int columnIndex, String columnName, JTable table){
        if (columnName != null && !columnName.isEmpty()) {
            if (tableIndex != -1 && columnIndex != -1) {
                table.getColumnModel().getColumn(columnIndex).setHeaderValue(columnName + " (" + database.tables.get(tableIndex).columns.get(columnIndex).type + ")");
                table.getTableHeader().repaint();

                database.tables.get(tableIndex).columns.get(columnIndex).setName(columnName);
            }
        }
    }

    public void changeColumnType(int tableIndex, int columnIndex, ColumnType columnType, JTable table){
        changeColumnType(tableIndex,columnIndex,columnType,"","", table);
    }
    public void changeColumnType(int tableIndex, int columnIndex, ColumnType columnType, String min, String max, JTable table){
        if (tableIndex != -1 && columnIndex != -1) {
            table.getColumnModel().getColumn(columnIndex).setHeaderValue(database.tables.get(tableIndex).columns.get(columnIndex).name + " (" + columnType.name() + ")");
            table.getTableHeader().repaint();

            switch (columnType) {
                case INT -> {
                    Column columnInt = new IntegerColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnInt);
                }
                case REAL -> {
                    Column columnReal = new RealColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnReal);
                }
                case STRING -> {
                    Column columnStr = new StringColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnStr);
                }
                case CHAR -> {
                    Column columnChar = new CharColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnChar);
                }
                case COLOR -> {
                    Column columnComplexInt = new ColorColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnComplexInt);
                }
                case COLORINVL -> {
                    Column columnRealInt = new ColorInvlColumn(database.tables.get(tableIndex).columns.get(columnIndex).name, min, max);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnRealInt);
                }
            }
            for (Row row: database.tables.get(tableIndex).rows) {
                row.values.set(columnIndex,"");
            }
            for (int i = 0; i < database.tables.get(tableIndex).rows.size(); i++) {
                table.setValueAt("", i, columnIndex);
            }
        }
    }

    public void deleteColumn(int tableIndex, int columnIndex, MyTableModel tableModel){

        if (columnIndex != -1) {
            tableModel.removeColumn(columnIndex);
            database.tables.get(tableIndex).deleteColumn(columnIndex);
        }
    }

    public void addRow(int tableIndex, Row row){

        if (tableIndex != -1) {
            JPanel tablePanel = (JPanel) instanceCSW.tabbedPane.getComponentAt(tableIndex);
            JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
            JTable table = (JTable) scrollPane.getViewport().getView();
            MyTableModel tableModel = (MyTableModel) table.getModel();
            tableModel.addRow(new Object[tableModel.getColumnCount()]);

            database.tables.get(tableIndex).addRow(row);
            for (int i = row.values.size(); i < database.tables.get(tableIndex).columns.size(); i++){
                row.values.add("");
            }
        }
    }

    public void deleteRow(int tableIndex, int rowIndex, MyTableModel tableModel){

        if (rowIndex != -1) {
            tableModel.removeRow(rowIndex);

            database.tables.get(tableIndex).deleteRow(rowIndex);
        }
    }

    public void updateCellValue(String value, int tableIndex, int columnIndex, int rowIndex, MyTable table){
        if (database.tables.get(tableIndex).columns.get(columnIndex).validate(value)){
            database.tables.get(tableIndex).rows.get(rowIndex).setAt(columnIndex,value.trim());
        }
        else {
            String data = database.tables.get(tableIndex).rows.get(rowIndex).getAt(columnIndex);
            if (data != null){
                table.setValueAt(data, rowIndex, columnIndex);
            }
            else {
                table.setValueAt("", rowIndex, columnIndex);
            }

            JFrame frame = new JFrame("Error!!!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid value",
                    "Error!!!",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void tablesIntersection(int tableIndex1, int tableIndex2){
        Table tempTable1 = new Table(database.tables.get(tableIndex1));
        Table tempTable2 = new Table(database.tables.get(tableIndex2));
        int i = 0;
        while (i<tempTable1.columns.size()){
            boolean flag = false;
            int j = i;
            while (j<tempTable2.columns.size()){
                if (tempTable1.columns.get(i).name.equals(tempTable2.columns.get(j).name) &&
                    tempTable1.columns.get(i).type.equals(tempTable2.columns.get(j).type)){
                    flag = true;
                    Column temp = tempTable2.columns.get(i);
                    tempTable2.columns.set(i,tempTable2.columns.get(j));
                    tempTable2.columns.set(j,temp);
                    for (Row row: tempTable2.rows) {
                        String data = row.getAt(i);
                        row.setAt(i,row.getAt(j));
                        row.setAt(j,data);
                    }
                    i++;
                    break;
                }
                else j++;
            }
            if (!flag){
                tempTable1.deleteColumn(i);
            }
        }
        i = 0;
        while (i<tempTable1.rows.size()){
            boolean flag = true;
            int j = 0;
            if (tempTable2.rows.size() == 0){
                flag = false;
            }
            while (j<tempTable2.rows.size()){
                for (int k = 0; k < tempTable1.rows.get(i).values.size(); k++) {
                    if (!tempTable1.rows.get(i).getAt(k).equals(tempTable2.rows.get(j).getAt(k))){
                        flag = false;
                        break;
                    }
                }
                if (flag){
                    i++;
                    tempTable2.rows.remove(j);
                    break;
                }
                else j++;
            }
            if (!flag) tempTable1.rows.remove(i);
        }
        addTable(tempTable1.name + " ^ " + tempTable2.name);
        for (Column column: tempTable1.columns) {
            addColumn(database.tables.size()-1,column.name, ColumnType.valueOf(column.type));
        }
        for (Row row: tempTable1.rows) {
            addRow(database.tables.size()-1,row);
            System.out.println(row.values);
        }
    }

}
