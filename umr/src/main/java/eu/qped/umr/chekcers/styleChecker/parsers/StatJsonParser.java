package eu.qped.umr.chekcers.styleChecker.parsers;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import eu.qped.umr.helpers.Logger;

import eu.qped.umr.model.StyleStatsModel;
import eu.qped.umr.model.StyleViolation;
import eu.qped.umr.qf.QfUser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class StatJsonParser implements Parser {


    private final StyleStatsModel styleStatsModel;

    public StatJsonParser (StyleStatsModel styleStatsModel){
        this.styleStatsModel = styleStatsModel;
    }


    @Override
    public Object parse() {
        JsonObject data = new JsonObject();

        JsonArray allUsers = new JsonArray();

        JsonObject user = new JsonObject();
        JsonArray userInfo = new JsonArray();
        JsonObject info = new JsonObject();

        info.addProperty("vioWeights" , styleStatsModel.getVioWeights() );
        info.addProperty("improvementRate",styleStatsModel.getImprovementRate());


        JsonObject violationsInfo =new JsonObject();

        JsonArray violations = new JsonArray();
        ArrayList<StyleViolation> styleViolations = styleStatsModel.getMadeViolations();
        for (StyleViolation styleViolation : styleViolations) {
            JsonObject vio = new JsonObject();
            vio.addProperty("rule", styleViolation.getRule());
            vio.addProperty("line", styleViolation.getLine());
            vio.addProperty("priority", styleViolation.getPriority());

            violations.add(vio);
        }

        violationsInfo.add("violations", violations);

        userInfo.add(info);
        userInfo.add(violationsInfo);

        user.add(styleStatsModel.getUser().getId() , userInfo);


        allUsers.add(user);

        data.add("users" , allUsers);

        try(OutputStream outputStream = Files.newOutputStream(Paths.get("StyleStats.json"))) {
            outputStream.write(data.toString().getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e){
            Logger.getInstance().log(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {

        ArrayList<StyleViolation> styleViolations = new ArrayList<>();
        StyleViolation styleViolation = new StyleViolation("mockRule" , "mockDesc" , 1 , 1);
        styleViolations.add(styleViolation);

        QfUser user = new QfUser();
        user.setFirstName("basel");
        user.setLastName("aktaa");
        user.setId("1233");

        StyleStatsModel styleStatsModel = new StyleStatsModel(user,styleViolations,10, 10 );

        StatJsonParser statJsonParser = new StatJsonParser(styleStatsModel);

        statJsonParser.parse();
    }
}
