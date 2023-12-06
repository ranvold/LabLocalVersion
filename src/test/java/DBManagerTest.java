import static org.junit.jupiter.api.Assertions.*;

import org.dbms.database.DBManager;
import org.dbms.database.component.Row;
import org.dbms.database.component.Table;
import org.dbms.database.component.column.ColumnType;
import org.dbms.database.ui.MyTable;
import org.dbms.database.ui.MyTableModel;
import org.dbms.database.ui.DBManagementSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class DBManagerTest {

    private DBManager dbManager;

    @BeforeEach
    public void setUp() {
        dbManager = DBManager.getInstance();
        DBManagementSystem.getInstance();
        DBManagementSystem.getInstance().databaseLabel = new JLabel("databaseName", SwingConstants.CENTER);
        // Initialize your database or perform other setup here if needed
    }

    @Test
    public void testCreateDB() {
        dbManager.createDB("TestDB");
        assertTrue(dbManager.existDB());
        assertEquals("TestDB", dbManager.database.name);
    }

    @Test
    public void testRenameDB() {
        dbManager.createDB("TestDB");
        dbManager.renameDB("NewName");
        assertEquals("NewName", dbManager.database.name);
    }

    @Test
    public void testDeleteDB() {
        dbManager.createDB("TestDB");
        dbManager.deleteDB();
        assertNull(dbManager.database);
        assertEquals(0, dbManager.instanceCSW.tabbedPane.getTabCount());
    }

    @Test
    public void testAddTable() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        assertEquals(1, dbManager.database.tables.size());
        assertEquals("Table1", dbManager.database.tables.get(0).name);
    }

    @Test
    public void testRenameTable() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.renameTable(0, "NewTable1");
        assertEquals("NewTable1", dbManager.database.tables.get(0).name);
    }

    @Test
    public void testDeleteTable() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.addTable("Table2");
        dbManager.deleteTable(0);
        assertEquals(1, dbManager.database.tables.size());
        assertEquals("Table2", dbManager.database.tables.get(0).name);
    }

    @Test
    public void testAddColumn() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.addColumn(0, "Column1", ColumnType.INT);
        assertEquals(1, dbManager.database.tables.get(0).columns.size());
        assertEquals("Column1", dbManager.database.tables.get(0).columns.get(0).name);
    }

    @Test
    public void testRenameColumn() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.addColumn(0, "Column1", ColumnType.INT);

        JPanel tablePanel = (JPanel) DBManagementSystem.getInstance().tabbedPane.getComponentAt(0);
        JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
        JTable table = (JTable) scrollPane.getViewport().getView();
        dbManager.renameColumn(0, 0, "NewColumn1", table); // Pass null for JTable in this test
        assertEquals("NewColumn1", dbManager.database.tables.get(0).columns.get(0).name);
    }

    @Test
    public void testChangeColumnType() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.addColumn(0, "Column1", ColumnType.INT);

        JPanel tablePanel = (JPanel) DBManagementSystem.getInstance().tabbedPane.getComponentAt(0);
        JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
        JTable table = (JTable) scrollPane.getViewport().getView();
        dbManager.changeColumnType(0, 0, ColumnType.REAL,
            table); // Pass null for JTable in this test
        assertEquals(ColumnType.REAL.name(), dbManager.database.tables.get(0).columns.get(0).type);
    }

    @Test
    public void testDeleteColumn() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.addColumn(0, "Column1", ColumnType.INT);
        dbManager.addColumn(0, "Column2", ColumnType.STRING);

        JPanel tablePanel = (JPanel) DBManagementSystem.getInstance().tabbedPane.getComponentAt(0);
        JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
        JTable table = (JTable) scrollPane.getViewport().getView();
        MyTableModel tableModel = (MyTableModel) table.getModel();
        dbManager.deleteColumn(0, 0, tableModel); // Pass null for CustomTableModel in this test
        assertEquals(1, dbManager.database.tables.get(0).columns.size());
        assertEquals("Column2", dbManager.database.tables.get(0).columns.get(0).name);
    }

    @Test
    public void testAddRow() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        Row row = new Row();
        row.values.add("Value1");
        dbManager.addRow(0, row);
        assertEquals(1, dbManager.database.tables.get(0).rows.size());
        assertEquals("Value1", dbManager.database.tables.get(0).rows.get(0).values.get(0));
    }

    @Test
    public void testDeleteRow() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.addTable("Table2");
        Row row1 = new Row();
        row1.values.add("Value1");
        Row row2 = new Row();
        row2.values.add("Value2");
        dbManager.addRow(0, row1);
        dbManager.addRow(1, row2);

        JPanel tablePanel = (JPanel) DBManagementSystem.getInstance().tabbedPane.getComponentAt(0);
        JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
        JTable table = (JTable) scrollPane.getViewport().getView();
        MyTableModel tableModel = (MyTableModel) table.getModel();
        dbManager.deleteRow(0, 0, tableModel); // Pass null for CustomTableModel in this test
        assertEquals(0, dbManager.database.tables.get(0).rows.size());
        assertEquals(1, dbManager.database.tables.get(1).rows.size());
        assertEquals("Value2", dbManager.database.tables.get(1).rows.get(0).values.get(0));
    }

    @Test
    public void testUpdateCellValue() {
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.addColumn(0, "Column1", ColumnType.INT);
        Row row = new Row();
        row.values.add("");
        dbManager.addRow(0, row);

        JPanel tablePanel = (JPanel) DBManagementSystem.getInstance().tabbedPane.getComponentAt(0);
        JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
        MyTable table = (MyTable) scrollPane.getViewport().getView();
        dbManager.updateCellValue("123", 0, 0, 0, table); // Pass null for CustomTable in this test
        assertEquals("123", dbManager.database.tables.get(0).rows.get(0).values.get(0));
    }

    @Test
    public void testTablesIntersection() {
        // Create a test database with two tables
        dbManager.createDB("TestDB");
        dbManager.addTable("Table1");
        dbManager.addTable("Table2");

        // Add columns to Table1
        dbManager.addColumn(0, "Column1", ColumnType.INT);
        dbManager.addColumn(0, "Column2", ColumnType.STRING);
        dbManager.addColumn(0, "Column3", ColumnType.REAL);

        // Add columns to Table2
        dbManager.addColumn(1, "Column2", ColumnType.STRING);
        dbManager.addColumn(1, "Column3", ColumnType.REAL);
        dbManager.addColumn(1, "Column4", ColumnType.CHAR);

        // Add rows to Table1
        Row row1 = new Row();
        row1.values.add("1");
        row1.values.add("Hello");
        row1.values.add("2.5");
        dbManager.addRow(0, row1);

        Row row2 = new Row();
        row2.values.add("2");
        row2.values.add("World");
        row2.values.add("3.7");
        dbManager.addRow(0, row2);

        // Add rows to Table2
        Row row3 = new Row();
        row3.values.add("Hello");
        row3.values.add("2.5");
        row3.values.add("A");
        dbManager.addRow(1, row3);

        Row row4 = new Row();
        row4.values.add("World");
        row4.values.add("3.7");
        row4.values.add("B");
        dbManager.addRow(1, row4);

        // Perform the intersection operation on Table1 and Table2
        int tableIndex1 = 0;
        int tableIndex2 = 1;
        dbManager.tablesIntersection(tableIndex1, tableIndex2);

        // Assert that the result table has the expected structure and data
        assertEquals(3, dbManager.database.tables.size());

        Table resultTable = dbManager.database.tables.get(2);
        assertEquals("Table1 ^ Table2", resultTable.name);

        // Assert the columns in the result table
        assertEquals(2, resultTable.columns.size());
        assertEquals("Column2 (STRING)", resultTable.columns.get(0).name + " (" + resultTable.columns.get(0).type + ")" );
        assertEquals("Column3 (REAL)", resultTable.columns.get(1).name + " (" + resultTable.columns.get(1).type + ")");

        // Assert the rows in the result table
        assertEquals(2, resultTable.rows.size());
        assertEquals("Hello", resultTable.rows.get(0).values.get(0));
        assertEquals("2.5", resultTable.rows.get(0).values.get(1));
        assertEquals("World", resultTable.rows.get(1).values.get(0));
        assertEquals("3.7", resultTable.rows.get(1).values.get(1));
    }
}