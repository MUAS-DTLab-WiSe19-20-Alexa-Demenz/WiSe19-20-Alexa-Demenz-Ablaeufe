package ablaeufe.handlers;


import ablaeufe.DBUtil;
import ablaeufe.Speicher;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import ablaeufe.PhrasesAndConstants;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ablaeufe.PhrasesAndConstants.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TestUtil {
    public static final DBUtil util = new DBUtil();
    public static HandlerInput mockHandlerInput(String ablauf,
                                                Map<String, Object> sessionAttributes,
                                                Map<String, Object> persistentAttributes,
                                                Map<String, Object> requestAttributes) {
        final AttributesManager attributesManagerMock = Mockito.mock(AttributesManager.class);
        when(attributesManagerMock.getSessionAttributes()).thenReturn(sessionAttributes);
        when(attributesManagerMock.getPersistentAttributes()).thenReturn(persistentAttributes);
        when(attributesManagerMock.getRequestAttributes()).thenReturn(requestAttributes);

        // Mock Slots
        final RequestEnvelope requestEnvelopeMock = RequestEnvelope.builder()
                .withRequest(IntentRequest.builder()
                        .withIntent(Intent.builder()
                                .putSlotsItem(ABLAUF_SLOT, Slot.builder()
                                        .withName(ABLAUF_SLOT)
                                        .withValue(ablauf)
                                        .build())
                                .build())
                        .build())
                .build();


        // Mock Handler input attributes
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        when(input.getAttributesManager()).thenReturn(attributesManagerMock);
        when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(input.getRequestEnvelope()).thenReturn(requestEnvelopeMock);

        return input;
    }

    public static Response standardTestForHandle(RequestHandler handler) {
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        sessionAttributes.put(ABLAUF_KEY, "Test");
        persistentAttributes.put(ABLAUF_KEY, "Test");
        final HandlerInput inputMock = TestUtil.mockHandlerInput(null, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        //assertFalse(response.getShouldEndSession());
        assertNotEquals("Test", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
        return response;
    }
    public static Response sessionAttributesTestForHandle(RequestHandler handler) {
        final Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put(ABLAUF_KEY, "rausgehen");
        final HandlerInput inputMock = TestUtil.mockHandlerInput(null, sessionAttributes, null, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        //in the WhatsMyColorIntentHandler
        //assertTrue(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        return response;
    }
    public static Response persistentAttributesTestForHandle(RequestHandler handler) {
        final Map<String, Object> persistentAttributes = new HashMap<>();
        persistentAttributes.put(ABLAUF_KEY, "kochen");
        final HandlerInput inputMock = TestUtil.mockHandlerInput(null, null, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        //in the WhatsMyColorIntentHandler
        //assertTrue(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        return response;
    }

    public static Response noAttributesTestForHandle(RequestHandler handler) {
        final HandlerInput inputMock = TestUtil.mockHandlerInput(null, null, null, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertFalse(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        return response;
    }

    public static Response sessionEndedTestForHandle(RequestHandler handler) {
        final HandlerInput inputMock = TestUtil.mockHandlerInput(null, null, null, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();
        assertTrue(response.getShouldEndSession());
        return response;
    }

    public static Speicher getSpeicher() {
        AmazonDynamoDB testDB;
        try {
            util.before();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        testDB = util.getAmazonDynamoDB();

        Speicher speicher = new Speicher(testDB);
        DynamoDB db = new DynamoDB(testDB);
        Table proceduresTable = db.createTable("Procedures_Data", Arrays.asList(new KeySchemaElement("UserID", KeyType.HASH),
                new KeySchemaElement("procedureName", KeyType.RANGE)), Arrays.asList(
                new AttributeDefinition("jsonFile", ScalarAttributeType.S)),
                new ProvisionedThroughput(10L, 10L));
        Table logTable = db.createTable("Procedures_Log", Arrays.asList(new KeySchemaElement("UserID", KeyType.HASH),
                new KeySchemaElement("startTime", KeyType.RANGE)), Arrays.asList(
                new AttributeDefinition("endState", ScalarAttributeType.S),
                new AttributeDefinition("endTime", ScalarAttributeType.S),
                new AttributeDefinition("procedureName", ScalarAttributeType.S)),
                new ProvisionedThroughput(10L, 10L));

        return speicher;
    }
}
