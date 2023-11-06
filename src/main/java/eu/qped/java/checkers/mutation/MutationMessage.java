package eu.qped.java.checkers.mutation;

import lombok.Getter;

@Getter
public class MutationMessage {
    private final String message;
    private final int messageOrder;
    private static int messageOrderCounter = 0; // Static counter to track the order

    public MutationMessage(String message) {
        this.message = message;
        this.messageOrder = messageOrderCounter++;
        MutationInfrastructure.mutationMessageList.add(this.message);
    }
}
