/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author DELL
 */
public class InvLineSubFrame extends JFrame{
    private JTextField itemNameF;
    private JTextField itemCountF;
    private JTextField itemPriceF;
    private JLabel itemNameLb;
    private JLabel itemCountLb;
    private JLabel itemPriceLb;
    private JButton okBtn;
    private JButton cancelBtn;
    
    public InvLineSubFrame(InvoiceFrame frame) {
        itemNameF = new JTextField(20);
        itemNameLb = new JLabel("Item Name");
        
        itemCountF = new JTextField(20);
        itemCountLb = new JLabel("Item Count");
        
        itemPriceF = new JTextField(20);
        itemPriceLb = new JLabel("Item Price");
        
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        
        okBtn.setActionCommand("createLineOK");
        cancelBtn.setActionCommand("createLineCancel");
        
        okBtn.addActionListener(frame.getListener());
        cancelBtn.addActionListener(frame.getListener());
        setLayout(new GridLayout(4, 2));
        
        add(itemNameLb);
        add(itemNameF);
        add(itemCountLb);
        add(itemCountF);
        add(itemPriceLb);
        add(itemPriceF);
        add(okBtn);
        add(cancelBtn);
        
        pack();
    }

    public JTextField getItemNameField() {
        return itemNameF;
    }

    public JTextField getItemCountField() {
        return itemCountF;
    }

    public JTextField getItemPriceField() {
        return itemPriceF;
    }
}
