package ablaeufe;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;


import java.util.HashMap;

import java.util.Map;

import static com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder.defaultClient;
import static ablaeufe.PhrasesAndConstants.*;

public class Speicher {
    private AmazonDynamoDB dynamoDB;

    public Speicher(AmazonDynamoDB testDB) {
        this.dynamoDB = testDB;
    }

    /**
     * Method for saving a finished procedure to the dynamoDB database in table "Procedures_Log".
     *
     * @param userId        use input.getRequestEnvelope().getContext().getSystem().getUser().getUserId()
     * @param procedureName name of the procedure
     * @param startTime     time saved as session attribute at the beginning of a procedure
     * @param endState      cause for ending the procedure of the procedure
     * @return true if the log entry was saved to dynamoDB, false otherwise
     */
    public static boolean saveLogEntry(String userId, String procedureName, int startTime, LogEndStates endState) {
        int endTime = (int)(System.currentTimeMillis() / 1000); // current time as the method is called when finishing a procedure
        String table = "Procedures_Log";
        AmazonDynamoDB db = defaultClient();

        Map<String, AttributeValue> items = new HashMap<>();
        AttributeValue a = new AttributeValue();
        items.put("userID", new AttributeValue(userId));
        items.put("procedureName", new AttributeValue(procedureName));
        a.setN(String.valueOf(startTime));
        items.put("startTime", a);
        a = new AttributeValue();
        a.setN(String.valueOf(endTime));
        items.put("endTime", a);
        items.put("endState", new AttributeValue(endState.toString()));
        try {
            db.putItem(table, items);
            return true;
        }
        catch (Exception e) {
            System.err.println("Unable to add entry!");
            System.err.println("(User: " + userId + ", procedure: " + procedureName + ", startTime: " + startTime + ", endTime: " + endTime + ", endState: " + endState + ")");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Saves Procedure files from the source code to the database. Mainly used for testing.
     * @param ablaufFile the json to be saved
     * @param procedureName the name of the procedure
     * @return true if successful, false otherwise
     */
    public static boolean saveAblaufFile(String ablaufFile, String procedureName) {
        AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1)
                .build();
        Map<String, AttributeValue> items = new HashMap<>();
        items.put(PROCEDURE_NAME_KEY, new AttributeValue(procedureName));
        items.put(ABLAUF_FILE_KEY, new AttributeValue(ablaufFile));
        try {
            db.putItem(PROCEDURES_FILES_TABLE, items);
            return true;
        }
        catch (Exception e) {
            System.err.println("Unable to add entry!");
            System.err.println("procedure: " + procedureName);
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Returns all the procedures available for this userid
     * @param userId the amazon userid token that was used to create the procedures on the website
     * @return all available procedures: the procedures created on this account plus all template procedures
     */
    public static String getAllProcedures(String userId) {
        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1)
                    .build();
            DynamoDB db = new DynamoDB(client);

            ScanRequest scanRequest = new ScanRequest()
                    .withTableName("Procedures_Data");
            ScanResult result = client.scan(scanRequest);
            String speechText = AVAILABLE_PROCEDURES;

            for(int index = 0; index < result.getItems().size(); index++){
                if(result.getItems().get(index).get("userID").getS().equals(userId)
                    || result.getItems().get(index).get("userID").getS().equals("Template")) {
                    speechText = speechText + result.getItems().get(index).get("procedureName").getS() + " und ";
                }
            }
            return speechText.substring(0, speechText.length()-4);

        }
        catch (Exception e) {
            System.err.println("Ablaufnamen konnten nicht gelesen werden");
            System.err.println(e.getMessage());
            return NO_PROCEDURE_AVAILABLE;
        }
    }

    /**
     * Returns the matching procedure json file if the user is allowed to see it
     * @param procedureName the procedure to select
     * @param userId to authenticate the user
     * @return the matching procedure json file or null if it does not exist
     */
    public static String getAblaufFile(String procedureName, String userId) {
        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1)
                    .build();
            DynamoDB db = new DynamoDB(client);

            ScanRequest scanRequest = new ScanRequest()
                    .withTableName("Procedures_Data");
            ScanResult result = client.scan(scanRequest);

            for(int index = 0; index < result.getItems().size(); index++){
                if((result.getItems().get(index).get("userID").getS().equals(userId)
                        || result.getItems().get(index).get("userID").getS().equals("Template"))
                        && result.getItems().get(index).get("procedureName").getS().toLowerCase().equals(procedureName.toLowerCase())) {
                    return result.getItems().get(index).get("jsonFile").getS();
                }
            }
            return null;
        }
        catch (Exception e) {
            System.err.println("Unable to read entry!");
            System.err.println("procedure: " + procedureName);
            System.err.println(e.getMessage());
            return null;
        }
    }



}
