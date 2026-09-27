package fr._42.annotations.annotations;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.lang.model.element.Element;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes({"fr._42.annotations.annotations.HtmlForm"})
public class HtmlProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println(">>> HTML PROCESSOR IS RUNNING");
        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            System.out.println(">>> Found form: " + element.getSimpleName());
            generateHtml(element);
        }

        return true;
    }

    public void generateHtml(Element element) {
        HtmlForm htmlForm = element.getAnnotation(HtmlForm.class);
        String fileName = htmlForm.fileName();
        String action = htmlForm.action();
        String method = htmlForm.method();

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<form action = \"")
                .append(action)
                .append("\" method = \"")
                .append(method)
                .append("\">\n");
        for (Element field : element.getEnclosedElements()) {
            HtmlInput htmlInput = field.getAnnotation(HtmlInput.class);

            if (htmlInput != null) {
                String inputType = htmlInput.type();
                String inputName = htmlInput.name();
                String inputPlaceholder = htmlInput.placeholder();

                htmlContent.append("<input type = \"")
                        .append(inputType)
                        .append("\" name = \"")
                        .append(inputName)
                        .append("\" placeholder = \"")
                        .append(inputPlaceholder)
                        .append("\">\n");
            }
        }
        htmlContent.append("<input type = \"submit\" value = \"Send\">\n</form>\n");

        try {
            FileObject file = processingEnv
                .getFiler()
                .createResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    fileName
                );

            try (Writer writer = file.openWriter()) {
                writer.write(htmlContent.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
