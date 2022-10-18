package codes.bjornnorgren.testgrades;

import codes.bjornnorgren.testgrades.dto.Student;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReportLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handle(SNSEvent event) throws JsonProcessingException {
        for (SNSEvent.SNSRecord record : event.getRecords()) {
            Student student = objectMapper.readValue(record.getSNS().getMessage(), Student.class);
            System.out.println(student);
        }
    }

}
