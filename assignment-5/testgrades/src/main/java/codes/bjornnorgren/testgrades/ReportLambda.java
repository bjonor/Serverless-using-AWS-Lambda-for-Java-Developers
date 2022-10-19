package codes.bjornnorgren.testgrades;

import codes.bjornnorgren.testgrades.dto.Student;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportLambda {

    private final Logger logger = LoggerFactory.getLogger(ReportLambda.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handle(SNSEvent event) {
        for (SNSEvent.SNSRecord record : event.getRecords()) {
            Student student = null;
            try {
                student = objectMapper.readValue(record.getSNS().getMessage(), Student.class);
            } catch (JsonProcessingException e) {
                logger.error("Error reading student from SNS", e);
                continue;
            }
            System.out.println(student);
        }
    }

}
