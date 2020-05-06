package ir.ac.um.mips;

public class ALUControl {
    public static int RegDst = 0;
    public static int ALUSrc = 0;
    public static int MemtoReg = 0;
    public static int RegWrite = 0;
    public static int MemRead = 0;
    public static int MemWrite = 0;
    public static int Branch = 0;
    public static int ALUOp1 = 0;
    public static int ALUOp0 = 0;
    public static int jump = 0;

    public static String getControl(String funct) {
        String result = "";
        if (Controller.ALUOp1 == 0) {
            if (Controller.ALUOp0 == 0) return "0010";
            else return "0110";
        }

        switch (funct) {
            case "100000" -> result = "0010";
            case "100010" -> result = "0110";
            case "100100" -> result = "0000";
            case "100101" -> result = "0001";
            case "101010" -> result = "0111";
        }
        return result;
    }
}
