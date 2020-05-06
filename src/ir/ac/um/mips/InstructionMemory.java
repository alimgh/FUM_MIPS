package ir.ac.um.mips;

public class InstructionMemory {
    private String [] IM;

    public InstructionMemory() {
         IM = new String[256]; //each cell has 32 length string
    }

    public String readWord(int pc) {
//        int address = bit2int(ad);
        return IM[pc/4];
    }

    public void writeWord(String ad, String data) {
        int address = bit2int(ad);
        IM[address/4] = data;
    }

    private int bit2int(String ad) {
//        int result = 0;
//        int multiplier = 1;
//        String reversedAddress = new StringBuilder(ad).reverse().toString();
//
//        for (char i: reversedAddress.toCharArray()) {
//
//            if (i == '1') result += multiplier;
//            multiplier *= 2;
//        }
        return Integer.parseInt(ad, 2);
    }
}
