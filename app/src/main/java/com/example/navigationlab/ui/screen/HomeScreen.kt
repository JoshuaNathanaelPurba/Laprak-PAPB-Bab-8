package com.example.navigationlab.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navigationlab.data.model.Student
import com.example.navigationlab.ui.home.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    searchQuery: String,
    onStudentClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onAboutClick: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("NavigationLab", style = MaterialTheme.typography.headlineMedium)
        Text("Daftar Pengguna", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Cari profil") },
            placeholder = { Text("Contoh: John") },
            singleLine = true
        )

        Button(
            onClick = onRefresh,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text(if (uiState.isLoading) "Memuat..." else "Refresh")
        }

        when {
            uiState.isLoading -> Box(
                Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            uiState.errorMessage != null -> Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(uiState.errorMessage)
                Button(onClick = onRetry) { Text("Coba Lagi") }
            }

            uiState.students.isEmpty() -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(if (searchQuery.isBlank()) "Belum ada data pengguna." else "Pengguna tidak ditemukan.")
            }

            else -> LazyColumn(
                modifier = Modifier.weight(1f, fill = true),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.students, key = { it.id }) { student ->
                    StudentRow(student = student, onClick = { onStudentClick(student.id) })
                }
            }
        }

        Button(onClick = onProfileClick, modifier = Modifier.fillMaxWidth()) { Text("Profile") }
        Button(onClick = onAboutClick, modifier = Modifier.fillMaxWidth()) { Text("About") }
    }
}

@Composable
private fun StudentRow(student: Student, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("ID: ${student.id}", style = MaterialTheme.typography.labelLarge)
            Text("Nama: ${student.name}", style = MaterialTheme.typography.bodyLarge)
            Text(student.email, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
