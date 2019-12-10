package CSSTickets;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

        // todo possibly add newest to oldest in date, same for name search
        // todo adding a different window to view a full ticket
        // todo possibly also add search by club member name to see all they've done
        // todo save to a file so it's maybe printable
        // todo possibly add description and resolution to the jTable display
        // todo figure out what's wrong with star id search
        // todo figure out how to make right click select an row
        // todo add stuff to the edit button in right click menu


        populateComboBoxes();

        tableModel = new DefaultTableModel();
        ticketTable.setModel(tableModel);
        ticketTable.setAutoCreateRowSorter(true);
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel.addColumn("ID");
        tableModel.addColumn("Client Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Club member");
        tableModel.addColumn("Date");

        showAllTickets();
        mouseListenerStuff();

        submitButton.addActionListener(e -> {
            addNewTicket();
        });

        loadAllTicketsButton.addActionListener(e -> {
            showAllTickets();
        });

        searchButton.addActionListener(e -> {
            if(searchByComboBox.getSelectedItem() == "Description"){
                searchByCategory("Description");
            }else if (searchByComboBox.getSelectedItem() == "Name"){
                searchByCategory("Name");
            }else{
                searchByCategory("Star ID");
            }
        });

    }

    private void populateComboBoxes() {

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
    }

    private void setTableData(List<Ticket> tickets){

        tableModel.setRowCount(0);

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
                ticket.setStarId(starID);
                controller.addTicket(ticket);
                showAllTickets();
            }else{
                Ticket ticket = new Ticket(clientName,email,machineModel,description,memberName,resolution,date);
                ticket.setStarId(starID);
                controller.addTicket(ticket);
                showAllTickets();
            }

        }

    }

    private void searchByCategory(String category){
        String searchTerm = searchByTextField.getText();
        List<Ticket> matchingTickets = controller.searchByCategory(searchTerm,category);

        if (matchingTickets.size() < 1){
            JOptionPane.showMessageDialog(this,"No matches found");

        }else{
            setTableData(matchingTickets);
        }
    }

    private void mouseListenerStuff(){

        JPopupMenu rightClickMenu = new JPopupMenu();
        //creating the menu items and then adding them to each menu

        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        JMenuItem editMenuItem = new JMenuItem("Edit");

        rightClickMenu.add(deleteMenuItem);
        rightClickMenu.add(editMenuItem);


        ticketTable.setComponentPopupMenu(rightClickMenu);

        deleteMenuItem.addActionListener(e -> {

        });

        editMenuItem.addActionListener(e -> {

        });


        //mouse listeners for both lists
        // i had to add the code under mouse pressed for it to work properly on my machine



        ticketTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selection = ticketTable.rowAtPoint(e.getPoint());
                ticketTable.setEditingRow(selection);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int selection = ticketTable.rowAtPoint(e.getPoint());
                ticketTable.setEditingRow(selection);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int selection = ticketTable.rowAtPoint(e.getPoint());
                ticketTable.setEditingRow(selection);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                int selection = ticketTable.rowAtPoint(e.getPoint());
                ticketTable.setEditingRow(selection);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                int selection = ticketTable.rowAtPoint(e.getPoint());
                ticketTable.setEditingRow(selection);

            }
        });
    }

    }

