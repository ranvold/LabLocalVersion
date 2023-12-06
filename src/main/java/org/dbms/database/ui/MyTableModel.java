package org.dbms.database.ui;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {

    public MyTableModel() {
        super();
    }

    public MyTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
    
    public void removeColumn(int column) {
        for (int i = 0; i < getRowCount(); i++) {
            Vector rowData = (Vector) dataVector.elementAt(i);
            rowData.removeElementAt(column);
        }
        
        columnIdentifiers.removeElementAt(column);
        
        fireTableStructureChanged();
    }
}