package CSSTickets;
/*
everything that has to do with the gui is her, buttons and all that. passes info through controller to get to
other classes.
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    private JButton testButton;

    // declaring table modle and controller
    DefaultTableModel tableModel;

    private Controller controller;
    // gving TicketGui controller so it has access to those methods and the database

    // made all of these final because they should never be changing
    // email regex pattern was taken from here https://stackoverflow.com/questions/14862289/how-to-use-regex-in-java-to-pattern-match
    private final Pattern emailPattern = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    // phone number was from Tom
    private final Pattern phoneNumberPattern = Pattern.compile("(\\d{3})\\.?-?(\\d{3}\\.?-?\\d{4})");
    // this was also Tom, I'm really bad at regex
    private final Pattern starIdPattern = Pattern.compile("[a-z]{2}\\d{4}[a-z]{2}");

    Map<String,String> patterns = new HashMap<>();


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


        // todo sort properly
        // todo save to a file so it's maybe printable
        // todo make validation section better
        // todo maybe print straight from java
        // todo adding the regect patters to a hashmap
        // todo have a better read me and comments 

        // calling a bunch of mehtods that do things
        populateComboBoxes();

        configureTableModel();

        showAllTickets();
        rightClickMenu();
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
            }else if (searchByComboBox.getSelectedItem() == "Email"){
                searchByCategory("Email");
            }else {
                searchByCategory("Club Member");
            }
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
        searchByList.add("Club Member");


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

    protected void showAllTickets(){
        // getting all the tickets from controller and passing them to the set table method
        List<Ticket> tickets = controller.loadAllTicketsFromTicketStore();

        setTableData(tickets);

    }

    private void addNewTicket(){

        // creating a date for when the ticket is added
        Date date =  new Date();
        // getting the rest of the needed info from the gui texfields and areas
        String clientName = clientNameTextField.getText().strip();
        String starID = starIdTextField.getText().strip();
        String email = emailTextField.getText().strip();
        String phoneNumber = phoneNumberTextField.getText().strip();
        String machineModel= machineTextField.getText().strip();
        String description = descriptionTextArea.getText().strip();
        String memberName = memberTextField.getText().strip();
        String resolution = resolutionTextArea.getText().strip();

        // making sure none of the required fields are left blan
        boolean valid = fieldValidation(clientName,starID,email,phoneNumber,machineModel,description,
                memberName,resolution);

        if (valid){
            Ticket ticket = new Ticket(clientName, starID, email, phoneNumber, machineModel, description, memberName,
                    resolution, date);
            controller.addTicket(ticket);
            showAllTickets();
            clearFields();
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

    private void rightClickMenu(){
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
                // added validation in case some weird things happen then making the selection
                int selection = ticketTable.rowAtPoint(e.getPoint());
                if (selection != -1){
                    ticketTable.setRowSelectionInterval(selection, selection);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // added validation in case some weird things happen then making the selection
                int selection = ticketTable.rowAtPoint(e.getPoint());
                if (selection != -1) {
                    ticketTable.setRowSelectionInterval(selection, selection);
                }

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

    // getting all the data from the table itself, currently an unsused method and unlikely to be used
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

        // getting selected row and getting the first value at that row
        int selected = ticketTable.getSelectedRow();
        int rowId = (int) ticketTable.getValueAt(selected,0);

        // making sure a row is selected
        if (selected == -1){
            JOptionPane.showMessageDialog(this, "Select a row to delete");
        }else {
            // asking confirmation if the user wants to delete the row
            if (JOptionPane.showConfirmDialog(this,"Are you sure you want to delete this row?",
                    "Delete",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                // deleting row, and calling controller to delete ticket
                tableModel.removeRow(selected);
                controller.deleteTicket(rowId);
            }
        }
    }

    private void newFramePopup(){
        // making the new window and setting this one as it's parent component, also passing controller
        ShowTicketGUI showTicket = new ShowTicketGUI(TicketGui.this,controller,TicketGui.this);

    }

    public int getSelectedRowId(){
        // getting selected row
        int selected = ticketTable.getSelectedRow();
        // getting the row id for that row and returning it
        int rowId = (int) ticketTable.getValueAt(selected,0);
        return rowId;
    }

    protected boolean validationRegEx(String type, String field){


        if (type == "email"){

            Matcher matcher = emailPattern.matcher(field);

            return matcher.matches();

        }else if (type == "phone number"){

            Matcher matcher = phoneNumberPattern.matcher(field);

            return matcher.matches();
        }else {

            Matcher matcher = starIdPattern.matcher(field);


            return matcher.matches();
        }

    }

    private void clearFields(){
        // clearing all the fields after a successful entry
        clientNameTextField.setText("");
        starIdTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");
        machineTextField.setText("");
        descriptionTextArea.setText("");
        memberTextField.setText("");
        resolutionTextArea.setText("");
    }

    protected boolean fieldValidation (String clientName,String starID,String email, String phoneNumber, String machineModel,
                                                String description,String memberName,String resolution){

        if (clientName.isEmpty() || email.isEmpty() || machineModel.isEmpty() || description.isEmpty()
                || memberName.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill out all the required fields.");
        }else if(!validationRegEx("email",email)){
            JOptionPane.showMessageDialog(this,"Invalid Email adress");
        }else{
            if (!starID.strip().isEmpty()) {
                if (!validationRegEx("star id",starID)) {
                    JOptionPane.showMessageDialog(this, "Invalid StarID");
                } else {
                    if (!phoneNumber.strip().isEmpty()){
                        if (!validationRegEx("phone number",phoneNumber)) {
                            JOptionPane.showMessageDialog(this, "Invalid Phone Number");
                        }else {
                            return true;
                        }
                    }else {
                        return true;
                    }
                }
            }else if (!phoneNumber.strip().isEmpty()){
                if (!validationRegEx("phone number",phoneNumber)){
                    JOptionPane.showMessageDialog(this,"Invalid Phone Number");
                }else {
                    if (!starID.strip().isEmpty()) {
                        if (!validationRegEx("star id",starID)) {
                            JOptionPane.showMessageDialog(this, "Invalid StarID");
                        }else {
                            return true;
                        }
                    }else {
                        return true;
                    }
                }
            }else {
                return true;
            }

        }
        return false;
    }



    }


