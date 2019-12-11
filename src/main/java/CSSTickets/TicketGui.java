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

    // declaring table modle and controller
    DefaultTableModel tableModel;

    private Controller controller;
    // gving TicketGui controller so it has access to those methods and the database
    TicketGui(Controller controller){

        this.controller = controller;
        // setting a title for the form
        setTitle("Computer Software Support Ticket System");
        //setting main panel, making sure it's visible, giving it specific dimensions, and packing it
        setContentPane(mainPanel);
        setVisible(true);
        setPreferredSize(new Dimension(800,500));
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        // todo possibly also add search by club member name to see all they've done
        // todo save to a file so it's maybe printable
        // todo possibly add description and resolution to the jTable display
        // todo add validation for input
        // todo add validation for mouse input
        // todo clear fields after entering a new ticket

        // calling a bunch of mehtods that do things
        populateComboBoxes();

        configureTableModel();

        showAllTickets();
        mouseListenerStuff();

        submitButton.addActionListener(e -> {
            addNewTicket();

        });

        loadAllTicketsButton.addActionListener(e -> {
            showAllTickets();
        });

        searchButton.addActionListener(e -> {
            // checks which option is selected in the combo box and gives it to the method
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

    private void configureTableModel() {
        // making the table model, enabling sorter, setting the model to the ticket table, and adding the column names
        tableModel = new DefaultTableModel();
        ticketTable.setModel(tableModel);
        ticketTable.setAutoCreateRowSorter(true);
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel.addColumn("ID");
        tableModel.addColumn("Client Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Club member");
        tableModel.addColumn("Date");
    }

    private void populateComboBoxes() {

        // adding the three items i want in the combo box to this list
        List<String> searchByList = new ArrayList<>();
        searchByList.add("Name");
        searchByList.add("Description");
        searchByList.add("Email");


        // looping over the list and adding each item to combo box
        for (String term: searchByList){
            searchByComboBox.addItem(term);
        }

    }

    private void setTableData(List<Ticket> tickets){
        // clearing table model
        tableModel.setRowCount(0);
        // adding data from each ticket in list to the model
        if (tickets != null){
            for (Ticket ticket :tickets){
                tableModel.addRow( new Object[] {ticket.getTicketId(),ticket.getClientName(),ticket.getEmail(),
                        ticket.getMemberName(),ticket.getDate()});
            }
        }
    }

    private void showAllTickets(){
        // getting all the tickets from controller and passing them to the set table method
        List<Ticket> tickets = controller.loadAllTicketsFromTicketStore();

        setTableData(tickets);

    }

    private void addNewTicket(){

        // creating a date for when the ticket is added
        Date date =  new Date();
        // getting the rest of the needed info from the gui texfields and areas
        String clientName = clientNameTextField.getText();
        String starID = starIdTextField.getText();
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String machineModel= machineTextField.getText();
        String description = descriptionTextArea.getText();
        String memberName = memberTextField.getText();
        String resolution = resolutionTextArea.getText();

        // making sure none of the required fields are left blank
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

            // clearing all the fields after a successful entry
            clientNameTextField.setText("");
            starIdTextField.setText("");
            emailTextField.setText("");
            phoneNumberTextField.setText("");
            machineTextField.setText("");
            descriptionTextArea.setText("");
            memberTextField.setText("");
            resolutionTextArea.setText("");
            // sending a message to let user know that the ticket has been added (might remove )
            JOptionPane.showMessageDialog(this,"Ticket added successfully");
        }


    }

    private void searchByCategory(String category){
        // getting the search term from gui
        String searchTerm = searchByTextField.getText();
        // getting all the matching tickets for that search
        List<Ticket> matchingTickets = controller.searchByCategory(searchTerm,category);

        // checking if there was anything found
        if (matchingTickets.size() < 1){
            // returning a message that nothing was found if the list is empty
            JOptionPane.showMessageDialog(this,"No matches found");

        }else{
            // setting the list model to have this new data in it if there were tickets found
            setTableData(matchingTickets);
        }
    }

    private void mouseListenerStuff(){
        // creating a new popup menu called rightclickmenu
        JPopupMenu rightClickMenu = new JPopupMenu();
        // creating new menu items to add to the right lick menu
        JMenuItem deleteMenuItem = new JMenuItem("Delete Row");
        JMenuItem editMenuItem = new JMenuItem("View/Edit");
        // adding the items to the right click menu
        rightClickMenu.add(deleteMenuItem);
        rightClickMenu.add(editMenuItem);
        // setting the right click menu to the ticket table
        ticketTable.setComponentPopupMenu(rightClickMenu);

        // clicking either delete or view/edit will call other methods
        deleteMenuItem.addActionListener(e -> {
            // calling a method that deletes a row in the table
            deleteTableRow();

        });

        editMenuItem.addActionListener(e -> {
            // calling a method that opens a new gui
            newFramePopup();

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
                ticketTable.setRowSelectionInterval(selection -1 , selection);

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {


            }

            @Override
            public void mouseExited(MouseEvent e) {

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

    private void newFramePopup(){

        ShowTicketGUI showTicket = new ShowTicketGUI(TicketGui.this,controller,TicketGui.this);



    }

    public int getSelectedRowId(){
        int selected = ticketTable.getSelectedRow();
        int rowId = (int) ticketTable.getValueAt(selected,0);
        return rowId;
    }
    }


