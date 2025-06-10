public interface Currency {
    String getKode();

    String getNama();

    double getKurs();

    double konversiDariIDR(double JumlahIDR);

    String FormatJumlah(double Jumlah);
}