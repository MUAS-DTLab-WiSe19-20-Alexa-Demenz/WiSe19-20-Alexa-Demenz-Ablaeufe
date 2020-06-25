package ablaeufe.handlers;

import ablaeufe.Speicher;
import ablaeufe.fachklassen.Ablauf;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ablaeufe.PhrasesAndConstants.*;

public class GetAllProceduresIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("GetAllProceduresIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        Ablauf ablauf = new Ablauf(handlerInput);
        if (!handlerInput.getAttributesManager().getSessionAttributes().containsKey(USER_ACCOUNT_KEY)) {
            ablauf.setOAuthToken();
        }
        String speechText = Speicher.getAllProcedures(ablauf.getUserID());
       /* String TableName = "Procedures_Files";
        String speechText = "Verfügbare Abläufe sind: ";
        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1)
                    .build();
            DynamoDB db = new DynamoDB(client);

            TableKeysAndAttributes ProcedureTableKeysAndAttributes = new TableKeysAndAttributes(TableName);
            // Add a partition key
            ProcedureTableKeysAndAttributes.addHashOnlyPrimaryKeys("Name", "Amazon S3", "Amazon DynamoDB");

            BatchGetItemOutcome outcome = db.batchGetItem(ProcedureTableKeysAndAttributes);

            List<Item> items = outcome.getTableItems().get(ProcedureTableKeysAndAttributes);
            for (Item item : items) {
                speechText = speechText + item + "und";
            }

        }
        catch (Exception e) {
            System.err.println("Unable to read entrys!");
            System.err.println(e.getMessage());
            return Optional.empty();
        }*/
        return handlerInput.getResponseBuilder()
                .withSimpleCard(CARD_TITLE,speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .build();
    }
}
