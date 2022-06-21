package test;

public class CaseStmt {

    public int caseStmt(int num) {
        switch (num%2) {
            case 0 :
                num++;
                break;
            case 1 :
                num--;
                break;
        }
        return num;
    }

    public int caseDefaultStmt(int num) {
        switch (num) {
            case 0 :
                num = 100;
                break;
            case 1 :
                num = 200;
                break;
            default:
                num = 0;
                break;
        }
        return num;
    }


    public int nestedCaseStmt(int num) {
        switch (num) {
            case 0:
                num = 100;
                break;
            default:
                switch (num%2) {
                    case 0 :
                        num = 0;
                        break;
                    case 1 :
                        num = 1;
                        break;
                }
        }
        return num;
    }

}
