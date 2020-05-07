package ir.ac.um.mips;

public class ALUControl {
    public static String getControl(String funct, String op) {
        String result = "";
        if (Controller.ALUOp1 == 0) {
            if (Controller.ALUOp0 == 0) return "0010";
            else return "0110";

        } else if (Controller.ALUOp1 == 1 && Controller.ALUOp0 == 1) {
            switch (op) {
                case "001000" -> result = "0010"; // addi
                case "001100" -> result = "0000"; // andi
                case "001101" -> result = "0001"; // ori
                case "001010" -> result = "0111"; // slti
            }

        } else {
            switch (funct) {
                case "100000" -> result = "0010"; // add
                case "001000" -> result = "0010"; // addi
                case "100010" -> result = "0110"; // sub
                case "100100" -> result = "0000"; // and
                case "001100" -> result = "0000"; // andi
                case "100101" -> result = "0001"; // or
                case "001101" -> result = "0001"; // ori
                case "101010" -> result = "0111"; // slt
                case "001010" -> result = "0111"; // slti
            }
        }
        return result;
    }
}
