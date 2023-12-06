package org.dbms.database.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.dbms.database.DBManager;
import org.dbms.database.component.Row;
import org.dbms.database.component.column.ColumnType;
import org.dbms.database.ui.DBManagementSystem;

public class DatabaseImporter {

    public static void importDatabase(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String dbName = reader.readLine();
            DBManager.getInstance().createDB(dbName);

            int numTables = Integer.parseInt(reader.readLine());

            for (int i = 0; i < numTables; i++) {
                String tableName = reader.readLine();
                DBManager.getInstance().addTable(tableName);
                importTable(reader, i);
            }
            DBManagementSystem.getInstance().renderCells();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void importTable(BufferedReader reader, int tableIndex) throws IOException {

        int numColumns = Integer.parseInt(reader.readLine());
        int numRows = Integer.parseInt(reader.readLine());

        for (int i = 0; i < numColumns; i++) {
            String columnName = reader.readLine();
            String columnType = reader.readLine();
            if (columnType.equals(ColumnType.COLORINVL.name())){
                ColumnType columnTypeEnum = ColumnType.valueOf(columnType);
                String min = reader.readLine();
                String max = reader.readLine();
                DBManager.getInstance().addColumn(tableIndex, columnName, columnTypeEnum,min,max);
            }
            else {
                ColumnType columnTypeEnum = ColumnType.valueOf(columnType);
                DBManager.getInstance().addColumn(tableIndex, columnName, columnTypeEnum);
            }
        }

        for (int i = 0; i < numRows; i++) {
            Row row = new Row();
            for (int j = 0; j < numColumns; j++){
                String data = reader.readLine();
                row.values.add(data);
            }
            DBManager.getInstance().addRow(tableIndex,row);
        }
    }
}