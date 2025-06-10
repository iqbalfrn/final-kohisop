public class IDR implements Currency {
    @Override
    public String getKode() {
        return "IDR";
    }

    @Override
    public String getNama() {
        return "Indonesian Rupiah";
    }

    @Override
    public double getKurs() {
        return 1.0;
    }

    @Override
    public double konversiDariIDR(double JumlahIDR) {
        return JumlahIDR;
    }

    @Override
    public String FormatJumlah(double Jumlah) {
        return String.format("%.0f IDR", Jumlah);
    }
}