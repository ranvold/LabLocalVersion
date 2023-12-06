package org.dbms.database.ui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.dbms.database.DBManager;

public class MyTable extends JTable {

    public MyTable(DefaultTableModel tableModel) {
        super(tableModel);
        addPropertyChangeListener(evt -> {
            if ("tableCellEditor".equals(evt.getPropertyName())) {
                if (!isEditing()){
                    processEditingStopped();
                }
            }
        });
    }

    public void processEditingStopped() {
        int editingRow = getEditingRow();
        int editingColumn = getEditingColumn();

        if (editingRow != -1 && editingColumn != -1) {
            Object newValue = getValueAt(editingRow, editingColumn);
            int selectedTab = DBManagementSystem.getInstance().tabbedPane.getSelectedIndex();
            DBManager.getInstance().updateCellValue((String) newValue, selectedTab,editingColumn, editingRow, this);
        }
    }
}