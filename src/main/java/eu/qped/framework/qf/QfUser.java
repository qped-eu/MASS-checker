package eu.qped.framework.qf;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QfUser extends QfObjectBase {
    private String id;
    private String firstName;
    private String lastName;
    private String language;

}