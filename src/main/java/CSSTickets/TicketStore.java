package CSSTickets;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class TicketStore {

    private String dbUri;

    TicketStore(String databaseURI){
        this.dbUri = databaseURI;

        // creating the Database and table in a try with resources to catch SQLexceptions
        try(Connection connection = DriverManager.getConnection(dbUri);
            Statement statement = connection.createStatement()){
            // making sure that specific fields are not empty when a new ticket is entered
            String createTable = "CREATE TABLE IF NOT EXISTS tickets(" +
                    "clientName TEXT NOT NULL," +
                    "starID TEXT ," +
                    "email TEXT NOT NULL UNIQUE ," +
                    "phoneNumber TEXT," +
                    "model TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "memberName TEXT NOT NULL," +
                    "resolution TEXT," +
                    "dateReproted NUMBER NOT NULL)";

            statement.executeUpdate(createTable);

        }catch (SQLException sqle){
            throw new RuntimeException(sqle);
        }

    }

    public List<Ticket> getAllTickets(){
        // empty list that will store of the tickets
        ArrayList<Ticket> allTickets = new ArrayList<>();
        // try with resources to catch SQL exceptions
        try(Connection connection = DriverManager.getConnection(dbUri);
        Statement statement = connection.createStatement()){
            // getting all of the tickets in the tickets table and ordering them by date
            ResultSet resultSet = statement.executeQuery("SELECT rowid ,* FROM tickets ORDER BY dateReproted");

            // looping through result set to get everything
            while (resultSet.next()){
                // getting data from all the fields and assigning them to a variable
                String clientName = resultSet.getString("clientName");
                String starID = resultSet.getString("starID");
                String email = resultSet.getString("email");
                String  phoneNumber = resultSet.getString("phoneNumber");
                String model = resultSet.getString("model");
                String description = resultSet.getString("description");
                String memberName = resultSet.getString("memberName");
                String resolution = resultSet.getString("resolution");
                Date date = resultSet.getDate("dateReproted");
                int rowID = resultSet.getInt("rowid");

                // adding the data to a new ticket and putting that into my list
                Ticket ticket = new Ticket(clientName,email,model,description,memberName,date);
                ticket.setTicketId(rowID);
                allTickets.add(ticket);
            }
            System.out.println(allTickets);

            return allTickets;

        }catch (SQLException sqle){
            System.err.println("Could not get all tickets because " + sqle);
            // returning null if an exception is caught
            return null;
        }
    }

    public void addNewTicket(Ticket newTicket) throws SQLException{

        // turning the date into a long so i can store it into the database
        long date = newTicket.getDate().getTime();

        // sql statement that lets me insert the new ticket
        String insertSQL ="INSERT INTO tickets VALUES (?,?,?,?,?,?,?,?,?)";

        // i use a try and catch block in the controller so i don't need one here
        Connection connection= DriverManager.getConnection(dbUri);
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

        // inputting all the values from the ticket passed from gui into my SQl statement
        preparedStatement.setString(1, newTicket.getClientName());
        preparedStatement.setString(2,newTicket.getStarId());
        preparedStatement.setString(3,newTicket.getEmail());
        preparedStatement.setString(4, newTicket.getPhoneNumber());
        preparedStatement.setString(5,newTicket.getModel());
        preparedStatement.setString(6,newTicket.getDescription());
        preparedStatement.setString(7,newTicket.getMemberName());
        preparedStatement.setString(8,newTicket.getResolution());
        preparedStatement.setLong(9,date);
        preparedStatement.execute();
        connection.close();
    }

    public List<Ticket> searchByCategory(String searchTerm, String category) {

        // getting all tickets from the get all tickets method
        List<Ticket> allTickets = getAllTickets();
        // empty list that will hold all of the matching tickets
        List<Ticket> matchingTickets = new ArrayList<>();

        // statements checking which category i'm searching in
        if (category == "Description") {
            // looping over all of the tickets and seeing if any of them match the search term, and ignoring case
            for (Ticket ticket : allTickets) {
                if (ticket.getDescription().toLowerCase().contains(searchTerm.toLowerCase())) {

                    matchingTickets.add(ticket);
                }
            }
            // returning the matching list
            return matchingTickets;
        }else if (category == "Name"){
            // looping over all of the tickets and seeing if any of them match the search term, and ignoring case
            for (Ticket ticket : allTickets) {
                if (ticket.getClientName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchingTickets.add(ticket);
                }
            }
            return matchingTickets;
        }else{
            // looping over all of the tickets and seeing if any of them match the search term, and ignoring case
            for (Ticket ticket : allTickets) {
                if (ticket.getEmail().toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchingTickets.add(ticket);
                }
            }
            return matchingTickets; // returning a list of matching tickets

        }

    }

    public void deleteTicket(int rowId){
        // SLQ statement to delete a ticket by row id
        String query = "DELETE FROM tickets WHERE rowid = ?";

        try(Connection connection = DriverManager.getConnection(dbUri);
        PreparedStatement preparedStatement = connection.prepareStatement(query)){

            // inserting the row id that i got from ticketgui  into the SQl statement
            preparedStatement.setInt(1,rowId);
            preparedStatement.execute();

        }catch (SQLException sqle){
            System.err.println("could not delete because "+ sqle);
        }
    }

    public Ticket getTicketById(int rowId){
        // SQL that gets a ticket by row id
        String sqlQuery = "SELECT rowid, * FROM tickets WHERE rowid = ? ";

        try(Connection connection = DriverManager.getConnection(dbUri);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){

            //inserting the row id into the SQL statement
            preparedStatement.setInt(1,rowId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // getting all the data from that specific ticket
            String clientName = resultSet.getString("clientName");
            String starID = resultSet.getString("starID");
            String email = resultSet.getString("email");
            String phoneNumber = resultSet.getString("phoneNumber");
            String model = resultSet.getString("model");
            String description = resultSet.getString("description");
            String memberName = resultSet.getString("memberName");
            String resolution = resultSet.getString("resolution");
            Date date = resultSet.getDate("dateReproted");
            int rowID = resultSet.getInt("rowid");

            // putting all that data into a ticket and returning it
            Ticket ticket = new Ticket(clientName,starID,email,phoneNumber,model,description,memberName,resolution,date);
            ticket.setTicketId(rowID);

            return ticket;
        }catch (SQLException sqle){
            System.err.println("could not execute query because "+ sqle);
            return null;
        }
    }

    public void updateTicket(Ticket ticket){

        // SQL that updates all fields except for date by rowid
        String sql = "UPDATE tickets SET clientName = ?," +
                "starID = ?," +
                "email = ?," +
                "phoneNumber = ?," +
                "model = ?," +
                "description = ?," +
                "memberName = ?," +
                "resolution = ? WHERE rowid = ?";

        try(Connection connection = DriverManager.getConnection(dbUri);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            // setting all the new data from a ticket we got from the gui
            preparedStatement.setString(1,ticket.getClientName());
            preparedStatement.setString(2,ticket.getStarId());
            preparedStatement.setString(3,ticket.getEmail());
            preparedStatement.setString(4,ticket.getPhoneNumber());
            preparedStatement.setString(5,ticket.getModel());
            preparedStatement.setString(6,ticket.getDescription());
            preparedStatement.setString(7,ticket.getMemberName());
            preparedStatement.setString(8,ticket.getResolution());
            preparedStatement.setInt(9,ticket.getTicketId());

            // executing the statement
            preparedStatement.executeUpdate();


        }catch (SQLException sqle){
            System.err.println("could not complete action because " + sqle);

        }
    }

}
