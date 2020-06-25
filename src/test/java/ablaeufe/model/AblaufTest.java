package ablaeufe.model;

import ablaeufe.PhrasesAndConstants;
import ablaeufe.fachklassen.Ablauf;
import ablaeufe.handlers.TestUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static ablaeufe.PhrasesAndConstants.*;
import static ablaeufe.handlers.TestUtil.mockHandlerInput;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class AblaufTest {

    @Test
    public void testSetGetSchrittNr() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(KOCHEN_VALUE,
                new HashMap<String, Object>() {
                }, new HashMap<String, Object>(), new HashMap<String, Object>()));
        ablauf.setSchrittNr(7);
        assertEquals(7, ablauf.getSchrittNr());
    }

    @Test
    public void testIncrementSchritt() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(KOCHEN_VALUE,
                new HashMap<String, Object>() {
                }, new HashMap<String, Object>(), new HashMap<String, Object>()));
        ablauf.setSchrittNr(2);

        assertEquals(2, ablauf.incrementSchrittNr());
        assertEquals(3, ablauf.getSchrittNr());
    }

    @Test
    public void testGetAblaufFileKochen() {

        Map<String,Object> sessionMap = new HashMap<>();
        sessionMap.put(ABLAUF_FILE_KEY, KOCHEN_ABLAUF);

        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(KOCHEN_VALUE,
                sessionMap, new HashMap<String, Object>(), new HashMap<String, Object>()));

        assertEquals(KOCHEN_ABLAUF, ablauf.getAblaufFile());
    }

    @Test
    public void testGetAblaufFileRausgehen() {

        Map<String,Object> sessionMap = new HashMap<>();
        sessionMap.put(ABLAUF_FILE_KEY, RAUSGEHEN_ABLAUF);

        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(RAUSGEHEN_VALUE,
                sessionMap, new HashMap<String, Object>(), new HashMap<String, Object>()));

        assertEquals(RAUSGEHEN_ABLAUF, ablauf.getAblaufFile());
    }

    @Test
    public void testGetAblaufFileNull() {
        Map<String,Object> sessionMap = new HashMap<>();
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(KOCHEN_VALUE,
                sessionMap, new HashMap<String, Object>(), new HashMap<String, Object>()));

        assertEquals(KEIN_ABLAUF, ablauf.getAblaufFile());
    }

    //DIE AUSKOMMENTIERTEN TESTS BENÖTIGEN AWS CREDENTIALS, UM AUF DYNAMODB ZUZUGREIFEN.
    //SIE LAUFEN AUF MEINEM RECHNER, ABER NICHT AUF JENKINS.
/*    @Test
    public void testSetAblaufRausgehen() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(RAUSGEHEN_VALUE,
                new HashMap<>(), new HashMap<String, Object>(), new HashMap<String, Object>()));
        ablauf.setAblauf();
        assertEquals(RAUSGEHEN_ABLAUF, ablauf.getAblaufFile());
    }

    @Test
    public void testSetAblaufKochen() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(KOCHEN_VALUE,
                new HashMap<>(), new HashMap<String, Object>(), new HashMap<String, Object>()));
        ablauf.setAblauf();
        assertEquals(KOCHEN_ABLAUF, ablauf.getAblaufFile());
    }

    @Test
    public void testSetAblaufNull() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(null,
                new HashMap<>(), new HashMap<String, Object>(), new HashMap<String, Object>()));
        ablauf.setAblauf();
        assertEquals(KEIN_ABLAUF, ablauf.getAblaufFile());
    }*/


    //Testen eines zu kleinen Index
    @Test
    public void testGetInstructionSchritt0() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(RAUSGEHEN_VALUE,
                new HashMap<>(), new HashMap<String, Object>(), new HashMap<String, Object>()));
        String res = ablauf.getInstruction(RAUSGEHEN_ABLAUF, 0);
        assertNull(res);
    }

    //Testen eines zu grossen Index
    @Test
    public void testGetInstructionSchritt5() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(RAUSGEHEN_VALUE,
                new HashMap<>(), new HashMap<String, Object>(), new HashMap<String, Object>()));
        String res = ablauf.getInstruction(RAUSGEHEN_ABLAUF, 5);
        assertNull(res);
    }

    @Test
    public void testGetInstruction() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(RAUSGEHEN_VALUE,
                new HashMap<>(), new HashMap<String, Object>(), new HashMap<String, Object>()));
        String res = ablauf.getInstruction(RAUSGEHEN_ABLAUF, 2);
        String correct = "Nehmen Sie die Schuhe aus der grünen Kommode und ziehen Sie sie an.";
        assertEquals(correct, res);
    }

    //DIE AUSKOMMENTIERTEN TESTS BENÖTIGEN AWS CREDENTIALS, UM AUF DYNAMODB ZUZUGREIFEN.
    //SIE LAUFEN AUF MEINEM RECHNER, ABER NICHT AUF JENKINS.
    /*@Test
    public void testGetNextStep() {
        Ablauf ablauf = new Ablauf(TestUtil.mockHandlerInput(KOCHEN_VALUE,
                new HashMap<>(), new HashMap<String, Object>(), new HashMap<String, Object>()));
        ablauf.setAblauf();
        ablauf.setSchrittNr(1);
        String res = ablauf.getNextStep();
        String correct = "Bitte suchen Sie die Nudeln im blauen Regal links von der Spüle.";
        assertEquals(correct, res);
        assertEquals(2, ablauf.getSchrittNr());
    }*/
}
