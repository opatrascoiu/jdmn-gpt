package org.jdmn.chat;

import dev.langchain4j.service.UserMessage;
import org.jdmn.generated.mortgage.type.Person;

public interface PersonExtractor {
    @UserMessage("Extract information about a person from {{it}}. When income is null, it is set to 0.")
    Person extractPersonFrom(String text);
}

