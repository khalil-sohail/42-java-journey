package fr._42.spring.preprocessor;

public class PreProcessorToLowerImpl implements PreProcessor {
    @Override
    public String process(String message) {
        return message.toLowerCase();
    }
}
