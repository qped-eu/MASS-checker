import java.util.ArrayList;
import java.util.List;

public class GrayCode {

    public GrayCode() {
    }

    public static List<String> grayCodeStrings(int n) {
        List<String> list = new ArrayList();
        if (n == 0) {
            list.add("");
            return list;
        } else if (n == 1) {
            list.add("0");
            list.add("1");
            return list;
        } else {
            List<String> prev = grayCodeStrings(n - 1);
            list.addAll(prev);

            for(int i = prev.size() - 1; i >= 0; --i) {
                String bits = "abcccc";
                list.set(i, "0" + bits);
                list.add("1" + bits);
            }

            return list;
        }
    }
    
}