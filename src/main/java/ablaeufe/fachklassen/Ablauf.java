package ablaeufe.fachklassen;

import ablaeufe.Speicher;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import org.json.JSONObject;

import static ablaeufe.PhrasesAndConstants.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Ablauf {
    private HandlerInput input;
    public Ablauf(HandlerInput input) {
        this.input = input;
    }

    /**
     * Set field input.
     * @param input Set field input.
     */
    public void setInput(HandlerInput input) {
        this.input = input;
    }

    /**
     * Get field input.
     * @return field input.
     */
    public HandlerInput getInput(){
        return input;
    }

    /**
     * Liefert den nächsten Schritt im Ablauf.
     * Bevor getnextStep() das erste mal aufgerufen wird, sollte mit setSchritt() die Session Variable
     * "schrittNr" gesetzt und mit setAblauf() der uebergebene slot als "ablauf" in den Session Variablen
     * gespeichert werden.
     * @return the text to be spoken in the next step
     */
    public String getNextStep() {
        return getInstruction(getAblaufFile(), incrementSchrittNr());
    }

    /**
     * Set Session Variable schrittNr.
     * @param schrittNr Set field input.
     */
    public void setSchrittNr(int schrittNr) {
        Map<String,Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        sessionMap.put(SCHRITTNR_KEY, schrittNr);
        input.getAttributesManager().setSessionAttributes(sessionMap);
    }

    /**
     * Inkrementiert die Session Variable schrittNr.
     * Wenn schrittNr noch nicht definert ist, tut die Methode nichts!
     * @return den Wert von schrittNr vor dem inkrementieren, oder PhrasesandConstants.SCHRITTNR_NOT_EXISTS
     * wenn schrittNr nicht definiert war.
     */
    public int incrementSchrittNr() {
        Map<String,Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        Integer alteSchrittNr = (Integer) sessionMap.get(SCHRITTNR_KEY);
        if(alteSchrittNr == null) {
            return SCHRITTNR_NOT_EXISTS;
        }
        sessionMap.put(SCHRITTNR_KEY, alteSchrittNr+1);
        input.getAttributesManager().setSessionAttributes(sessionMap);
        return alteSchrittNr;
    }

    /**
     * Get Session Variable schrittNr. Liefert PhrasesandConstants.SCHRITTNR_NOT_EXISTS,
     * wenn Session Variable nicht vorhanden.
     * @return die Session Variable schrittNr
     */
    public int getSchrittNr() {
        Map<String,Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        Integer schrittNr = (Integer) sessionMap.get(SCHRITTNR_KEY);
        if(schrittNr == null) {
            return SCHRITTNR_NOT_EXISTS;
        }
        return schrittNr;
    }

    /**
     * Gibt den in der Session Variable PhrasesAndConstants.ABLAUF_FILE_KEY gespeicherten Ablauf zurück.
     * @return Ablauf als String im Json Format, oder PhrasesandConstants.KEIN_ABLAUF, wenn Session
     * Variable null oder nicht vorhanden
     */
    public String getAblaufFile() {
        Map<String,Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        String ablaufFile = (String) sessionMap.get(ABLAUF_FILE_KEY);
        if(ablaufFile == null) {
            return KEIN_ABLAUF;
        }
        return ablaufFile;
    }

    /**
     * Holt den im Slot angefragten Ablauf aus DynomoDB und speichert ihn als Session Variable
     * PhrasesandConstants.ABLAUF_FILE_KEY.
     * Speichert die Ablauf Startzeit (aktuelle Systemzeit) als "startTime".
     */
    public void setAblauf() {
        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        String procedureName = intentRequest.getIntent().getSlots()
                    .get(ABLAUF_SLOT).getValue();
        if(procedureName != null) {
            Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
            sessionMap.put(ABLAUF_FILE_KEY, Speicher.getAblaufFile(procedureName, getUserID()));
            sessionMap.put(ABLAUF_KEY, getAblaufName());
            sessionMap.put(START_TIME_KEY, (int) (System.currentTimeMillis() / 1000));
            input.getAttributesManager().setSessionAttributes(sessionMap);
        }
    }

    /**
     * Extrahiert den zu sprechenden Text für den Schritt schrittNr aus dem ablaufFile.
     * @param ablaufFile die Json Ablaufdatei als String
     * @param schrittNr die schrittNr des gewünschten zu sprechenden Textes
     * @return von Alexa zu sprechender Text
     */
    public String getInstruction(String ablaufFile, int schrittNr) {
        try {
            return new JSONObject(ablaufFile).getJSONArray("steps")
                    .getJSONObject(schrittNr - 1).getString("instructionText");
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Extrahiert den zu sprechenden Reprompt für den Schritt schrittNr aus dem ablaufFile.
     * @return von Alexa zu sprechender Text
     */
    public String getReprompt() {
        int schrittNr = getSchrittNr();
        String ablaufFile = getAblaufFile();
        try {
            return new JSONObject(ablaufFile).getJSONArray("steps")
                    .getJSONObject(schrittNr - 1).getString("confirmText");
        }
        catch (Exception e) {
            return ANWEISUNG_NEXT_STEP;
        }
    }

    /**
     * Extrahiert den zu sprechenden FinishedText für den Schritt schrittNr aus dem ablaufFile.
     * @return von Alexa zu sprechender Text
     */
    public String getFinishedText() {
        int schrittNr = getSchrittNr();
        String ablaufFile = getAblaufFile();
        try {
            return new JSONObject(ablaufFile).getJSONArray("steps")
                    .getJSONObject(schrittNr -2).getString("finishedText");
        }
        catch (Exception e) {
            return ANWEISUNG_ABLAUF_FERTIG;
        }
    }

    /**
     * Liefert den aktuellen Schritt im Ablauf nochmal.
     * Bevor repaeatStep() das erste mal aufgerufen wird, sollte mit setSchritt() die Session Variable
     * "schrittNr" gesetzt und mit setAblauf() der uebergebene slot als "ablauf" in den Session Variablen
     * gespeichert werden.
     * @return the text to be spoken in the next step
     */
    public String repeatStep() {
        return getInstruction(getAblaufFile(),getSchrittNr()-1);
    }

    /**
     * @return den Ablaufnamen des aktuellen Ablaufs.
     */
    public String getAblaufName() {
        return new JSONObject(getAblaufFile()).getString("procedureName");
    }

    /**
     * @return von Alexa zu sprechender Hilfetext
     */
    public String getHelpText() {
        try {
            return new JSONObject(getAblaufFile()).getJSONArray("steps")
                    .getJSONObject(getSchrittNr()-2).getString("helpText");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Saves the OAuthToken as session variable USER_ACCOUNT_KEY
     */
    public void setOAuthToken() {
        String token = input.getRequestEnvelope().getContext().getSystem().getUser().getAccessToken();
        String urlString = "https://api.amazon.com/user/profile?access_token=" + token;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                response.append(currentLine);
            }
            in.close();

            String id =  new JSONObject(response.toString()).getString("user_id");
            String name = new JSONObject(response.toString()).getString("name");

            if(id != null) {
                Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
                sessionMap.put(USER_ACCOUNT_KEY, id);
                sessionMap.put(USER_NAME_KEY, name);
                input.getAttributesManager().setSessionAttributes(sessionMap);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Returns the session Variable USER_NAME_KEY
     * @return the session Variable USER_NAME_KEY
     */
    public String getUserName() {
        Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        return (String) sessionMap.get(USER_NAME_KEY);
    }

    /**
     * Returns the session Variable USER_ACCOUNT_KEY
     * @return the session variable USER_ACCOUNT_KEY
     */
    public String getUserID() {
        Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        return (String) sessionMap.get(USER_ACCOUNT_KEY);
    }



}
