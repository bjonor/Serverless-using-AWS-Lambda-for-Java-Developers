package customers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import customers.dto.Customer;

import java.util.UUID;

public class CreateCustomerLambda {

    private final ObjectMapper objectMapper;
    private final Table table;

    public CreateCustomerLambda() {
        objectMapper = new ObjectMapper();
        DynamoDB db = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
        table = db.getTable(System.getenv("CUSTOMERS_TABLE"));
    }

    public APIGatewayProxyResponseEvent createCustomer(APIGatewayProxyRequestEvent request, Context context) throws JsonProcessingException {
        String id = UUID.fromString(context.getAwsRequestId()).toString();

        Customer customer = objectMapper.readValue(request.getBody(), Customer.class);

        Item item = new Item().withPrimaryKey("id", id)
                .withString("firstName", customer.firstName)
                .withString("lastName", customer.lastName)
                .withInt("rewardPoints", customer.rewardPoints);
        table.putItem(item);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("{ \"customerId\": \"" + id + "\" }");
    }

}
