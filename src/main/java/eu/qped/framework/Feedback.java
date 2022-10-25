package eu.qped.framework;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated(since = "release v.1.2", forRemoval = true)
public abstract class Feedback {

    String body;

}
