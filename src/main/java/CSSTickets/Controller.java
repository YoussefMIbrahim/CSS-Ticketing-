package CSSTickets;

import java.util.List;

public class Controller {

    TicketStore store;

    Controller(TicketStore store){
        this.store = store;
    }

    protected List<Ticket> loadAllTicketsFromTicketStore(){

        List<Ticket> allTickets = store.getAllTickets();

        return allTickets;

    }
}
