package ir.ac.um.mips;

public class ALU {
    public static String calculate(String ALUControl, String inp1, String inp2) {
        String result = "";

        int a = bit2int(inp1);
        int b = bit2int(inp2);

        switch (ALUControl) {
            case "0000":
                result = int2bit(a&b);
                break;

            case "0001":
                result = int2bit(a|b);
                break;

            case "0010":
                result = int2bit(a+b);
                break;

            case "0110":
                result = int2bit(a-b);
                break;

            case "0111":
                if (a < b) result = int2bit(1);
                else result = int2bit(0);
                break;
        }

//        if (result.charAt(0) == '1')
//            while (result.length() < 32) result = '1' + result;
//        else
//            while (result.length() < 32) result = '0' + result;

        return result;
    }

    private static int bit2int(String data) {
//        int result = 0;
//        int multiplier = 1;
//        String reversedAddress = new StringBuilder(data).reverse().toString();
//
//        for (char i: reversedAddress.toCharArray()) {
//
//            if (i == '1') result += multiplier;
//            multiplier *= 2;
//        }
        return Integer.parseInt(data, 2);
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
}
