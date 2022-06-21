package test;

public class IfStmt {

    public int ifStmt(int num) {
        if (num < 0) {
            num = 0;
        }
        return num;
    }


    public int ifElseStmt(int num) {
        if (num < 0) {
            return 0;
        } else {
            return num;
        }
    }


    public int nestedIfStmts(int num) {
        if (num == 0) {
            if (num == 0) {
                num++;
            }
        } else if (num == 0) {
            if (num == 0) {
                num++;
            } else {
                num++;
            }
        } else {
            if (num == 0) {
                num++;
            } else if (num == 0) {
                num++;
            }
        }
        return num;
    }


    public int ifNoBody(int num) {
        if (num < 0) return 0;
        return num;
    }


}
