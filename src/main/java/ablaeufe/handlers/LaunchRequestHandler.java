
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


import ablaeufe.Speicher;
import ablaeufe.fachklassen.Ablauf;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import com.amazon.ask.model.dialog.DynamicEntitiesDirective;
import com.amazon.ask.model.er.dynamic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;
import static ablaeufe.PhrasesAndConstants.*;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {


        //pruefen, ob account linking aktiviert
        if(input.getRequestEnvelope().getContext().getSystem().getUser().getAccessToken() == null) {
            return input.getResponseBuilder()
                    .withSpeech(AUTHENTICATE_PROMPT)
                    .withLinkAccountCard()
                    .withShouldEndSession(true) // Ablauf beenden
                    .build();
        }

        Ablauf ablauf = new Ablauf(input);
        ablauf.setOAuthToken();
        String speechText = Speicher.getAllProcedures(ablauf.getUserID());


        //Wenn Procedures verfügbar sind
        List<Entity> entities = new ArrayList<>();
        if(!speechText.equals(NO_PROCEDURE_AVAILABLE)) {
            //Slots erweitern
            String[] values = speechText.split(" und ");
            for(String value: values) {
                Entity entity = Entity.builder()
                        .withName(EntityValueAndSynonyms.builder()
                                .withValue(value)
                                .build())
                        .build();
                entities.add(entity);
            }
        }

        DynamicEntitiesDirective replaceEntityDirective = DynamicEntitiesDirective
                .builder()
                .addTypesItem(EntityListItem.builder()
                        .withName("Procedure")
                        .withValues(entities)
                        .build())
                .withUpdateBehavior(UpdateBehavior.REPLACE)
                .build();

        return input.getResponseBuilder()
                .withSpeech(HALLO + ablauf.getUserName() + ANWEISUNG_LAUNCH_REQUEST + speechText)
                .withSimpleCard(CARD_TITLE, "")
                .withReprompt(ANWEISUNG_LAUNCH_REPROMPT)

                .addDirective(replaceEntityDirective)
                .build();
    }

}