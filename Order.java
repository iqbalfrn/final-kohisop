public class Order {
    private final String itemKode;
    private final Item item;
    private final int kuantitas;

    public Order(String itemKode, Item item, int kuantitas) {
        this.itemKode = itemKode;
        this.item = item;
        this.kuantitas = kuantitas;
    }

    public String getItemKode() {
        return itemKode;
    }

    public Item getItem() {
        return item;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public int getTotalHarga() {
        return item.getHarga() * kuantitas;
    }

    public double getJumlahPajak(Member member) {
        if (member != null && member.getKodeMember().contains("A")) {
            return 0.0;
        }
        return getTotalHarga() * item.Pajak();
    }

    public double getTotaldenganPajak(Member member) {
        return getTotalHarga() + getJumlahPajak(member);
    }

    @Override
    public String toString() {
        return item.getKode() + " " + item.getNama() + " " + kuantitas;
    }
}