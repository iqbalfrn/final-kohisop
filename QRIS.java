public class QRIS implements MetodePembayaran {
    @Override
    public String getNama() {
        return "QRIS";
    }

    @Override
    public double getDiskon() {
        return 0.05;
    }

    @Override
    public int getAdminFee() {
        return 0;
    }

    @Override
    public double totalBiayaAkhir(double totalHarga) {
        return totalHarga * (1 - getDiskon());
    }
}

