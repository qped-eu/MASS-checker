package eu.qped.java.checkers.syntax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Deprecated(forRemoval = true)
public class ErrorInfo {

    private String errorKey;
    private String errorDescription;

}
