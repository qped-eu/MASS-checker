package eu.qped.framework.feedback.fromatter;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

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


    private void setDefaultKeyWordPattern() {
        this.setKeyWordPattern(Pattern.compile("\\{\\$(.+?)\\}"));
    }


    public String replace(@NonNull String target, @NonNull String toReplace, @NonNull String replacement) {
        if (keyWordPattern == null) setDefaultKeyWordPattern();
        Matcher matcher = keyWordPattern.matcher(target);
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String match = matcher.group(1);
            if (match.equals(toReplace)) {
                result.append(target, i, matcher.start());
                result.append(replacement);
                i = matcher.end();
            }
        }
        result.append(target.substring(i));
        return result.toString();
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
        Map<String, String> replacements = mapObject(object);
        return replace(target, replacements);
    }

    private Map<String, String> mapObject(@NonNull Object objectToMap) {
        var objectMapper = new ObjectMapper();
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
                } catch (Exception ignored) {
                }
            }
        });
        return result;
    }


}
