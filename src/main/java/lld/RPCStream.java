package lld;

import java.util.Arrays;

public class RPCStream {
    private byte[] buffer = new byte[32];
    private int curIdx = 0;

    public void write(byte[] b, int off, int len) {
        System.out.println("Write byte[] was called.");
        for (int curOff=off; curOff<off+len; curOff++) write(b[curOff]);
    }

    public void write(int n) {
        System.out.println("Write int was called.");
        write((byte)n);
    }

    private void write(byte b) {
        buffer[curIdx++] = b;
        if (curIdx == 32) flush();      // if curIdx reached 32 flush
    }

    public void flush() {
        byte[] out;
        if (curIdx == 32) {
            out = buffer;
        } else {
            out = new byte[curIdx];
            System.arraycopy(buffer, 0, out, 0, curIdx);
        }
        buffer = new byte[32];
        curIdx = 0;
        System.out.println(Arrays.toString(out));
        System.out.println(new String(out));
    }


    public static void main(String[] args) {
        RPCStream ins = new RPCStream();
        String str = "Sandeep is so stupid. He messed up his interview. He should jump into a well.";
        byte[] bAr = str.getBytes();

        ins.write(bAr, 0, 13);
        ins.write(bAr, 13, 5);
        ins.write(bAr, 18, 7);
        ins.write(bAr, 25, 3);
        ins.write(2881);
        ins.write(bAr, 28, 9);
        ins.write(bAr, 37, bAr.length-37);
        ins.flush();

        System.out.println((byte)(2881>>8));
    }
}
