/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package ablaeufe.handlers;

import ablaeufe.LogEndStates;
import ablaeufe.Speicher;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ablaeufe.PhrasesAndConstants.*;

public class CancelandStopIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.StopIntent").or(intentName("AMAZON.CancelIntent")));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String,Object> session = input.getAttributesManager().getSessionAttributes();
        if (session.get(ABLAUF_KEY) != null) {
            Speicher.saveLogEntry((String) session.get(USER_ACCOUNT_KEY), (String) session.get(ABLAUF_KEY), (int) session.get(START_TIME_KEY), LogEndStates.CANCELED);
        }
        return input.getResponseBuilder()
                .withSpeech(CANCEL_AND_STOP)
                .withSimpleCard(CARD_TITLE, CANCEL_AND_STOP)
                .withShouldEndSession(true)
                .build();
    }
}
