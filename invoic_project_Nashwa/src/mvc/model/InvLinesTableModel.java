/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DELL
 */
public class InvLinesTableModel extends AbstractTableModel {

    private List<InvoiceLines> invLinesItems;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    public InvLinesTableModel(List<InvoiceLines> invoiceLines) {
        this.invLinesItems = invoiceLines;
    }

    public List<InvoiceLines> getinvLinesItems() {
        return invLinesItems;
    }
    
    
    @Override
    public int getRowCount() {
        return invLinesItems.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int colmIndex) {
        switch (colmIndex) {
            case 0:
                return "Item Name";
            case 1:
                return "Item Price";
            case 2:
                return "Count";
            case 3:
                return "Line Total";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Double.class;
            case 2:
                return Integer.class;
            case 3:
                return Double.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLines row = invLinesItems.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return row.getItemName();
            case 1:
                return row.getItemPrice();
            case 2:
                return row.getItemCount();
            case 3:
                return row.getLineTotal();
            default:
                return "";
        }
        
    }
    
}
