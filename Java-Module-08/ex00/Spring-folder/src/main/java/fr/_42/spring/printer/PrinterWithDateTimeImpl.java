package fr._42.spring.printer;

import fr._42.spring.renderer.Renderer;

import java.time.LocalDateTime;

public class PrinterWithDateTimeImpl implements Printer {
    private final Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void print(String message) {
        String dateTime = LocalDateTime.now().toString();
        String messageWithDateTime = "[" + dateTime + "] " + message;
        renderer.render(messageWithDateTime);
    }
}
