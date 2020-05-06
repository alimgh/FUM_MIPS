package ir.ac.um.mips;

public class Main {

//    TODO: test { lw, sw, beq }
//    TODO: implement { addi, slti, andi, ori, bne }
    public static void main(String[] args) {

        int pc = 0;
        InstructionMemory IM = new InstructionMemory();
        DataMemory DM = new DataMemory();
        RegisterFile RF = new RegisterFile();

        loadTest(IM, RF);

        String instruction = IM.readWord(pc);

        String op = instruction.substring(0, 6);
        Controller.setControl(op);

        String [] readData = RF.readWord(instruction.substring(6, 11), instruction.substring(11, 16));
        String signedEx = signedExtend(instruction.substring(16, 32));
        String ALUResult = ALU.calculate(
                ALUControl.getControl(instruction.substring(26, 32)),
                readData[0],
                (Controller.ALUSrc == 0 ? readData[1] : signedEx)
            );

        String memRead = "";
        if (Controller.MemWrite == 1)
            DM.writeWord(ALUResult, readData[1]);

        if (Controller.MemRead == 1)
            memRead = DM.readWord(ALUResult);

        if (Controller.RegWrite == 1)
            RF.writeWord(
                    (Controller.RegDst == 0? instruction.substring(11, 16) : instruction.substring(16, 21)),
                    (Controller.MemtoReg == 1? memRead : ALUResult)
            );

        pc += 4;
        if (Controller.jump == 1) {
            String h = int2bit(pc).substring(0, 4);
            String l = instruction.substring(6, 32) + "00";
            pc = Integer.parseInt(h+l, 2);

        } else if (Controller.Branch == 1 && bit2int(ALUResult) == 0) {
            pc += Integer.parseInt(signedEx, 2)<<2;
        }

        System.out.println(int2bit(pc));
//        while (pc < 1024) {
//
//        }
    }

    private static void loadTest(InstructionMemory im, RegisterFile rf) {
        rf.writeWord("00001", "00000000000000000000000000000010");
        rf.writeWord("00010", "00000000000000000000000000001011");
        im.writeWord("00000000", "00001000001000100001100000101010");
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
