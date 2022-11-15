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
public class TestLevel1 {

    TestLevel2 testLevel2;

    String key1;
    int key2;
    Map<String,String> mapTestLevel1;
    List<Object> listToIgnore;
}
