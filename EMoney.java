public class EMoney implements MetodePembayaran {
    @Override
    public String getNama() {
        return "e-money";
    }

    @Override
    public double getDiskon() {
        return 0.07;
    }

    @Override
    public int getAdminFee() {
        return 20;
    }

    @Override
    public double totalBiayaAkhir(double totalHarga) {
        return (totalHarga * (1 - getDiskon())) + getAdminFee();
    }
}