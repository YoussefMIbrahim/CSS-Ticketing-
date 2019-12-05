package CSSTickets;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class TicketGui extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JTextField clientNameTextField;
    private JTextField starIdTextField;
    private JTextField emailTextField;
    private JTextField phoneNumberTextField;
    private JTextField machineTextField;
    private JTextArea descriptionTextArea;
    private JTextField memberTextField;
    private JButton submitButton;
    private JComboBox searchByComboBox;
    private JList<Ticket> ticketList;
    private JComboBox orderByComboBox;
    private JButton searchButton;
    private JTextField searchByTextField;
    private JButton loadAllTicketsButton;
    private JTable ticketTable;
    private JTextArea resolutionTextArea;

    DefaultListModel<Ticket> listModel;
    DefaultTableModel tableModel;

    private Controller controller;

    TicketGui(Controller controller){

        this.controller = controller;

        setTitle("Computer Software Support Ticket System");
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setPreferredSize(new Dimension(1000,1000));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // todo possibly add newest to oldest in date, smae for name search
        // todo adding a differnt window to view a full ticket
        // todo possibly also add search by club member name to see all they've done
        // todo organize the ticket display so it looks nicer


        List<String> searchByList = new ArrayList<>();
        searchByList.add("Name");
        searchByList.add("Description");
        searchByList.add("Star ID");

        List<String> orderByList = new ArrayList<>();
        orderByList.add("Date");
        orderByList.add("Name");

        for (String term: searchByList){
            searchByComboBox.addItem(term);
        }

        for (String term: orderByList){
            orderByComboBox.addItem(term);
        }


        tableModel = new DefaultTableModel();
        ticketTable.setModel(tableModel);
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel.addColumn("ID");
        tableModel.addColumn("Client Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Club member");
        tableModel.addColumn("Date");

        showAllTickets();

        submitButton.addActionListener(e -> {
            addNewTicket();
        });

    }


    private void setTableData(List<Ticket> tickets){

        if (tickets != null){
            for (Ticket ticket :tickets){
                tableModel.addRow( new Object[] {ticket.getTicketId(),ticket.getClientName(),ticket.getEmail(),
                        ticket.getMemberName(),ticket.getDate()});
            }
        }
    }

    private void showAllTickets(){
        List<Ticket> tickets = controller.loadAllTicketsFromTicketStore();

        setTableData(tickets);

    }
    private void updateTable(Ticket ticket){

        tableModel.addRow( new Object[] {ticket.getTicketId(),ticket.getClientName(),ticket.getEmail(),
                ticket.getMemberName(),ticket.getDate()});

    }

    private void addNewTicket(){

        Date date =  new Date();
        String clientName = clientNameTextField.getText();
        String starID = starIdTextField.getText();
        String email = emailTextField.getText();
//        int phoneNumber = phoneNumberTextField.getText();
        String machineModel= machineTextField.getText();
        String description = descriptionTextArea.getText();
        String memberName = memberTextField.getText();
        String resolution = resolutionTextArea.getText();

        if (clientName.isEmpty() || email.isEmpty() || machineModel.isEmpty() || description.isEmpty()
                || memberName.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill out all the required fields.");
        }else{

            if (resolution.isEmpty()){

                Ticket ticket =  new Ticket(clientName,email,machineModel,description,memberName,date);
                controller.addTicket(ticket);
                updateTable(ticket);
            }else{
                Ticket ticket = new Ticket(clientName,email,machineModel,description,memberName,resolution,date);
                controller.addTicket(ticket);
                updateTable(ticket);
            }


        }

    }
}
