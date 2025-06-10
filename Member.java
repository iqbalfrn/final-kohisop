import java.util.Random;

public class Member {
    private final String kodeMember;
    private final String nama;
    private int poin;

    public Member(String nama) {
        this.kodeMember = generateKodeMember();
        this.nama = nama;
        this.poin = 0;
    }

    private String generateKodeMember() {
        String characters = "ABCDEF0123456789";
        Random random = new Random();
        StringBuilder kode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            kode.append(characters.charAt(random.nextInt(characters.length())));
        }
        return kode.toString();
    }

    public String getKodeMember() {
        return kodeMember;
    }

    public String getNama() {
        return nama;
    }

    public int getPoin() {
        return poin;
    }

    public void tambahPoin(double totalHarga) {
        int poinBaru = (int) (totalHarga / 10);
        if (kodeMember.contains("A"))
            poinBaru *= 2;
        poin += poinBaru;
    }

    public double potongDenganPoin(double totalHarga) {
        if (poin > 0) {
            int poinDigunakan = Math.min(poin, (int) (totalHarga / 2));
            poin -= poinDigunakan;
            return totalHarga - (poinDigunakan * 2);
        }
        return totalHarga;
    }
}