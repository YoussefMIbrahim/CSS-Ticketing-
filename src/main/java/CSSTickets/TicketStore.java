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


        try(Connection connection = DriverManager.getConnection(dbUri);
            Statement statement = connection.createStatement()){

            String createTable = "CREATE TABLE IF NOT EXISTS tickets(" +
                    "clientName TEXT NOT NULL," +
                    "starID TEXT UNIQUE ," +
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

        ArrayList<Ticket> allTickets = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(dbUri);
        Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT rowid ,* FROM tickets ORDER BY dateReproted");

            while (resultSet.next()){
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

                Ticket ticket = new Ticket(clientName,email,model,description,memberName,date);
                ticket.setTicketId(rowID);
                allTickets.add(ticket);
            }
            System.out.println(allTickets);

            return allTickets;

        }catch (SQLException sqle){
            System.err.println("Could not get all tickets because " + sqle);
            return null;
        }
    }

    public void addNewTicket(Ticket newTicket) throws SQLException{

        long date = newTicket.getDate().getTime();

        String insertSQL ="INSERT INTO tickets VALUES (?,?,?,?,?,?,?,?,?)";

        Connection connection= DriverManager.getConnection(dbUri);
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);


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


        List<Ticket> allTickets = getAllTickets();
        List<Ticket> matchingTickets = new ArrayList<>();

        if (category == "Description") {
            for (Ticket ticket : allTickets) {
                if (ticket.getDescription().toLowerCase().contains(searchTerm.toLowerCase())) {

                    matchingTickets.add(ticket);
                }
            }
            // returning the matching list
            return matchingTickets;
        }else if (category == "Name"){
            for (Ticket ticket : allTickets) {
                if (ticket.getClientName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchingTickets.add(ticket);
                }
            }
            return matchingTickets;
        }else{
            for (Ticket ticket : allTickets) {
                if (ticket.getEmail().toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchingTickets.add(ticket);
                }
            }
            return matchingTickets;

        }

    }

    public void deleteTicket(int rowId){

        String query = "DELETE FROM tickets WHERE rowid = ?";

        try(Connection connection = DriverManager.getConnection(dbUri);
        PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1,rowId);
            preparedStatement.execute();

        }catch (SQLException sqle){
            System.err.println("could not delete because "+ sqle);
        }
    }

    public Ticket getTicketById(int rowId){
        String sqlQuery = "SELECT rowid, * FROM tickets WHERE rowid = ? ";

        try(Connection connection = DriverManager.getConnection(dbUri);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){

            preparedStatement.setInt(1,rowId);
            ResultSet resultSet = preparedStatement.executeQuery();

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

            Ticket ticket = new Ticket(clientName,starID,email,phoneNumber,model,description,memberName,resolution,date);
            ticket.setTicketId(rowID);

            return ticket;
        }catch (SQLException sqle){
            System.err.println("could not execute query because "+ sqle);
            return null;
        }
    }

    public void updateTicket(Ticket ticket){

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

            preparedStatement.setString(1,ticket.getClientName());;
            preparedStatement.setString(2,ticket.getStarId());;
            preparedStatement.setString(3,ticket.getEmail());
            preparedStatement.setString(4,ticket.getPhoneNumber());
            preparedStatement.setString(5,ticket.getDescription());
            preparedStatement.setString(6,ticket.getMemberName());
            preparedStatement.setString(7,ticket.getResolution());
            preparedStatement.setInt(8,ticket.getTicketId());

            preparedStatement.execute();


        }catch (SQLException sqle){
            System.err.println("could not complete action because " + sqle);

        }
    }

}
