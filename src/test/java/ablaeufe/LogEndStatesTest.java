package ablaeufe;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;


public class LogEndStatesTest {

    @Test
    public void logEndStatesTest() {
        assertNotNull(LogEndStates.CANCELED);
        assertNotNull(LogEndStates.ERROR);
        assertNotNull(LogEndStates.COMPLETED);
        assertNotNull(LogEndStates.TIMEOUT);
    }
}
