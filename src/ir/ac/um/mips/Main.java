package ir.ac.um.mips;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

//    TODO: create test program
    public static void main(String[] args) {

        int pc = 0;
        InstructionMemory IM = new InstructionMemory();
        DataMemory DM = new DataMemory();
        RegisterFile RF = new RegisterFile();

//        loadTest(IM, RF, DM);
        loadProgramTest(IM, RF, DM);
//        getTest(IM, RF, DM);


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
                    (op.equals("000000") && instruction.substring(26, 32).equals("000000")?
                            readData[1]:
                            readData[0]
                    ),
                    (op.equals("000000") && instruction.substring(26, 32).equals("000000")?
                            instruction.substring(21, 26):
                            (Controller.ALUSrc == 0 ? readData[1] : signedEx)
                    )
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
                pc = (int) Long.parseLong(h + l, 2);

            } else if (Controller.Branch == 1)
                if (op.equals("000100") && bit2int(ALUResult) == 0 ||
                        op.equals("000101") && bit2int(ALUResult) != 0) {
                    pc += (int) Long.parseLong(signedEx, 2) << 2;
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

    private static void loadProgramTest(InstructionMemory im, RegisterFile rf, DataMemory dm) {
//        String[] iTemp = {
//                "00100000000001110000000000000111",
//                "00000000000000010001000000101010",
//                "10101100011001110000000000000000",
//                "10001100011001000000000000000000",
//                "00010100010001000000000000001000",
//                "00000000010000100010000000100000",
//                "00000000010000100010000000100000",
//                "00000000010000100010000000100000",
//                "00110000010001011111111111111111",
//                "00001000000000000000000000001011",
//                "00000000010000100010000000100000",
//                "00000000010000100010000000100000",
//                "10101100100000100000000000000000"};
        rf.writeWord("00101", "00000000000000000000000000000000");

        String[] iTemp = {
                "00100000000010100000000000001010",
                "00100000000001100000000000000000",
                "00000000000001100101100010000000",
                "00000001011001010101100000100000",
                "10001101011011000000000000000000",
                "00100001100101010000000000000000",
                "00100001100101100000000000000000",
                "00100000110001100000000000000001",
                "00010000110010100000000000001110",
                "00000000000001100101100010000000",
                "00000001011001010101100000100000",
                "10001101011011000000000000000000",
                "00000001100101010001000000101010",
                "00010000010000000000000000000100",
                "00100001100101010000000000000000",
                "00100000110001100000000000000001",
                "00010100110010101111111111111000",
                null,
                "00000001100101100001000000101010",
                "00010100010000001111111111110011",
                "00100001100101100000000000000000",
                "00100000110001100000000000000001",
                "00010100110010101111111111110001"};

        String[] dTemp = {
                "00000000000000000000000000001001",
                "00000000000000000000000000000100",
                "00000000000000000000000000000001",
                "00000000000000000000000000000100",
                "00000000000000000000000010000000",
                "00000000000000000000100000000000",
                "00000000000000000000010000000000",
                "00000010000000000000000000000000",
                "00000000000000000000100000000000",
                "00000000000000000000000000001000"};

        for (int i = 0; i<23; i++)
            im.writeWord(i*4, iTemp[i]);

        for (int i = 0; i<10; i++)
            dm.writeWord(i*4, dTemp[i]);
    }

    private static void getTest(InstructionMemory im, RegisterFile rf, DataMemory dm) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Number of Data:");
        int dataSize = scanner.nextInt();
        for (int i=0; i<dataSize; i++) {
            String data = int2bit(scanner.nextInt());
            dm.writeWord(i*4, data);
        }
        System.out.println();

        System.out.println("Number of Instructions:");
        int instrSize = scanner.nextInt();
        for (int i=0; i<instrSize; i++) {
            String instr = scanner.nextLine();
            im.writeWord(i*4, instr);
        }

//        System.out.println();
//
//        System.out.println("Number of Instructions:");
//        int rfSize = scanner.nextInt();
//        for (int i=0; i<rfSize; i++) {
//            String instr = scanner.nextLine();
//            im.writeWord(i*4, instr);
//        }
    }

    private static int bit2int(String data) {
        return (int) Long.parseLong(data, 2);
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
