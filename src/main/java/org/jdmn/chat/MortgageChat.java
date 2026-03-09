package org.jdmn.chat;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.jdmn.generated.mortgage.type.Person;

import static java.time.Duration.ofSeconds;

public class MortgageChat {

    private final ChatLanguageModel model;

    private final PersonExtractor extractor;

    private final MortgageCalculator agent = new MortgageCalculator();

    private final Assistant assistant;

    public MortgageChat(String openAiApiKey) {
        model = OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .timeout(ofSeconds(60))
                .build();

        extractor = AiServices.create(PersonExtractor.class, model);

        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(agent)
                .build();
    }

    public String chat(String text) {
        return text.endsWith("?") ? assistant.chat(text) : extractPerson(text);
    }

    private String extractPerson(String text) {
        Person person = extractor.extractPersonFrom(text);
        agent.register(person);
        return person.toString();
    }
}
