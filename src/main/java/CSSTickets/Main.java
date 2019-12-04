package CSSTickets;

public class Main {

    public static void main(String[] args) {
        String databaseURI = DBConfig.DBUri;

        TicketStore ticketStore = new TicketStore(databaseURI);
        Controller controller = new Controller(ticketStore);
        TicketGui ticketGui = new TicketGui(controller);

    }
}
