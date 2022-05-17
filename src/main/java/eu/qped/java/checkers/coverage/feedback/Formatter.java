package eu.qped.java.checkers.coverage.feedback;

import eu.qped.framework.Feedback;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Objects;

public class Formatter {

    public static String[] format(String format, FormatterFacade summary) {

        File f = null;
        try {
            String temp = Paths.get("./src/main/resources/coverage/template/index.vm").toString();
            if (Objects.nonNull(format) && ! format.isBlank() ) {
                f = Paths.get("./target/classes/coverage/template/custom.vm").toFile(); // TODO::NotOK?
                FileWriter w = new FileWriter(f);
                w.write(format);
                w.close();
                temp = f.getPath();
            }
            Template template = Velocity.getTemplate(temp);
            VelocityContext context = new VelocityContext();
            context.put("summary", summary);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);

            return new String[] {writer.toString()};
        } catch (Exception e) {


            e.printStackTrace();


            return summary.feedbacks().stream().map(Feedback::getBody).toArray(String[]::new);
        } finally {
            if (Objects.nonNull(f)) {
                f.delete();
            }
        }
    }

}
