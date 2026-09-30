package fr._42.spring.printer;

import fr._42.spring.renderer.Renderer;

public class PrinterWithPrefixImpl implements Printer {
    private final Renderer renderer;
    private String PREFIX = "[PREFIX] ";

    public PrinterWithPrefixImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setPrefix(String prefix) {
        PREFIX = prefix;
    }

    @Override
    public void print(String message) {
        renderer.render(this.PREFIX + " " + message);
    }
}
