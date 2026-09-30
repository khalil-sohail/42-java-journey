package fr._42.spring.preprocessor;

public class PreProcessorToUpperImpl implements PreProcessor {
    @Override 
    public String process(String message) {
        return message.toUpperCase();
    }
}
