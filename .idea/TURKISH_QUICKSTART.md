# IntelliJ IDEA Hızlı Başlangıç (Turkish Quick Start)

## Sorun: Run Butonu Devre Dışı

IntelliJ IDEA'da run butonu (▶️) devre dışı görünüyorsa, aşağıdaki adımları takip edin:

### Hızlı Çözüm

1. **Projeyi Açın**
   - File → Open → `focusappbackend` klasörünü seçin

2. **Maven'i İçe Aktarın**
   - Sağ üstte Maven popup'ı görünürse "Import" tıklayın
   - Veya: `pom.xml` dosyasına sağ tıklayın → Maven → Reload Project

3. **JDK 17 Ayarlayın**
   - File → Project Structure (Ctrl+Alt+Shift+S)
   - Project → SDK: Java 17 seçin
   - Eğer yoksa: Add SDK → Download JDK → Version 17 seçin

4. **Run Configuration'ı Kullanın**
   - Sağ üstteki dropdown'dan "FocusAppBackendApplication" seçin
   - Yeşil run butonuna (▶️) basın veya Shift+F10

### Eğer Hala Çalışmıyorsa

#### Annotation Processing'i Aktifleştirin
Settings → Build, Execution, Deployment → Compiler → Annotation Processors → "Enable annotation processing" işaretleyin

#### Lombok Plugin'i Yükleyin
Settings → Plugins → "Lombok" arayın ve yükleyin

#### Projeyi Temizleyin
Build → Clean Project → Build → Rebuild Project

### Environment Variables (Çevre Değişkenleri)

**Geliştirme için (Development):**
Uygulama artık varsayılan değerlerle çalışır. Direkt çalıştırabilirsiniz:

```bash
mvn spring-boot:run
```

**Üretim için (Production - ÖNEMLİ!):**
Üretim ortamında mutlaka şunları ayarlayın:

1. Run Configuration dropdown → Edit Configurations
2. "FocusAppBackendApplication" seçin
3. Environment variables bölümüne ekleyin:
   ```
   JWT_SECRET=gizli-anahtar-en-az-256-bit-uzunlugunda-olmali
   MONGODB_URI=mongodb://localhost:27017
   MONGODB_DATABASE=focusapp
   ```

⚠️ **Güvenlik Uyarısı**: Varsayılan JWT secret sadece geliştirme içindir. Üretimde mutlaka `JWT_SECRET` ayarlayın!

### MongoDB Başlatma

#### Docker ile (Önerilen)
```bash
docker-compose up -d
```

#### Manuel Kurulum
- macOS: `brew install mongodb-community && brew services start mongodb-community`
- Windows: MongoDB.com'dan indirin
- Linux: Paket yöneticinizi kullanın

### Uygulamayı Çalıştırma

#### Yöntem 1: Run Button (En Kolay)
1. Sağ üstte "FocusAppBackendApplication" seçili olduğundan emin olun
2. Yeşil ▶️ butonuna basın
3. Veya klavyeden: Shift+F10

#### Yöntem 2: Main Class'tan
1. `FocusAppBackendApplication.java` dosyasını açın
2. Dosyada herhangi bir yere sağ tıklayın
3. "Run 'FocusAppBackendApplication.main()'" seçin

#### Yöntem 3: Maven ile
1. Sağ tarafta Maven tool window'u açın
2. Plugins → spring-boot → spring-boot:run çift tıklayın

### Uygulama Çalışıyor mu Kontrol Edin

Tarayıcınızda şu adresi açın:
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

### Sık Karşılaşılan Hatalar

#### "ClassNotFoundException: com.focusapp.backend.FocusAppBackendApplication"
Bu hata, projenin derlenmediği (compile edilmediği) anlamına gelir. Çözümler:

1. **Maven ile derleyin** (Önerilen):
   ```bash
   mvn clean compile
   ```
   
2. **IntelliJ'de build edin**:
   - Build → Build Project (Ctrl+F9)
   - Veya: Build → Rebuild Project
   
3. **Maven'i yeniden yükleyin**:
   - `pom.xml` dosyasına sağ tık → Maven → Reload Project
   
4. **Run configuration'ı kontrol edin**:
   - Çalıştırmadan önce otomatik olarak derleme yapmalı
   - Yapmazsa, configuration'ı düzenleyin ve "Before launch" görevlerine "Maven compile" ekleyin

Derlenmiş sınıflar `target/classes/` dizininde olmalıdır. Bu dizin yoksa, proje build edilmelidir.

#### "Cannot resolve symbol 'lombok'"
- Settings → Plugins → Lombok yükleyin
- Settings → Annotation Processors → Enable işaretleyin
- Build → Rebuild Project

#### "Port 8080 already in use"
Başka bir uygulama 8080 portunu kullanıyor:
- O uygulamayı durdurun
- Veya farklı port kullanın: Environment variables'a `SERVER_PORT=8081` ekleyin

#### MongoDB bağlantı hatası
- MongoDB'nin çalıştığından emin olun: `docker-compose ps`
- Bağlantıyı test edin: MongoDB Compass ile `mongodb://localhost:27017`

#### "Error creating bean with name 'jwtUtils'" veya JWT Configuration hatası
Bu hata JWT secret ayarlanmadığında oluşurdu. Artık düzeltildi:

- Uygulama varsayılan bir secret ile çalışır (sadece geliştirme için)
- Üretim ortamında mutlaka `JWT_SECRET` environment variable ayarlayın
- Detaylar için: [JWT_CONFIG_FIX.md](JWT_CONFIG_FIX.md)

**Güvenli bir secret oluşturmak için:**
```bash
openssl rand -base64 32
```

### Daha Fazla Yardım

Detaylı İngilizce rehber için: [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)

### Yararlı Kısayollar

- Çalıştır: Shift+F10
- Debug: Shift+F9
- Durdur: Ctrl+F2
- Build: Ctrl+F9
- Maven Reload: Ctrl+Shift+O

### API Testleri

#### Swagger UI Kullanarak (En Kolay)
1. Uygulamayı çalıştırın
2. http://localhost:8080/swagger-ui.html adresini açın
3. API endpoint'leri test edin

#### Postman Kullanarak
- Proje kök dizinindeki `postman_collection.json` dosyasını Postman'e import edin

### Başarılı Çalıştırma Göstergeleri

Console'da şunları göreceksiniz:
```
Started FocusAppBackendApplication in X.XXX seconds
Tomcat started on port(s): 8080 (http)
```

Swagger UI açılıyorsa: ✅ Başarılı!

### Destek

Sorun yaşamaya devam ediyorsanız:
1. [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md) - Detaylı İngilizce rehber
2. [README.md](../README.md) - Proje dokümantasyonu
3. Build log'ları kontrol edin
4. Issue açın: GitHub repository

---

**Not**: Bu proje hazır IntelliJ run configuration'ları ile gelir. Normal şartlarda sadece projeyi açıp Maven import'u bekledikten sonra run butonuna basmanız yeterlidir.
