package eu.qped.java.feedback.syntax;

import eu.qped.java.checkers.syntax.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class SyntaxFeedbackDataNew {
    private final static String NEWLINE = "\n";

    //FIXME no static
    public Map<String, List<SyntaxFeedbackNew>> getSyntaxFeedbackByErrorCode() {
        Map<String, List<SyntaxFeedbackNew>> feedbackBySyntaxErrorCode = new HashMap<>();
//        feedbackBySyntaxErrorCode.put(
//                // TODO
//                "compiler.err.illegal.start.of.type",
//                List.of(
//                        SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " for"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+for")
//                                                .build()
//                                )
//                                .build()
//                        ,
//                        SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " switch"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+switch")
//                                                .build()
//                                )
//                                .build()
//                        ,
//                        SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " while"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+while")
//                                                .build()
//                                )
//                                .build()
//                        , SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " if"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+if")
//                                                .build()
//                                )
//                                .build()
//                        , SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " else"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+else")
//                                                .build()
//                                )
//                                .build()
//                        , SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " System"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+System")
//                                                .build()
//                                )
//                                .build()
//                        ,
//                        SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " break"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+break")
//                                                .build()
//                                )
//                                .build()
//                        ,
//                        SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " continue"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+continue")
//                                                .build()
//                                )
//                                .build()
//                        ,
//                        SyntaxFeedbackNew.builder()
//                                .feedbackContent(""
//                                        + "you have used the Type:"
//                                        + " case"
//                                        + " in a wrong place"
//                                )
//                                .solutionExample("")
//                                .errorMessage("")
//                                .errorInfo(
//                                        ErrorInfo.builder()
//                                                .errorKey("compiler.err.illegal.start.of.type+µ+case")
//                                                .build()
//                                )
//                                .build()
//                )
//
//        );
        return feedbackBySyntaxErrorCode;
    }

}
