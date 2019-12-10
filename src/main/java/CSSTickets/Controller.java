package CSSTickets;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class Controller {

    TicketStore store;

    Controller(TicketStore store){
        this.store = store;
    }

    protected List<Ticket> loadAllTicketsFromTicketStore(){

        List<Ticket> allTickets = store.getAllTickets();

        return allTickets;

    }

    protected boolean addTicket(Ticket newTicket){

        try{
            store.addNewTicket(newTicket);
            return true;

        }catch (SQLException sqle){
            return false;
        }

    }

    protected List<Ticket> searchByCategory(String searchTerm, String searchCategory){
        List<Ticket> matchingTickets = store.searchByCategory(searchTerm,searchCategory);
        return matchingTickets;
    }

    protected void deleteTicket(int rowId){
        store.deleteTicket(rowId);
    }


}
