package ablaeufe;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.junit.Test;
import ablaeufe.PhrasesAndConstants;
import ablaeufe.fachklassen.Ablauf;
import ablaeufe.handlers.TestUtil;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static ablaeufe.PhrasesAndConstants.*;
import static ablaeufe.handlers.TestUtil.mockHandlerInput;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class SpeicherTest {
Speicher speicher = TestUtil.getSpeicher();
    //DIE AUSKOMMENTIERTEN TESTS BENÖTIGEN AWS CREDENTIALS, UM AUF DYNAMODB ZUZUGREIFEN.
    //SIE LAUFEN AUF MEINEM RECHNER, ABER NICHT AUF JENKINS.
/*    @Test
    public void testGetAblaufFileKochen() {
        assertEquals(KOCHEN_ABLAUF,
                Speicher.getAblaufFile(KOCHEN_VALUE));

    }

    @Test
    public void testGetAblaufFileRausgehen() {
        assertEquals(RAUSGEHEN_ABLAUF,
                Speicher.getAblaufFile(RAUSGEHEN_VALUE));

    }

    @Test
    public void testGetAblaufFileNotExist() {
        assertNull(Speicher.getAblaufFile("wandern"));

    }

    @Test
    public void saveAblaufFile() {
        Speicher.saveAblaufFile(KOCHEN_ABLAUF, KOCHEN_VALUE);
        String res = Speicher.getAblaufFile(KOCHEN_VALUE);
        assertEquals(KOCHEN_ABLAUF, res);
    }*/


}
