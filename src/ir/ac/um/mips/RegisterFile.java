package ir.ac.um.mips;

public class RegisterFile {
    private String [] RF;

    public RegisterFile() {
        RF = new String[32]; //each cell has 32 length string
        RF[0] = "00000000000000000000000000000000";
    }

    public String [] readWord(String ad1, String ad2) {
        int address1 = bit2int(ad1);
        int address2 = bit2int(ad2);
        return new String[]{RF[address1], RF[address2]};
    }

    public void writeWord(String ad, String data) {
        int address = bit2int(ad);
        RF[address] = data;
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
