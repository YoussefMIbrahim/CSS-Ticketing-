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
    private JButton saveChangesButton;
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

        setDataForTicket();
        exitButton.addActionListener(e -> {
            parentComponent.setEnabled(true);
            dispose();
        });

        saveChangesButton.addActionListener(e -> {

            saveChanges();
            parentComponent.setEnabled(true);
            dispose();

        });


    }

    private void setDataForTicket(){
        int rowId = ticketGui.getSelectedRowId();
        Ticket ticket = controller.getTicketById(rowId);


        clientNameTextField.setText(ticket.getClientName());
        starIdTextField.setText(ticket.getStarId());
        emailTextField.setText(ticket.getEmail());
        phoneNumberTextField.setText(ticket.getPhoneNumber());
        deviceModelTextField.setText(ticket.getModel());
        descriptionTextArea.setText(ticket.getDescription());
        clubMemberNameTextField.setText(ticket.getMemberName());
        resolutionTextArea.setText(ticket.getResolution());

    }

    private void saveChanges() {

        int rowId = ticketGui.getSelectedRowId();

        String clientName = clientNameTextField.getText();
        String starID = starIdTextField.getText();
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String machineModel = deviceModelTextField.getText();
        String description = descriptionTextArea.getText();
        String memberName = clubMemberNameTextField.getText();
        String resolution = resolutionTextArea.getText();

        if (clientName.isEmpty() || email.isEmpty() || machineModel.isEmpty() || description.isEmpty()
                || memberName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please make sure the required fields are not empty");
        } else {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to save these changes?",
                    "Save", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                Ticket ticket = new Ticket(clientName, starID, email, phoneNumber, machineModel, description, memberName, resolution);
                ticket.setTicketId(rowId);

                controller.updateTicket(ticket);

            }
        }

    }
}
