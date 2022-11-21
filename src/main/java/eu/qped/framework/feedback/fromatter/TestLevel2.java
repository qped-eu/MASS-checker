package eu.qped.framework.feedback.fromatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;



@Getter
@Setter
@AllArgsConstructor
@Builder
public class TestLevel2 {

    String key3;
    int key4;
    Map<String,Integer> mapTestLevel2;
    List<Object> listToIgnore;
}
