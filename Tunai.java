public class Tunai implements MetodePembayaran {
    @Override
    public String getNama() {
        return "Tunai";
    }

    @Override
    public double getDiskon() {
        return 0.0;
    }

    @Override
    public int getAdminFee() {
        return 0;
    }

    @Override
    public double totalBiayaAkhir(double totalHarga) {
        return totalHarga;
    }
}