package eu.qped.umr.checkers.autoChecker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataProvider {

    private final List<String> data = new ArrayList<>();

    private DataProvider (){
    }

    public static DataProvider createDataProvider(){
        return new DataProvider();
    }



    public void provide () {
        try (Stream<Path> paths = Files.walk(Paths.get("StudentenData"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(x -> read(x.toString()));
        }
        catch (IOException e){
            System.out.println(e.getCause().toString());
        }
    }

    private void read (String path){
        String content = "";
        try {
//            System.out.println(path);
             content = Files.readString(Path.of(path), StandardCharsets.US_ASCII);
             data.add(content);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    private void print (){
        for (String s : data){
            System.out.println(s);
            System.out.println("------------------------------------------------------------------------------------------------------------");
        }
    }


    public List<String> getData(){
        return this.data;
    }


}
