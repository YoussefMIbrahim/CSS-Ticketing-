package CSSTickets;

import javax.swing.*;
import java.sql.Connection;

public class ShowTicketGUI extends JFrame{
    private JPanel mainPanel;
    private JTextField clientNameTextField;
    private JTextField starIdTextField;
    private JTextField emailTextField;
    private JTextField phoneNumberTextField;
    private JTextField deviceModelTextField;
    private JTextArea descriptionTextArea;
    private JTextArea resolutionTextArea;
    private JButton exitButton;
    private JButton saveAndExitButton;
    private JTextField clubMemberNameTextField;

    private Controller controller;
    private TicketGui ticketGui;

    ShowTicketGUI(final TicketGui parentComponent, Controller controller, TicketGui ticketGui){

        this.controller = controller;
        this.ticketGui = ticketGui;

        setContentPane(mainPanel);
        pack();
        setVisible(true);
        parentComponent.setEnabled(false);

        getDataForTicket();
        exitButton.addActionListener(e -> {
            parentComponent.setEnabled(true);
            dispose();
        });


    }

    private void getDataForTicket(){
        int rowId = ticketGui.getSelectedRowId();
        Ticket ticket = controller.getTicketById(rowId);
        System.out.println(ticket);
        clientNameTextField.setText(ticket.getClientName());
        starIdTextField.setText(ticket.getStarId());
        emailTextField.setText(ticket.getEmail());
        phoneNumberTextField.setText(ticket.getPhoneNumber());
        deviceModelTextField.setText(ticket.getModel());
        descriptionTextArea.setText(ticket.getDescription());
        clubMemberNameTextField.setText(ticket.getMemberName());

    }
}
