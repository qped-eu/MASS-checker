package eu.qped.framework.feedback.fromatter;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyWordReplacer {

    Pattern keyWordPattern;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    ObjectMapper objectMapper = new ObjectMapper();

    private void setDefaultKeyWordPattern() {
        this.setKeyWordPattern(Pattern.compile("\\{\\$(.+?)\\}"));
    }

    public String replace(@NonNull String target, @NonNull Map<String, String> replacements) {
        if (keyWordPattern == null) setDefaultKeyWordPattern();
        Matcher matcher = keyWordPattern.matcher(target);
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            result.append(target, i, matcher.start());
            if (replacement == null)
                result.append(matcher.group(0));
            else
                result.append(replacement);
            i = matcher.end();
        }
        result.append(target.substring(i));
        return result.toString();
    }

    public String replace(@NonNull String target, @NonNull Object object) {
        if (objectMapper == null) objectMapper = new ObjectMapper();
        Map<String, String> replacements = mapObject(object);
        return replace(target, replacements);
    }

    private Map<String, String> mapObject(@NonNull Object objectToMap) {
        Map<String, Object> mappedObject = objectMapper.convertValue(objectToMap, Map.class);
        Map<String, String> result = new HashMap<>();

        mappedObject.forEach((fieldName, fieldValue) -> {
            if (fieldValue instanceof Integer
                    || fieldValue instanceof Short
                    || fieldValue instanceof Long
                    || fieldValue instanceof Float
                    || fieldValue instanceof Double
                    || fieldValue instanceof String
            ) {
                result.put(fieldName, String.valueOf(fieldValue));
            } else if (fieldValue != null
                    && !fieldValue.getClass().isPrimitive()
                    && !fieldValue.getClass().isArray()
                    && !(fieldValue instanceof Collection<?>)
            ) {
                try {
                    Map<String, String> subObjectMapped = mapObject(fieldValue);
                    result.putAll(subObjectMapped);
                } catch (Exception ignored) {}
            }
        });
        return result;
    }

    public static void main(String[] args) {
        TestLevel2 testLevel2 = TestLevel2.builder()
                .listToIgnore(new ArrayList<>())
                .mapTestLevel2(new HashMap<>() {{
                    put("key5", 5);
                    put("key6", 6);
                }})
                .key3("3")
                .key4(4)
                .build();
        TestLevel1 testLevel1 = TestLevel1.builder()
                .testLevel2(testLevel2)
                .key1("1")
                .key2(2)
                .listToIgnore(null)
                .mapTestLevel1(new HashMap<>() {{
                    put("key7", "7");
                    put("key8", "8");
                }})
                .build();
        String target = " {$key1} {$key2} {$key3} {$key4} {$key5} {$key6} {$key7} {$key8} {$key1} {$key2} {$} ";
        KeyWordReplacer keyWordReplacer = KeyWordReplacer.builder().build();
        String result = keyWordReplacer.replace(target, testLevel1);
        System.out.println(result);
    }

}
