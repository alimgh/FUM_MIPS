package ir.ac.um.mips;

public class Controller {
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

    public static void setControl(String op) {
        switch (op) {

            // R-Type
            case "000000" -> {
                RegDst = 1;
                jump = 0;
                ALUSrc = 0;
                MemtoReg = 0;
                RegWrite = 1;
                MemRead = 0;
                MemWrite = 0;
                Branch = 0;
                ALUOp1 = 1;
                ALUOp0 = 0;
            }

            // lw
            case "100011" -> {
                RegDst = 0;
                jump = 0;
                ALUSrc = 1;
                MemtoReg = 1;
                RegWrite = 1;
                MemRead = 1;
                MemWrite = 0;
                Branch = 0;
                ALUOp1 = 0;
                ALUOp0 = 0;
            }

            // sw
            case "101011" -> {
                jump = 0;
                ALUSrc = 1;
                RegWrite = 0;
                MemRead = 0;
                MemWrite = 1;
                Branch = 0;
                ALUOp1 = 0;
                ALUOp0 = 0;
            }

            // beq
            case "000100" -> {
                jump = 0;
                ALUSrc = 0;
                RegWrite = 0;
                MemRead = 0;
                MemWrite = 0;
                Branch = 1;
                ALUOp1 = 0;
                ALUOp0 = 1;
            }

            // j
            case "000010" -> {
                jump = 1;
                RegWrite = 0;
                MemWrite = 0;
            }
        }
    }
}
