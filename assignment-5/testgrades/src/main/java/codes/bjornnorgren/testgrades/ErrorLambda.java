package codes.bjornnorgren.testgrades;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorLambda {

    private final Logger logger = LoggerFactory.getLogger(ErrorLambda.class);

    public void handle(SNSEvent event) {
        event.getRecords().forEach(record -> logger.error(record.toString()));
    }

}
