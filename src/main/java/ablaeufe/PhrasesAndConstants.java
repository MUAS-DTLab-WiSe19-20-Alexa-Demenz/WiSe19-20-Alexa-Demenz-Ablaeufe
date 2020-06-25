package ablaeufe;

public class PhrasesAndConstants {

    private PhrasesAndConstants() {
        throw new IllegalStateException("Utility class");
    }

    //SSML TEST STRINGS
    public static final String SSML_TEST_STRING = "<speak><say-as interpret-as=\"interjection\">obacht</say-as>.<amazon:effect name='whispered'>Hey.</amazon:effect>SSML kann viel mehr, als du denkst...<audio src=\"soundbank://soundlibrary/alarms/beeps_and_bloops/woosh_01\"/></speak>";
    public static final String SSML_LANG_TEST_STRING = "<voice name='Takumi'><lang xml:lang='ja-JP'>あれくささんなきれいですね。</lang></voice>";

    //COMMON PROMPTS
    public static final String CARD_TITLE = "Abläufe";
    public static final String ALLPROCEDURES = "Mit mir können Sie folgende Abläufe durchgehen: Rausgehen, Kochen und Bettfertig machen.";
    public static final String ALLPROCEDURES_REPROMT = "Was möchten Sie tun?";
    public static final String STARTPROCEDURE_REPROMT = "Sind Sie bereit?";
    public static final String WELCOME = "Hallo. Ich bin die Ablaufhilfe";
    public static final String HELP = "Sag den Namen eines Ablaufs den du starten willst, oder 'Welche abläufe gibt es?' damit alle verfügbaren Abläufe aufgezählt werden.";
    public static final String HELP_REPROMPT = "Sag mir bitte, welchen Ablauf du starten willst";
    public static final String CANCEL_AND_STOP = "Auf Wiedersehen.";
    public static final String FALLBACK = "Tut mir leid, das hab ich nicht verstanden. Sage einfach Hilfe.";

    public static final String HALLO = "Hallo ";
    public static final String GOOD_BYE =" Auf Wiedersehen.";
    public static final String ANWEISUNG_NEXT_STEP="Sind Sie fertig? Antworten Sie mit fertig, wenn sie den Schritt abgeschlossen haben";
    public static final String ANWEISUNG_ABLAUF_FERTIG="Damit ist der Ablauf abgeschlossen. Gut gemacht und auf Wiedersehen.";
    public static final String ANWEISUNG_LAUNCH_REQUEST=". Willkommen bei Ablauf-Hilfe. Sage zum Beispiel ich will kochen um einen Ablauf zu starten.";
    public static final String ANWEISUNG_LAUNCH_REPROMPT="Ich würde ja gerne Kochen. Sage ich will kochen, um den Ablauf zu starten";
    public static final String INACTIVITY_1 = "Sie wollten doch ";
    public static final String INACTIVITY_2 = ". Ich helfe Ihnen gerne auf die Sprünge:";
    public static final String AUTHENTICATE_PROMPT = "Bitte authentifizieren sie sich mit ihren Amazon Login Daten in der Alexa App, um den Skill zu nutzen";
    public static final String AVAILABLE_PROCEDURES = " Die folgenden Abläufe sind vorhanden: ";
    public static final String NO_REPROMPT = "Es ist kein Reprompt Text vorhanden";
    public static final String NO_PROCEDURE_AVAILABLE = "Es sind keine Abläufe verfügbar.";


    //Keys für die Session Variablen
    public static final String USER_NAME_KEY = "name";
    public static final String USER_ACCOUNT_KEY = "userId";
    public static final String ABLAUF_KEY = "ablaufName";
    public static final String ABLAUF_FILE_KEY = "ablaufFile";
    public static final String SCHRITTNR_KEY = "schrittNr";
    public static final String START_TIME_KEY = "startTime";

