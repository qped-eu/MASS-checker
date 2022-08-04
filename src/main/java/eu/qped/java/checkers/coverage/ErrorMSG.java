package eu.qped.java.checkers.coverage;

public class ErrorMSG {

    public static final String MISSING_CLASS = "Ups something went wrong! \n" +
            "There are no classes to test.\n" +
            "Solution: \n" +
            "- Check if you have provided a class in your answer.\n" +
            "- Check if you have complied with all folder conventions.\n";

    public static final String MISSING_TESTCLASS = "Ups something went wrong! \n" +
            "There are no test classes.\n" +
            "Solution: \n" +
            "- Check if you have provided a test class in your answer.\n" +
            "- Check if you have complied with all folder conventions.\n" +
            "- Check if you have complied with all naming conventions.\n";

    public static final String UPS = "Ups something went wrong!";

    public static final String MISSING_FILES = "Ups something went wrong!\n" +
            "Needs at lest an student answer or a resource form a teacher.";

    public static final String FAILED_DOWNLOAD = "Ups something went wrong!\n" +
            "Can't download following file %s.";

    public static final String FAILED_UNZIPPING = "Ups something went wrong!\n" +
            "Can't unzip following file %s.";


    public static final String NO_ZIP_FOLDER = "Ups something went wrong!\n" +
            "The following folder should end with .zip";


    public static final String CANT_READ_FILE = "Ups something went wrong!\n" +
            "It's not possible to read the following file %s.";
}
