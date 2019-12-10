package CSSTickets;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Vector;


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
    private JButton saveChangesButton;

    DefaultListModel<Ticket> listModel;
    DefaultTableModel tableModel;

    private Controller controller;

    TicketGui(Controller controller){

        this.controller = controller;

        setTitle("Computer Software Support Ticket System");
        setContentPane(mainPanel);
        setVisible(true);
        setPreferredSize(new Dimension(500,500));
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        // todo adding a different window to view a full ticket
        // todo possibly also add search by club member name to see all they've done
        // todo save to a file so it's maybe printable
        // todo possibly add description and resolution to the jTable display
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
                searchByCategory("Email");
            }
        });

        saveChangesButton.addActionListener(e -> {
            testGettingDataFromTAble();
        });

    }

    private void populateComboBoxes() {

        List<String> searchByList = new ArrayList<>();
        searchByList.add("Name");
        searchByList.add("Description");
        searchByList.add("Email");

        List<String> orderByList = new ArrayList<>();
        orderByList.add("Date");
        orderByList.add("Name");

        for (String term: searchByList){
            searchByComboBox.addItem(term);
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

        JMenuItem deleteMenuItem = new JMenuItem("Delete Row");
        JMenuItem editMenuItem = new JMenuItem("View/Edit");

        rightClickMenu.add(deleteMenuItem);
        rightClickMenu.add(editMenuItem);


        ticketTable.setComponentPopupMenu(rightClickMenu);

        deleteMenuItem.addActionListener(e -> {
            deleteTableRow();

        });

        editMenuItem.addActionListener(e -> {

        });


        ticketTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int selection = ticketTable.rowAtPoint(e.getPoint());
                ticketTable.setRowSelectionInterval(selection -1 , selection);

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

    private void testGettingDataFromTAble(){

        for (Vector row: tableModel.getDataVector()){
            int id = (int) row.get(0);
            String clientName = (String) row.get(1);
            String email = (String) row.get(2);
            String clubMember = (String) row.get(3);
            Date date = (Date) row.get(4);

            System.out.println(id+clientName+email+clubMember+date);

        }

    }

    private void deleteTableRow(){

        int selected = ticketTable.getSelectedRow();
        int rowId = (int) ticketTable.getValueAt(selected,0);

        if (selected == -1){
            JOptionPane.showMessageDialog(this, "Select a row to delete");
        }else {

            if (JOptionPane.showConfirmDialog(this,"Are you sure you want to delete this row?",
                    "Delete",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){

                tableModel.removeRow(selected);
                controller.deleteTicket(rowId);
            }
        }
    }

    }