    public static final int SCHRITTNR_NOT_EXISTS = -1;
    //Dynamo
    public static final String PROCEDURE_NAME_KEY = "procedureName";
    public static final String PROCEDURES_FILES_TABLE = "Procedures_Files";
    public static final String PROCEDURES_MUSIK_TABLE = "Procedures_Musik";
    public static final String MUSIC_NAME_KEY = "musikName";
    public static final String PROCEDURE_USER_ID_KEY = "userID";

    //In diesem Slot wird der Name des gewünschten Ablaufs übergeben
    public static final String ABLAUF_SLOT = "procedure";

    //Alle Namen der verfügbaren Abläufe
    public static final String KOCHEN_VALUE = "kochen";
    public static final String RAUSGEHEN_VALUE = "rausgehen";

    //Ab hier alle Abläufe
    public static final String KEIN_ABLAUF="Kein Ablauf ausgewählt";



    //DAS PROGRAMM GREIFT NICHT MEHR AUF DIESE ABLÄUFE ZU!
    //ALLE ABLÄUFE WERDEN AUS DYNAMODB GELADEN
    //DIE ABLÄUFE STEHEN NUR NOCH HIER IM CODE, DAMIT DIE TESTS DARAUF ZUGREIFEN KÖNNEN
    public static final String KOCHEN_ABLAUF = "{\n" +
            "    \"procedureName\":\"Kochen\",\n" +
            "    \"triggerParameters\":[\n" +
            "        {\n" +
            "            \"DAYS\":\"EVERYDAY\",\n" +
            "            \"TIME\":\"0700\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"steps\":[\n" +
            "        {\n" +
            "            \"step\":1,\n" +
            "            \"instructionText\":\"Bitte suchen Sie die Nudeln im blauen Regal links von der Spüle.\",\n" +
            "            \"helpText\":\"Das Regal ist in der Küche auf der rechten Seite. Die Nudeln sind in einer blauen Verpackung unter der Obstschale, denn: 'Wie du siehst es ist Obst im Haus.'.\""+
            "        },\n" +
            "        {\n" +
            "           \"step\":2,\n" +
            "           \"instructionText\":\"Nehmen Sie einen mittelgrossen Topf aus dem Schrank.\",\n" +
            "           \"helpText\":\"Der Schrank ist auf der linken Seite. Der Topf hat die Aufschrift 'Nudeltopf'.\""+
            "        },\n" +
            "        {\n" +
            "           \"step\":3,\n" +
            "           \"instructionText\":\"Fuellen Sie den Topf mit Wasser und salzen Sie das Wasser.\",\n" +
            "           \"helpText\":\"Bitte nehmen Sie ca. 1 TL Salz. Bitte nicht übersalzen.\""+
            "        }\n" +
            "    ]\n" +
            "}";


    public static final String RAUSGEHEN_ABLAUF = "{\n" +
            "    \"procedureName\":\"rausgehen\",\n" +
            "    \"triggerParameters\":[\n" +
            "        {\n" +
            "            \"DAYS\":\"EVERYDAY\",\n" +
            "            \"TIME\":\"0700\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"steps\":[\n" +
            "        {\n" +
            "            \"step\":1,\n" +
            "            \"instructionText\":\"Bitte ziehen Sie die Regenjacke an.\",\n" +
            "            \"helpText\":\"Ihre Regenjacke ist blau und grün.\""+
            "        },\n" +
            "        {\n" +
            "           \"step\":2,\n" +
            "           \"instructionText\":\"Nehmen Sie die Schuhe aus der grünen Kommode und ziehen Sie sie an.\",\n" +
            "           \"helpText\":\"Ihre Schuhe sind schwarz/weiß.\""+
            "        },\n" +
            "        {\n" +
            "           \"step\":3,\n" +
            "           \"instructionText\":\"Nehmen Sie den Schlüssel vom Haken an der Tür und stecken ihn ein.\",\n" +
            "           \"helpText\":\"Der Schlüssel ist blau...\""+
            "        }\n" +
            "    ]\n" +
            "}";
}
