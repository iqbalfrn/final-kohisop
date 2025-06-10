import java.util.LinkedList;

public class Menu {
    private final LinkedList<Item> items;
    public int JumlahItem = 0;
    
    public Menu() {

        items = new LinkedList<>();
        
        // daftar menu minuman
        TambahItem(new Minuman("A1", "Caffe Latte", 46));
        TambahItem(new Minuman("A2", "Cappuccino", 46));
        TambahItem(new Minuman("E1", "Caffe Americano", 37));
        TambahItem(new Minuman("E2", "Caffe Mocha", 55));
        TambahItem(new Minuman("E3", "Caramel Macchiato", 59));
        TambahItem(new Minuman("E4", "Asian Dolce Latte", 55));
        TambahItem(new Minuman("E5", "Double Shots Iced Shaken Espresso", 50));
        TambahItem(new Minuman("B1", "Freshly Brewed Coffee", 23));
        TambahItem(new Minuman("B2", "Vanilla Sweet Cream Cold Brew", 50));
        TambahItem(new Minuman("B3", "Cold Brew", 44));
        
        // daftar menu makanan
        TambahItem(new Makanan("M1", "Petemania Pizza", 112));
        TambahItem(new Makanan("M2", "Mie Rebus Super Mario", 35));
        TambahItem(new Makanan("M3", "Ayam Bakar Goreng Rebus Spesial", 72));
        TambahItem(new Makanan("M4", "Soto Kambing Iga Guling", 124));
        TambahItem(new Makanan("S1", "Singkong Bakar A La Carte", 37));
        TambahItem(new Makanan("S2", "Ubi Cilembu Bakar Arang", 58));
        TambahItem(new Makanan("S3", "Tempe Mendoan", 18));
        TambahItem(new Makanan("S4", "Tahu Bakso Extra Telur", 28));
    }
    
    private void TambahItem(Item item) {
        items.add(item);
        JumlahItem++;
    }
    
    public Item KodeMenu(String Kode) {
        for (Item  item : items) {
            if (item.getKode().equalsIgnoreCase(Kode)) {
                return item;
            }
        }
        return null;
    }
    
    public Item[] getDaftarMinuman() {
        LinkedList<Item> minuman = new LinkedList<>();
        for (Item item : items) {
            if (item instanceof Minuman) {
                minuman.add(item);
            }
        }
        minuman.sort((a, b) -> a.getKode().compareTo(b.getKode()));
        return minuman.toArray(Item[]::new);
    }
    
    public Item[] getDaftarMakanan() {
        LinkedList<Item> makanan = new LinkedList<>();
        for (Item item : items) {
            if (item instanceof Makanan) {
                makanan.add(item);
            }
        }
        makanan.sort((a, b) -> a.getKode().compareTo(b.getKode()));
        return makanan.toArray(Item[]::new);
    }
}