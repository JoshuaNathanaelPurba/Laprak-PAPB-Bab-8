# Dokumentasi Pembaruan Modul 08

## Pencarian reaktif
`HomeViewModel` menyimpan query dalam `MutableStateFlow`. Perubahan query
dikoleksi melalui `debounce(500)`, `distinctUntilChanged()`, dan `collectLatest`.
Jika query kosong, aplikasi mengambil daftar pengguna. Jika tidak kosong,
aplikasi memanggil endpoint pencarian DummyJSON.

## Coroutine dan StateFlow
Endpoint Retrofit menggunakan `suspend`. Remote data source dan repository
juga menggunakan `suspend`, sehingga callback `Call<T>` dan `enqueue()` tidak
lagi digunakan. Home dan Detail mengekspos `StateFlow` read-only kepada UI.
Compose mengoleksi state menggunakan `collectAsStateWithLifecycle()`.

## Refresh
Tombol Refresh pada `HomeScreen` memanggil `HomeViewModel.refresh()`.
ViewModel membaca query aktif lalu menjalankan request dalam `viewModelScope`.
Refresh memuat daftar awal saat query kosong atau mengulangi pencarian saat
query berisi teks. Selama request berlangsung, `isLoading` diaktifkan dan
`errorMessage` dibersihkan. Jika request gagal, pesan error ditampilkan.

## File yang diperbarui
- `app/build.gradle.kts`
- `data/remote/StudentApiService.kt`
- `data/remote/StudentRemoteDataSource.kt`
- `data/repository/StudentRepository.kt`
- `ui/home/HomeUiState.kt`
- `ui/home/HomeViewModel.kt`
- `ui/detail/DetailViewModel.kt`
- `ui/screen/HomeScreen.kt`
- `ui/screen/DetailScreen.kt`
- `navigation/NavigationLabApp.kt`

## Catatan screenshot
Ambil screenshot sebelum dan sesudah menekan Refresh dari emulator/perangkat
asli. ZIP ini tidak menyertakan screenshot simulasi.
