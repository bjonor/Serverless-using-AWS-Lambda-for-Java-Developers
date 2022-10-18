package customers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import customers.dto.Customer;
import customers.dto.CustomerWithId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReadCustomersLambda {

    private final String tableName;
    private final AmazonDynamoDB db;
    private final ObjectMapper objectMapper;

    public ReadCustomersLambda() {
        tableName = System.getenv("CUSTOMERS_TABLE");
        db = AmazonDynamoDBClientBuilder.defaultClient();
        objectMapper = new ObjectMapper();
    }

    public APIGatewayProxyResponseEvent readCustomers(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        ScanResult result = db.scan(new ScanRequest().withTableName(tableName));

        List<Customer> orders = result.getItems()
                .stream()
                .map(ReadCustomersLambda::createCustomerFromItem)
                .collect(Collectors.toList());

        String body = objectMapper.writeValueAsString(orders);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(body);
    }

    private static Customer createCustomerFromItem(Map<String, AttributeValue> item) {
        return new CustomerWithId(
                item.get("id").getS(),
                item.get("firstName").getS(),
                item.get("lastName").getS(),
                Integer.parseInt(item.get("rewardPoints").getN())
        );
    }

}
