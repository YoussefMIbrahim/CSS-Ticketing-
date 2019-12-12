package CSSTickets;

import java.util.Date;

public class Ticket {

    // all the fields i will need for my ticket
    private int ticketId;
    private String clientName;
    private String starId;
    private String email;
    private String phoneNumber;
    private String model;
    private String description;
    private String memberName;
    private String resolution;
    private Date date;

// four constructors each for a specific situation of where the ticket is being used
    Ticket (String clientName, String email,String model,String description,String memberName,Date date){
        this.clientName = clientName;
        this.email = email;
        this.model = model;
        this.description = description;
        this.memberName = memberName;
        this.date =  date;
    }


    Ticket (String clientName, String email,String model,String description,String memberName,String resolution,Date date){
        this.clientName = clientName;
        this.email = email;
        this.model = model;
        this.description = description;
        this.memberName = memberName;
        this.resolution = resolution;
        this.date =  date;
    }

    Ticket (String clientName,String starId, String email,String phoneNumber,String model,String description,
            String memberName,String resolution,Date date){
        this.clientName = clientName;
        this.starId = starId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.model = model;
        this.description = description;
        this.memberName = memberName;
        this.resolution = resolution;
        this.date =  date;
    }

    Ticket (String clientName,String starId, String email,String phoneNumber,String model,String description,
            String memberName,String resolution){
        this.clientName = clientName;
        this.starId = starId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.model = model;
        this.description = description;
        this.memberName = memberName;
        this.resolution = resolution;
    }


    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStarId() {
        return starId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // to string method thats currently unused, might remove it
    public String toString(){

        String output = String.format("Id: %d, Client Name: %s, Email: %s, Club Member: %s Date: %s",
                this.ticketId, this.clientName,this.email,this.memberName,this.date);

        return output;
    }
}
