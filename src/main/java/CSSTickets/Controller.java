package CSSTickets;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class Controller {

    // getting access to the store class
    TicketStore store;

    Controller(TicketStore store){
        this.store = store;
    }

    //passing all the tickets from store to gui
    protected List<Ticket> loadAllTicketsFromTicketStore(){

        List<Ticket> allTickets = store.getAllTickets();

        return allTickets;

    }
    // passing ticket info from gui to store
    protected boolean addTicket(Ticket newTicket){

        try{
            store.addNewTicket(newTicket);
            return true;

        }catch (SQLException sqle){
            return false;
        }

    }

    // taking search terms from gui and using them to search in store then returning the matching tickets to gui
    protected List<Ticket> searchByCategory(String searchTerm, String searchCategory){
        List<Ticket> matchingTickets = store.searchByCategory(searchTerm,searchCategory);
        return matchingTickets;
    }

    // passing the rowid for the ticket marked for deletion from gui to store
    protected void deleteTicket(int rowId){
        store.deleteTicket(rowId);
    }

    // passing row id for ticket that we're trying to get from DB to store
    protected Ticket getTicketById(int rowId){
        Ticket ticket = store.getTicketById(rowId);
        return ticket;
    }

    // giving store a new ticket from the gui to input into the database through store
    protected void updateTicket(Ticket ticket){
        store.updateTicket(ticket);
    }


}
