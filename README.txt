NAVIGATIONLAB - MODUL PRAKTIKUM 08
Refactoring Coroutine, StateFlow, Flow, dan Refresh

Project ini merupakan pembaruan dari NavigationLab Modul 07. Fitur REST API,
Detail, Profile, About, error state, pencarian profil, serta navigasi tetap
dipertahankan. Arsitektur data dan ViewModel telah diubah menggunakan suspend
function, coroutine, StateFlow, dan lifecycle-aware collection.

CARA MENJALANKAN
1. Ekstrak ZIP.
2. Buka folder NavigationLab_Modul06 melalui Android Studio.
3. Pastikan perangkat/emulator terhubung ke internet.
4. Lakukan Gradle Sync.
5. Jalankan aplikasi pada emulator/perangkat Android.

IMPLEMENTASI MODUL 08
- StudentApiService menggunakan suspend function untuk endpoint users,
  users/{id}, dan users/search.
- StudentRemoteDataSource mengembalikan data secara langsung tanpa callback.
- StudentRepository menggunakan suspend function dan mengubah DTO menjadi
  model Student.
- HomeViewModel dan DetailViewModel menggunakan MutableStateFlow internal
  serta StateFlow read-only untuk UI.
- Request dijalankan melalui viewModelScope.launch.
- CancellationException dilempar kembali agar pembatalan coroutine berjalan
  dengan benar.
- Compose mengoleksi StateFlow menggunakan collectAsStateWithLifecycle().
- Pencarian berjalan otomatis setelah pengguna berhenti mengetik 500 ms.
  debounce mengurangi request, distinctUntilChanged mencegah query identik
  diproses berulang, dan collectLatest membatalkan pekerjaan pencarian lama.
- Query kosong memuat kembali daftar awal.
- Retry mengulangi request sesuai query yang sedang aktif.

TUGAS PRAKTIKUM: REFRESH
- HomeScreen memiliki tombol Refresh.
- Tombol memanggil callback onRefresh yang dihubungkan ke HomeViewModel.refresh().
- refresh() mengambil query aktif dari searchQuery.value dan menjalankan
  request melalui viewModelScope.launch.
- Jika query kosong, refresh memanggil repository.getStudents().
- Jika query berisi teks, refresh memanggil repository.searchStudents(query).
- Saat request dimulai, isLoading menjadi true dan errorMessage dibersihkan.
- Jika berhasil, daftar diperbarui dan loading berhenti.
- Jika gagal, loading berhenti dan errorMessage diisi pesan kegagalan.
- Tidak ada CoroutineScope baru atau GlobalScope di composable.
- Navigasi Detail dan kembali ke Home tetap menggunakan Navigation Compose.

PENGUJIAN YANG DISARANKAN
1. Jalankan aplikasi dengan koneksi internet aktif.
2. Tunggu daftar awal selesai dimuat.
3. Ambil screenshot kondisi Home sebelum Refresh.
4. Tekan tombol Refresh dan amati indikator loading.
5. Setelah daftar tampil kembali, ambil screenshot kondisi sesudah Refresh.
6. Masukkan kedua screenshot asli dari emulator/perangkat ke laporan.
7. Ketik query, misalnya "John", lalu tunggu sekitar 500 ms. Pastikan hasil
   pencarian berubah otomatis.
8. Tekan Refresh saat query "John" masih terisi. Pastikan hasil pencarian
   diulang untuk query tersebut, bukan daftar awal.
9. Kosongkan kolom pencarian dan tunggu daftar awal dimuat kembali.
10. Buka salah satu pengguna, periksa halaman Detail, lalu tekan Kembali.
11. Uji kondisi gagal dengan mematikan koneksi internet, lalu gunakan Coba Lagi
    atau Refresh setelah koneksi dipulihkan.

SCREENSHOT LAPORAN
Screenshot emulator tidak dibuat atau direkayasa di dalam ZIP ini. Ambil
screenshot langsung dari emulator/perangkat setelah aplikasi dijalankan.

Saran caption:
- "Gambar 1. Tampilan daftar pengguna sebelum tombol Refresh ditekan."
- "Gambar 2. Tampilan daftar pengguna setelah proses Refresh berhasil."
- "Gambar 3. Hasil pencarian profil secara reaktif."
- "Gambar 4. Tampilan halaman Detail pengguna."

DEPENDENCY UTAMA
- kotlinx-coroutines-android: 1.11.0
- lifecycle-viewmodel-ktx: 2.9.4
- lifecycle-viewmodel-compose: 2.9.4
- lifecycle-runtime-compose: 2.9.4

Catatan: versi Lifecycle disamakan pada seluruh dependency Lifecycle.
