package CSSTickets;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private JButton writeToFileButton;

    private Controller controller;
    private TicketGui ticketGui;

    ShowTicketGUI(final TicketGui parentComponent, Controller controller, TicketGui ticketGui){

        // all the setup stuff and things
        this.controller = controller;
        this.ticketGui = ticketGui;

        setContentPane(mainPanel);
        pack();
        setVisible(true);
        // making the parent gui inaccessible while this one is active
        parentComponent.setEnabled(false);
        // setting closing operation to do nothing
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // adding a window listener to close button to set focus back to parent component before disposing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentComponent.setEnabled(true);
                dispose();
            }
        });


        // calling a method to set the data for the gui
        setDataForTicket();

        exitButton.addActionListener(e -> {
            // changing the focus back to the main window then disposing this one
            parentComponent.setEnabled(true);
            dispose();
        });

        saveChangesButton.addActionListener(e -> {
            // running the save changes method then closing this window
            saveChanges();
            parentComponent.setEnabled(true);
            dispose();

        });

    }

    private void setDataForTicket(){
        // getting the row id and then getting the specific ticket from the database
        int rowId = ticketGui.getSelectedRowId();
        Ticket ticket = controller.getTicketById(rowId);

        // setting the data from the ticket i got to the popup gui
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
        // getting row id for the ticket
        int rowId = ticketGui.getSelectedRowId();

        // getting all the text fields for any possible changes
        String clientName = clientNameTextField.getText();
        String starID = starIdTextField.getText();
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String machineModel = deviceModelTextField.getText();
        String description = descriptionTextArea.getText();
        String memberName = clubMemberNameTextField.getText();
        String resolution = resolutionTextArea.getText();

        // making sure all the required fields are still filled out
        if (clientName.isEmpty() || email.isEmpty() || machineModel.isEmpty() || description.isEmpty()
                || memberName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please make sure the required fields are not empty");
        } else {
            // asking if the user want's to make these changes
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to save these changes?",
                    "Save", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                // adding all of this to a new ticket and passing it to the tiketstore to update the ticket
                Ticket ticket = new Ticket(clientName, starID, email, phoneNumber, machineModel, description, memberName, resolution);
                ticket.setTicketId(rowId);

                controller.updateTicket(ticket);

            }
        }

    }
}
