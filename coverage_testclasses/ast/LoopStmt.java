package test;

import java.util.List;

public class LoopStmt {

    public int forStmt(int num) {
        int sum = 0;
        for (int i = num; i < num*num; i++) {
            sum += i;
        }
        return sum;
    }

    public int foreachStmt(List<Integer> nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        return sum;
    }

    public int whileStmt(int num, int div) {
        int count = 0;
        while (num < 0) {
            num -= div;
            count++;
        }
        return count;

    }

}
