package CSSTickets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TicketStore {

    private String dbUri;

    TicketStore(String databaseURI){
        this.dbUri = databaseURI;


        try(Connection connection = DriverManager.getConnection(dbUri);
            Statement statement = connection.createStatement()){

            String createTable = "CREATE TABLE IF NOT EXISTS tickets(" +
                    "clientName TEXT NOT NULL," +
                    "starID TEXT," +
                    "email TEXT NOT NULL," +
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
}
