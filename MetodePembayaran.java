public interface MetodePembayaran {
    String getNama();
    double getDiskon();
    int getAdminFee();
    double totalBiayaAkhir(double BiayaAkhir);
}
