package customers.dto;

public class CustomerWithId extends Customer {
    public String id;

    public CustomerWithId() {
    }

    public CustomerWithId(String id, String firstName, String lastName, int rewardPoints) {
        super(firstName, lastName, rewardPoints);
        this.id = id;
    }
}
