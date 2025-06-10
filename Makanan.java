public class Makanan extends Item {
    public Makanan(String kode, String nama, int harga) {
        super(kode, nama, harga);
    }

    @Override
    public double Pajak() {
        if (harga < 50)
            return 0.11;
        else
            return 0.08;
    }
}