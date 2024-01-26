package eu.qped.java.checkers.mutation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MutationMessage {
    
    public static List<String> mutationMessageList = new ArrayList<>();
    
    private final String message;
    private final int messageOrder;
    private static int messageOrderCounter = 0; // Static counter to track the order

    public MutationMessage(String message) {
        this.message = message;
        this.messageOrder = messageOrderCounter++;
        mutationMessageList.add(message);
    }
}
