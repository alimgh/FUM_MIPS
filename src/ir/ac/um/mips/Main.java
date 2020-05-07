package ir.ac.um.mips;

import java.util.Arrays;

public class Main {

//    TODO: implement { bne } and test program
    public static void main(String[] args) {

        int pc = 0;
        InstructionMemory IM = new InstructionMemory();
        DataMemory DM = new DataMemory();
        RegisterFile RF = new RegisterFile();

        loadTest(IM, RF, DM);

        String instruction = IM.readWord(pc);

        while (instruction != null && !instruction.equals("00000000000000000000000000000000")) {

            // op and control
            String op = instruction.substring(0, 6);
            Controller.setControl(op);

            // read from RF
            String[] readData = RF.readWord(instruction.substring(6, 11), instruction.substring(11, 16));

            // signed extend immediate
            String signedEx = signedExtend(instruction.substring(16, 32));

            // ALU
            String ALUResult = ALU.calculate(
                    ALUControl.getControl(instruction.substring(26, 32), op),
                    readData[0],
                    (Controller.ALUSrc == 0 ? readData[1] : signedEx)
            );

            // Write to Memory
            if (Controller.MemWrite == 1)
                DM.writeWord(ALUResult, readData[1]);

            // Read from Memory
            String memRead = "";
            if (Controller.MemRead == 1)
                memRead = DM.readWord(ALUResult);

            // Write to Register
            if (Controller.RegWrite == 1)
                RF.writeWord(
                        (Controller.RegDst == 0 ? instruction.substring(11, 16) : instruction.substring(16, 21)),
                        (Controller.MemtoReg == 1 ? memRead : ALUResult)
                );

            // new PC
            pc += 4;
            if (Controller.jump == 1) {
                String h = int2bit(pc).substring(0, 4);
                String l = instruction.substring(6, 32) + "00";
                pc = Integer.parseInt(h + l, 2);

            } else if (Controller.Branch == 1 && bit2int(ALUResult) == 0) {
                pc += Integer.parseInt(signedEx, 2) << 2;
            }

            instruction = IM.readWord(pc);
//            System.out.println(RF.readWord(3));
        }

        System.out.println(pc);
        System.out.println(Arrays.toString(DM.top20()));
        System.out.println(Arrays.toString(RF.getRF()));

    }

    private static void loadTest(InstructionMemory im, RegisterFile rf, DataMemory dm) {
        rf.writeWord("00001", "00000000000000000000000000000010");
        rf.writeWord("00010", "00000000000000000000000000001011");
        im.writeWord("00000000", "00101000010000110000000000001011");

//        im.writeWord("00000000", "00101000010000110000000000001011"); // slti
//        im.writeWord("00000000", "00110100010000110000000110001010"); // ori
//        im.writeWord("00000000", "00110000010000111111111111111110"); // andi
//        im.writeWord("00000000", "00100000001000111111111111111110"); // addi
//        im.writeWord("00000000", "00100000000000111111111111111110"); // addi
//        dm.writeWord(8, "00000000000111000000000000001011"); // lw
//        im.writeWord("00000000", "10001100001000110000000000000110"); // lw
//        im.writeWord("00000000", "10101100001000100000000000000010"); // sw
//        im.writeWord("00000000", "00001000001000100001100000101010"); // Jump
//        im.writeWord("00000000", "00000000001000100001100000101010"); // R-Type
//        im.saveWord("00000000", "000000 00001 00002 00003 00000 100000");
    }

    private static int bit2int(String data) {
        int result = 0;
        int multiplier = 1;
        String reversedAddress = new StringBuilder(data).reverse().toString();

        for (char i: reversedAddress.toCharArray()) {

            if (i == '1') result += multiplier;
            multiplier *= 2;
        }
        return result;
    }

    private static String int2bit(int data) {
        String result = "";
        int unsigned = Math.abs(data);
        while (unsigned > 1) {
            result = (unsigned%2==1 ? '1':'0') + result;
            unsigned /= 2;
        }
        result = Integer.toString(unsigned) + result;

        if (data < 0) {
            String tmp = result;
            result = "";

            int j=tmp.length()-1;
            while (tmp.charAt(j) == '0') {
                result += "0"; j--;
            }
            result = '1' + result;

            for (int i=j-1; i>=0; i--)
                result = (tmp.charAt(i) == '0'? '1':'0') + result;

            while (result.length() < 32) result = '1' + result;

        } else {
            while (result.length() < 32) result = '0' + result;
        }

        return result;
    }

    private static String signedExtend(String data) {
        String result = data;

        if (result.charAt(0) == '1')
            while (result.length() < 32) result = '1' + result;
        else
            while (result.length() < 32) result = '0' + result;

        return result;
    }
}
