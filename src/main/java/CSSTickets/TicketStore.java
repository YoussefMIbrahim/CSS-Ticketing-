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
                    "phoneNumber INTEGER," +
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
                int phoneNumber = resultSet.getInt("phoneNumber");
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
        preparedStatement.setInt(4, newTicket.getPhoneNumber());
        preparedStatement.setString(5,newTicket.getModel());
        preparedStatement.setString(6,newTicket.getDescription());
        preparedStatement.setString(7,newTicket.getMemberName());
        preparedStatement.setString(8,newTicket.getResolution());
        preparedStatement.setLong(9,date);
        preparedStatement.execute();
        connection.close();
    }




}
