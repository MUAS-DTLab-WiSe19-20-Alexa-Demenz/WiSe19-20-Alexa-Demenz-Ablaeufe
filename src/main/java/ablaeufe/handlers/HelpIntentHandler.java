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

import ablaeufe.fachklassen.Ablauf;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ablaeufe.PhrasesAndConstants.*;

public class HelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Ablauf ablauf = new Ablauf(input);
        String helpText = ablauf.getHelpText();
        if (helpText == null){
            //Annahme, dass kein Schritt vorliegt
            return input.getResponseBuilder()
                    .withSimpleCard(CARD_TITLE,HELP)
                    .withSpeech(HELP)
                    .withReprompt(HELP_REPROMPT)
                    .withShouldEndSession(false)
                    .build();
        }
        //Annahme, dass ein Schritt vorliegt
        
        return input.getResponseBuilder()
                .withSimpleCard(CARD_TITLE, helpText)
                //.withSpeech(helpText)
                .withSpeech(helpText + "<audio src='https://proceduresmusic.s3-eu-west-1.amazonaws.com/swedenskillaudiotest.mp3'/>")
                //Hier sollte Alexa wie üblich warten und dann nachfragen
                .withShouldEndSession(false)
                .build();
    }
}
