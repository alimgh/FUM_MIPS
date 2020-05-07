package ir.ac.um.mips;

public class DataMemory {
    private String [] DM;

    public DataMemory() {
        DM = new String[256]; //each cell has 32 length string
    }

    public String readWord(String ad) {
        int address = bit2int(ad);
        return DM[address/4];
    }

    public String readWord(int ad) {
        return DM[ad/4];
    }

    public void writeWord(String ad, String data) {
        int address = bit2int(ad);
        DM[address/4] = data;
    }

    public void writeWord(int ad, String data) {
        DM[ad/4] = data;
    }

    private int bit2int(String ad) {
        return (int) Long.parseLong(ad, 2);
    }

    public String [] top20() {
        String[] out = new String[20];
        for(int i = 0; i < 20; i++) {
            out[i] = DM[i];
        }
        return out;
    }
}
