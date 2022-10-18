package codes.bjornnorgren.testgrades;

import codes.bjornnorgren.testgrades.dto.Student;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GradingLambda {

    private final Logger logger = LoggerFactory.getLogger(GradingLambda.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
    private final String topic = System.getenv("TEST_GRADES_TOPIC");

    public void handle(S3Event s3Event) {
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            S3ObjectInputStream s3Stream = getS3Stream(record);
            Student student = null;
            try {
                student = getStudent(s3Stream);
            } catch (IOException e) {
                logger.error("Error reading student from S3", e);
                continue;
            }
            System.out.println(student);
            gradeStudent(student);
            try {
                publishGradedStudent(student);
            } catch (JsonProcessingException e) {
                logger.error("Error publishing student to SNS", e);
                continue;
            }
            try {
                s3Stream.close();
            } catch (IOException e) {
                logger.error("Error closing S3 stream", e);
                continue;
            }
        }
    }

    private void gradeStudent(Student student) {
        student.grade = getGrade(student.score);
    }

    private Student getStudent(S3ObjectInputStream s3Stream) throws IOException {
        return objectMapper.readValue(s3Stream, Student.class);
    }

    private S3ObjectInputStream getS3Stream(S3EventNotification.S3EventNotificationRecord record) {
        String bucketName = record.getS3().getBucket().getName();
        String objectKey = record.getS3().getObject().getKey();
        return s3.getObject(bucketName, objectKey).getObjectContent();
    }

    private void publishGradedStudent(Student student) throws JsonProcessingException {
        sns.publish(topic, objectMapper.writeValueAsString(student));
    }

    private String getGrade(int score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

}
