package ablaeufe.handlers;

import ablaeufe.LogEndStates;
import ablaeufe.Speicher;
import ablaeufe.fachklassen.Ablauf;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;

import com.amazon.ask.model.Response;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

import static ablaeufe.PhrasesAndConstants.*;


public class NextStepIntentHandler implements RequestHandler{

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("FinishedIntent")
                .or(intentName("NotFinishedIntent"))
                .or(intentName("AMAZON.PreviousIntent"))
                .or(intentName("AMAZON.NextIntent"))
                .or(intentName("AMAZON.YesIntent"))
                .or(intentName("AMAZON.NoIntent"))
                .or(intentName("StartProcedureIntent"))
                .or(intentName("AMAZON.RepeatIntent")));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        //Ablauf initialisieren
        Ablauf ablauf = new Ablauf(input);
        if(input.matches(intentName("StartProcedureIntent"))) {
            if (!input.getAttributesManager().getSessionAttributes().containsKey(USER_ACCOUNT_KEY)) {
                ablauf.setOAuthToken();
            }
            ablauf.setAblauf();
            ablauf.setSchrittNr(1);
        }

        //Wenn der aktuelle Schritt wiederholt werden soll
        int numberOfRepeats = 0;
        if (input.matches(intentName("NotFinishedIntent").or(intentName("AMAZON.RepeatIntent")).or(intentName("AMAZON.PreviousIntent"))
                .or(intentName("AMAZON.NoIntent")))) {

            numberOfRepeats++;
            String inactivityMessage = "";
            if (numberOfRepeats % 2 == 0 && ablauf.getAblaufFile() != null) {
                inactivityMessage = INACTIVITY_1 + ablauf.getAblaufName() + INACTIVITY_2;
            }
            String speechText = ablauf.repeatStep();
            return input.getResponseBuilder()
                    .withSimpleCard(CARD_TITLE, speechText)
                    .withSpeech(speechText + "<audio src='https://proceduresmusic.s3-eu-west-1.amazonaws.com/swedenskillaudiotest.mp3'/>")
                    .withReprompt(inactivityMessage + speechText + ANWEISUNG_NEXT_STEP)
                    .withShouldEndSession(false)
                    .build();
        }

        String speechText = ablauf.getNextStep();
        String repromptText = ablauf.getReprompt();

        //Wenn kein weiterer Schritt existiert, dann ist der Ablauf fertig
        if(speechText == null) {
            String finishedText = ablauf.getFinishedText();
            Map<String,Object> session = input.getAttributesManager().getSessionAttributes();
            Speicher.saveLogEntry((String) session.get(USER_ACCOUNT_KEY), (String) session.get(ABLAUF_KEY), (int) session.get(START_TIME_KEY), LogEndStates.COMPLETED);
            return input.getResponseBuilder()
                    .withSpeech(finishedText + GOOD_BYE)
                    .withSimpleCard(CARD_TITLE,finishedText + GOOD_BYE)
                    .withShouldEndSession(true) // Abgeschlossener Ablauf beendet Skill-Session
                    .build();
        }

        //Den nächsten Schritt ausgeben
        return input.getResponseBuilder()
                .withSpeech(speechText + "<audio src='https://proceduresmusic.s3-eu-west-1.amazonaws.com/swedenskillaudiotest.mp3'/>")
                /*
                    Der Zugang zur Audiodatei in der S3 Bucket wurde angepasst.
                    Es geht, wenn die Datei für jeden zum Lesen erreichbar ist (Everyone has reading access). Für einen Prototyp reicht es zwar,
                    doch wäre dies der Sicherheit wegen nicht so optimal.
                    .
                */
                .withSimpleCard(CARD_TITLE, speechText)
                .withReprompt(repromptText)
                .withShouldEndSession(false)
                .build();
    }

}