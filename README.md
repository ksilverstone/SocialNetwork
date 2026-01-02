# Sosyal Ağ Analizi Projesi

Bu proje, sosyal ağları graf teorisi kullanarak modelleyen ve çeşitli graf algoritmaları ile analiz eden bir Java uygulamasıdır. Kullanıcılar arasındaki ilişkileri görselleştirir ve farklı algoritmalar ile ağ yapısını incelemeye olanak sağlar.

## Ekran Görüntüleri

![images/WhatsApp Image 2026-01-02 at 16.49.10 (1).jpeg](images/WhatsApp%20Image%202026-01-02%20at%2016.49.10%20(1).jpeg)

![images/WhatsApp Image 2026-01-02 at 16.49.10 (2).jpeg](images/WhatsApp%20Image%202026-01-02%20at%2016.49.10%20(2).jpeg)

![images/WhatsApp Image 2026-01-02 at 16.49.10.jpeg](images/WhatsApp%20Image%202026-01-02%20at%2016.49.10.jpeg)

![images/WhatsApp Image 2026-01-02 at 16.49.13 (1).jpeg](images/WhatsApp%20Image%202026-01-02%20at%2016.49.13%20(1).jpeg)

![images/WhatsApp Image 2026-01-02 at 16.49.13 (2).jpeg](images/WhatsApp%20Image%202026-01-02%20at%2016.49.13%20(2).jpeg)

![images/WhatsApp Image 2026-01-02 at 16.49.13.jpeg](images/WhatsApp%20Image%202026-01-02%20at%2016.49.13.jpeg)

![images/WhatsApp Image 2026-01-02 at 16.49.14.jpeg](images/WhatsApp%20Image%202026-01-02%20at%2016.49.14.jpeg)

## Özellikler

### Graf Modeli
- **UserNode**: Kullanıcıları temsil eden düğümler (aktiflik, etkileşim, bağlantı skorları)
- **RelationshipEdge**: Kullanıcılar arası ilişkileri temsil eden kenarlar
- **Dinamik Ağırlık Hesaplama**: Kenar ağırlıkları, kullanıcıların özellik farklarına göre dinamik olarak hesaplanır

### Uygulanan Algoritmalar

1. **BFS (Breadth-First Search)**
   - Genişlik öncelikli arama
   - Graf üzerinde dalga dalga yayılım
   - En kısa yol bulma (ağırlıksız graflar için)

2. **DFS (Depth-First Search)**
   - Derinlik öncelikli arama
   - Erişilebilir düğümleri bulma
   - Çalışma süresi ve ziyaret edilen düğüm sayısı takibi

3. **A* (A-Star) Algoritması**
   - En kısa yol bulma algoritması
   - Heuristic fonksiyon kullanarak optimize edilmiş arama
   - Özellik farklarına dayalı tahmin fonksiyonu

4. **Bağlı Bileşenler (Connected Components)**
   - Graf içindeki bağlı bileşenleri bulma
   - Her bileşen farklı renkle görselleştirilir

5. **Welsh-Powell Algoritması**
   - Graf renklendirme algoritması
   - Komşu düğümlerin aynı renge sahip olmaması kuralı

### Kullanıcı Arayüzü

- **İki Sekme Yapısı**: İki farklı kullanıcı için ayrı test panelleri
- **Görselleştirme**: Graf yapısının görsel olarak çizilmesi
- **Etkileşimli Kontroller**: Butonlar ile algoritmaları çalıştırma
- **Sonuç Gösterimi**: Algoritma sonuçlarının mesaj kutuları ile gösterilmesi

## Proje Yapısı

