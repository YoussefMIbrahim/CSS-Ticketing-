package CSSTickets;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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

    DefaultListModel<Ticket> listModel;

    private Controller controller;

    TicketGui(Controller controller){

        this.controller = controller;

        setTitle("Computer Software Support Ticket System");
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // todo possibly add newest to oldest in date, smae for name search
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
}
