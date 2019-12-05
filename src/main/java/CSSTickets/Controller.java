package CSSTickets;

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

    protected Vector getColumnNames(){
        Vector<String> colNames = store.getColumnNames();
        return colNames;
    }
}
