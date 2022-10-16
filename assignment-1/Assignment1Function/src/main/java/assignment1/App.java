package assignment1;

import assignment1.data.Payment;
import assignment1.data.Ticket;
import com.amazonaws.services.lambda.runtime.Context;

import java.util.List;

public class App  {

    public void truckTracker(List<Double> coords) {
        System.out.println("Longitude: " + coords.get(0));
        System.out.println("Latitude: " + coords.get(1));
    }

    public Ticket getTicket(Payment payment) {
        System.out.println("Payment Type: " + payment.getPaymentType());
        System.out.println("Payment Amount: " + payment.getPaymentAmount());
        System.out.println("Payment Date: " + payment.getPaymentDate());

        Ticket ticket = new Ticket();
        ticket.setTicketNumber("123456");
        ticket.setTicketType("Movie");
        ticket.setTicketDate("01/01/2019");
        ticket.setTicketTime("12:00");
        ticket.setTicketLocation("123 Main St");
        ticket.setTicketDescription("Dinoshark");
        return ticket;
    }

}
