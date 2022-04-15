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
public class InvHeaderSubFrame extends JFrame {
    private JTextField custNamF;
    private JTextField invDatF;
    private JLabel custNamLb;
    private JLabel invDatLb;
    private JButton okBtn;
    private JButton cancelBtn;

    public InvHeaderSubFrame(InvoiceFrame frame) {
        custNamLb = new JLabel("Customer Name:");
        custNamF = new JTextField(20);
        invDatLb = new JLabel("Invoice Date:");
        invDatF = new JTextField(20);
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        
        okBtn.setActionCommand("createInvOK");
        cancelBtn.setActionCommand("createInvCancel");
        
        okBtn.addActionListener(frame.getListener());
        cancelBtn.addActionListener(frame.getListener());
        setLayout(new GridLayout(3, 2));
        
        add(invDatLb);
        add(invDatF);
        add(custNamLb);
        add(custNamF);
        add(okBtn);
        add(cancelBtn);
        
        pack();
        
    }

    public JTextField getCustNamF() {
        return custNamF;
    }

    public JTextField getInvDatF() {
        return invDatF;
    }
    
}
