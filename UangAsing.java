public abstract class UangAsing implements Currency {
    @Override
    public double konversiDariIDR (double JumlahIDR) {
        return JumlahIDR / getKurs();
    }

    @Override
    public String FormatJumlah(double Jumlah) {
        return String.format("%.2f %s", Jumlah, getKode());
    }
}
