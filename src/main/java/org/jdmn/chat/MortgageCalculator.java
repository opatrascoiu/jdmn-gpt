package org.jdmn.chat;

import com.gs.dmn.runtime.ExecutionContext;
import com.gs.dmn.runtime.ExecutionContextBuilder;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.V;
import org.jdmn.generated.mortgage.type.Person;

import java.util.HashMap;
import java.util.Map;

public class MortgageCalculator {

    private final Map<String, Person> personsRegistry = new HashMap<>();
    private final org.jdmn.generated.mortgage.MortgageCalculator mortgageCalculator = new org.jdmn.generated.mortgage.MortgageCalculator();

    public MortgageCalculator() {
    }

    @Tool("Grant mortgage to {{name}}")
    public String grantMortgage(@V("name") String name) {
        Person person = personsRegistry.get(name);

        if (person == null) {
            return "Unknown person";
        }

        ExecutionContext executionContext = ExecutionContextBuilder.executionContext().build();
        Boolean outcome = mortgageCalculator.apply(person, executionContext);
        if (outcome) {
            return "Yes, mortgage can be granted";
        }
        return "Mortgage cannot be granted cannot be granted because " + executionContext.getAnnotations();
    }

    public void register(Person person) {
        personsRegistry.put(person.getFullName(), person);
    }
}