```
SocialNetwork/
├── src/
│   ├── Main.java                    # Ana giriş noktası
│   ├── model/
│   │   ├── SocialGraph.java         # Graf veri yapısı
│   │   ├── UserNode.java            # Kullanıcı düğüm modeli
│   │   └── RelationshipEdge.java   # İlişki kenar modeli
│   ├── algorithms/
│   │   ├── IGraphAlgorithm.java     # Algoritma arayüzü
│   │   ├── BFSAlgorithm.java        # BFS implementasyonu
│   │   ├── DFS.java                 # DFS implementasyonu
│   │   ├── AStarAlgorithm.java      # A* implementasyonu
│   │   ├── ConnectedComponents.java # Bağlı bileşenler
│   │   └── WelshPowellAlgorithm.java # Graf renklendirme
│   └── ui/
│       ├── MainFrame.java           # Ana pencere
│       └── GraphPanel.java          # Graf çizim paneli
├── images/                          # Ekran görüntüleri
│   ├── WhatsApp Image 2026-01-02 at 16.49.10 (1).jpeg
│   ├── WhatsApp Image 2026-01-02 at 16.49.10 (2).jpeg
│   ├── WhatsApp Image 2026-01-02 at 16.49.10.jpeg
│   ├── WhatsApp Image 2026-01-02 at 16.49.13 (1).jpeg
│   ├── WhatsApp Image 2026-01-02 at 16.49.13 (2).jpeg
│   ├── WhatsApp Image 2026-01-02 at 16.49.13.jpeg
│   └── WhatsApp Image 2026-01-02 at 16.49.14.jpeg
└── README.md
```

## Gereksinimler

- Java JDK 8 veya üzeri
- Swing kütüphanesi (Java ile birlikte gelir)

## Kurulum ve Çalıştırma

1. Projeyi klonlayın veya indirin:
```bash
git clone <repository-url>
cd SocialNetwork
```

2. Java dosyalarını derleyin:
```bash
javac -d . src/**/*.java src/*.java
```

3. Uygulamayı çalıştırın:
```bash
java Main
```

Alternatif olarak, bir IDE (IntelliJ IDEA, Eclipse, VS Code) kullanarak projeyi açıp doğrudan `Main.java` dosyasını çalıştırabilirsiniz.

## Kullanım

### Kişi 1 Sekmesi
- **BFS Başlat**: BFS algoritmasını çalıştırır ve ziyaret sırasını gösterir
- **Welsh-Powell**: Grafı renklendirir (komşu düğümler farklı renkler alır)
- **Grafı Sıfırla**: Tüm görselleştirmeleri temizler

### Kişi 2 Sekmesi
- **DFS Başlat**: DFS algoritmasını çalıştırır ve erişilebilir düğümleri gösterir
- **A* (En Kısa Yol)**: Başlangıç ve hedef düğüm ID'lerini girerek en kısa yolu bulur
- **Bağlı Bileşenler**: Graf içindeki tüm bağlı bileşenleri bulur ve renklendirir
- **Grafı Sıfırla**: Tüm görselleştirmeleri temizler

## Algoritma Detayları

### Dinamik Ağırlık Hesaplama
Kenar ağırlıkları, kullanıcıların özellik farklarına göre hesaplanır:
```
weight = 1 + √((ΔAktiflik²) + (ΔEtkileşim²) + (ΔBağlantı²))
```

### A* Heuristic Fonksiyonu
A* algoritması, özellik farklarına dayalı bir heuristic kullanır:
```
h(n) = √((ΔAktiflik²) + (ΔEtkileşim²) + (ΔBağlantı²))
```

## Test Verisi

Uygulama başlatıldığında otomatik olarak 6 düğümlü bir test grafı oluşturulur:
- Düğümler 0-5 ID'leri ile numaralandırılmıştır
- Her düğümün aktiflik, etkileşim ve bağlantı skorları vardır
- Düğümler arasında çeşitli bağlantılar tanımlanmıştır

## Teknolojiler

- **Java**: Programlama dili
- **Java Swing**: Grafik kullanıcı arayüzü
- **Graf Teorisi**: Algoritma temelleri

## Lisans
