/*
 * To change this license headerObj, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controller;

import mvc.model.InvoiceHeader;
import mvc.model.InvHeaderTableModel;
import mvc.model.InvoiceLines;
import mvc.model.InvLinesTableModel;
import mvc.view.InvoiceFrame;
import mvc.view.InvHeaderSubFrame;
import mvc.view.InvLineSubFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author DELL
 */
public class InvActionListener implements ActionListener, ListSelectionListener  {
    private InvoiceFrame invFormObj;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    public InvActionListener(InvoiceFrame invFormObj) {
        this.invFormObj = invFormObj;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {

      if (e.getActionCommand().equals("CreateNewInvoice")) {
                invoiceSubDialog();}
      else if (e.getActionCommand().equals("DeleteInvoice")){deleteCusInv();}
       else if (e.getActionCommand().equals("CreateNewLine")){LineSubDialog();}
        else if (e.getActionCommand().equals("DeleteLine")){deleteLineItem();} 
      else if (e.getActionCommand().equals("LoadFile")){loadFile();}  
       else if (e.getActionCommand().equals("SaveFile")){saveFile();}  
         else if (e.getActionCommand().equals("createInvCancel")){cancelcusInvoice();}  
      else if (e.getActionCommand().equals("createInvOK")){ okCusInvoice();} 
       else if (e.getActionCommand().equals("createLineCancel")){ LineCancelMth();} 
      else if (e.getActionCommand().equals("createLineOK")){ LineOKMth();} 
      else {System.out.println("thanks");}       
        
      
      
    }

    private void loadFile() {
        JOptionPane.showMessageDialog(invFormObj, "First:Please select header file", "Note", JOptionPane.INFORMATION_MESSAGE);
        JFileChooser selectFile = new JFileChooser();
        int filesWindow = selectFile.showOpenDialog(invFormObj);
        if (filesWindow == JFileChooser.APPROVE_OPTION) {
            File invHeadFile = selectFile.getSelectedFile();
            try {
                FileReader invHeadFreader = new FileReader(invHeadFile);
                BufferedReader invHeadBreader = new BufferedReader(invHeadFreader);
                String headCustomerInfo = null;

                while ((headCustomerInfo = invHeadBreader.readLine()) != null) {
                    String[] invHeadColumns = headCustomerInfo.split(",");
                    String invNumS = invHeadColumns[0];
                    String invDateS = invHeadColumns[1];
                    String customerName = invHeadColumns[2];

                    int invNumInt = Integer.parseInt(invNumS);
                    Date invDate = df.parse(invDateS);

                    InvoiceHeader invHeaderArr = new InvoiceHeader(invNumInt, customerName, invDate);
                    invFormObj.getInvoicesItems().add(invHeaderArr);
                }

                JOptionPane.showMessageDialog(invFormObj, "Second:Please select lines file", "Note", JOptionPane.INFORMATION_MESSAGE);
                filesWindow = selectFile.showOpenDialog(invFormObj);
                if (filesWindow == JFileChooser.APPROVE_OPTION) {
                    File invLinesFile = selectFile.getSelectedFile();
                    BufferedReader invLinesBreader = new BufferedReader(new FileReader(invLinesFile));
                    String invLinesItems = null;
                    while ((invLinesItems = invLinesBreader.readLine()) != null) {
                        String[] invLineColumns = invLinesItems.split(",");
                        String linNumS = invLineColumns[0];
                        String itemName = invLineColumns[1];
                        String itemPriceS = invLineColumns[2];
                        String itemCountS = invLineColumns[3];

                        int invLinNum = Integer.parseInt(linNumS);
                        double itemPrice = Double.parseDouble(itemPriceS);
                        int itemCount = Integer.parseInt(itemCountS);
                        InvoiceHeader headerObj = matchedNum(invLinNum);
                        InvoiceLines invLine = new InvoiceLines(itemName, itemPrice, itemCount, headerObj);
                        headerObj.getLines().add(invLine);
                    }
                    invFormObj.setInvHeaderTableModelObj(new InvHeaderTableModel(invFormObj.getInvoicesItems()));
                    invFormObj.getInvoicesTable().setModel(invFormObj.getinvHeaderTableModelObj());
                    invFormObj.getInvoicesTable().validate();
                }
                
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(invFormObj, "Date Format Error" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(invFormObj, "Number Format Error" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(invFormObj, "File Error" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(invFormObj, "Read Error" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
      
    }

   private void saveFile() {
        String savHeaderCust = "";
        String linesItems = "";
        for (InvoiceHeader savHeaderArr : invFormObj.getInvoicesItems()) {
            savHeaderCust += savHeaderArr.getDataAsCSV();
            savHeaderCust += "\n";
            for (InvoiceLines savLine : savHeaderArr.getLines()) {
               linesItems += savLine.getDataAsCSV();
                   linesItems += "\n";
            }
        }
        JOptionPane.showMessageDialog(invFormObj, "First: Please select file to save customer header info", "To Save", JOptionPane.INFORMATION_MESSAGE);
        JFileChooser fileChooser = new JFileChooser();
        int saveFile = fileChooser.showSaveDialog(invFormObj);
        if (saveFile == JFileChooser.APPROVE_OPTION) {
            File saveheadFile = fileChooser.getSelectedFile();
            try {
                FileWriter savHeadwriter = new FileWriter(saveheadFile);
                savHeadwriter.write(savHeaderCust);
                savHeadwriter.flush();
                savHeadwriter.close();

                JOptionPane.showMessageDialog(invFormObj, "Second: Please select file to save invoice items ", "To Save", JOptionPane.INFORMATION_MESSAGE);
                saveFile = fileChooser.showSaveDialog(invFormObj);
                if (saveFile == JFileChooser.APPROVE_OPTION) {
                    File savLinesFile = fileChooser.getSelectedFile();
                    FileWriter lFW = new FileWriter(savLinesFile);
                    lFW.write(linesItems);
                    lFW.flush();
                    lFW.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(invFormObj, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(invFormObj, "Data saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

    }

      
    private InvoiceHeader matchedNum(int invNum) {
        InvoiceHeader matchedHeader = null;
        for (InvoiceHeader invN : invFormObj.getInvoicesItems()) {
            if (invNum == invN.getInvNum()) {
                matchedHeader = invN;
                break;
            }
        }
        return matchedHeader;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Invoice Selected!");
        invoicesTableRowSelected();
    }

    private void invoicesTableRowSelected() {
        int selectedRowIndex = invFormObj.getInvoicesTable().getSelectedRow();
        if (selectedRowIndex >= 0) {
            InvoiceHeader row = invFormObj.getinvHeaderTableModelObj().getCusinvList().get(selectedRowIndex);
            invFormObj.getCustNameTF().setText(row.getCustomerName());
            invFormObj.getInvDateTF().setText(df.format(row.getInvDate()));
            invFormObj.getInvNumLbl().setText("" + row.getInvNum());
            invFormObj.getInvTotalLbl().setText("" + row.getInvTotal());
            ArrayList<InvoiceLines> lines = row.getLines();
            invFormObj.setInvLinesTableModelObj(new InvLinesTableModel(lines));
            invFormObj.getInvLinesTable().setModel(invFormObj.getInvoiceLinesTableModelObj());
            invFormObj.getInvoiceLinesTableModelObj().fireTableDataChanged();
        }
    }

    private void invoiceSubDialog() {
        invFormObj.setHeaderDialog(new InvHeaderSubFrame(invFormObj));
        invFormObj.getHeaderDialog().setVisible(true);
    }

    private void LineSubDialog() {
        invFormObj.setLineDialog(new InvLineSubFrame(invFormObj));
        invFormObj.getLineDialog().setVisible(true);
    }
    
      private void okCusInvoice() {
        String custName = invFormObj.getHeaderDialog().getCustNamF().getText();
        String invDateStr = invFormObj.getHeaderDialog().getInvDatF().getText();
        invFormObj.getHeaderDialog().setVisible(false);
        invFormObj.getHeaderDialog().dispose();
        invFormObj.setHeaderDialog(null);
        try {
            Date invDate = df.parse(invDateStr);
            int invNum = getNextInvN();
            InvoiceHeader invoiceHeader = new InvoiceHeader(invNum, custName, invDate);
            invFormObj.getInvoicesItems().add(invoiceHeader);
            invFormObj.getinvHeaderTableModelObj().fireTableDataChanged();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(invFormObj, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
      
    }
    

    private void cancelcusInvoice() {
        invFormObj.getHeaderDialog().setVisible(false);
        invFormObj.getHeaderDialog().dispose();
        invFormObj.setHeaderDialog(null);
    }

  

    private int getNextInvN() {
        int max = 0;
        for (InvoiceHeader header : invFormObj.getInvoicesItems()) {
            if (header.getInvNum() > max) {
                max = header.getInvNum();
            }
        }
        return max + 1;
    }

    private void LineCancelMth() {
        invFormObj.getLineDialog().setVisible(false);
        invFormObj.getLineDialog().dispose();
        invFormObj.setLineDialog(null);
    }

    private void LineOKMth() {
        String itemName = invFormObj.getLineDialog().getItemNameField().getText();
        String itemCountStr = invFormObj.getLineDialog().getItemCountField().getText();
        String itemPriceStr = invFormObj.getLineDialog().getItemPriceField().getText();
        invFormObj.getLineDialog().setVisible(false);
        invFormObj.getLineDialog().dispose();
        invFormObj.setLineDialog(null);
        int itemCount = Integer.parseInt(itemCountStr);
        double itemPrice = Double.parseDouble(itemPriceStr);
        int headIndx = invFormObj.getInvoicesTable().getSelectedRow();
        InvoiceHeader invOk = invFormObj.getinvHeaderTableModelObj().getCusinvList().get(headIndx);

        InvoiceLines invLineOk = new InvoiceLines(itemName, itemPrice, itemCount, invOk);
        invOk.addInvLine(invLineOk);
        invFormObj.getInvoiceLinesTableModelObj().fireTableDataChanged();
        invFormObj.getinvHeaderTableModelObj().fireTableDataChanged();
        invFormObj.getInvTotalLbl().setText("" + invOk.getInvTotal());
      
    }

    private void deleteCusInv() {
        int invIndex = invFormObj.getInvoicesTable().getSelectedRow();
        InvoiceHeader headerDel = invFormObj.getinvHeaderTableModelObj().getCusinvList().get(invIndex);
        invFormObj.getinvHeaderTableModelObj().getCusinvList().remove(invIndex);
        invFormObj.getinvHeaderTableModelObj().fireTableDataChanged();
        invFormObj.setInvLinesTableModelObj(new InvLinesTableModel(new ArrayList<InvoiceLines>()));
        invFormObj.getInvLinesTable().setModel(invFormObj.getInvoiceLinesTableModelObj());
        invFormObj.getInvoiceLinesTableModelObj().fireTableDataChanged();
        invFormObj.getCustNameTF().setText("");
        invFormObj.getInvDateTF().setText("");
        invFormObj.getInvNumLbl().setText("");
        invFormObj.getInvTotalLbl().setText("");
    
    }

    private void deleteLineItem() {
        int lineItemsIndx = invFormObj.getInvLinesTable().getSelectedRow();
        InvoiceLines lineItems = invFormObj.getInvoiceLinesTableModelObj().getinvLinesItems().get(lineItemsIndx);
        invFormObj.getInvoiceLinesTableModelObj().getinvLinesItems().remove(lineItemsIndx);
        invFormObj.getInvoiceLinesTableModelObj().fireTableDataChanged();
        invFormObj.getinvHeaderTableModelObj().fireTableDataChanged();
        invFormObj.getInvTotalLbl().setText("" + lineItems.getHeader().getInvTotal());
       
    }

   
    
}
