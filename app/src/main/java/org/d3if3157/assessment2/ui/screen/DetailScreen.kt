package org.d3if3157.assessment2.ui.screen
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3157.assessment2.R
import org.d3if3157.assessment2.database.MahasiswaDb
import org.d3if3157.assessment2.ui.theme.Assessment2Theme
import org.d3if3157.assessment2.util.ViewModelFactory

const val KEY_ID = "idMahasiswa"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null){
    val context = LocalContext.current
    val db = MahasiswaDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nilai by remember {
        mutableStateOf("")
    }
    var nama by remember {
        mutableStateOf("")
    }
    var kelas by remember {
        mutableStateOf("")
    }

    LaunchedEffect(true){
        if (id == null) return@LaunchedEffect

        val data = viewModel.getMahasiswa(id) ?: return@LaunchedEffect
        nilai = data.nilai.toString()
        nama = data.nama
        kelas = data.kelas

    }
    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null) Text(text = stringResource(id = R.string.tambah_mahasiswa))
                    else Text(text = stringResource(id = R.string.edit_mahasiswa))
                },

                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            if (nama.equals("") || nilai.equals("") || kelas.equals("")) {
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                                return@IconButton
                            }
                            if (id == null) viewModel.insert(nama, nilai.toFloat(), kelas)
                            else viewModel.update(id, nama, nilai.toFloat(), kelas)
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null){
                        DeleteById {
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        },
    ){
            padding ->
        FormMahasiswa(
            id = id,
            nilai = nilai,
            onNilaiChange = { nilai = it },
            nama =  nama,
            onNamaChange = { nama = it},
            kelas = kelas,
            onKelasChange = { kelas = it},
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun FormMahasiswa(
    id: Long?,
    nilai: String,
    onNilaiChange: (String) -> Unit,
    nama: String,
    onNamaChange: (String) -> Unit,
    kelas: String,
    onKelasChange: (String) -> Unit,
    modifier: Modifier
) {
    var nilaiMtk by remember { mutableStateOf("") }
    var nilaiIndo by remember { mutableStateOf("") }
    var nilaiIPA by remember { mutableStateOf("") }
    var nilaiAkhir by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = { onNamaChange(it) },
            label = { Text(text = stringResource(id = R.string.isi_mahasiswa)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (id != null) {
            OutlinedTextField(
                value = nilai,
                onValueChange = { onNilaiChange(it) },
                label = { Text(text = stringResource(id = R.string.nilai)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedTextField(
                value = nilaiMtk,
                onValueChange = { nilaiMtk = it },
                label = { Text(text = stringResource(id = R.string.nilai)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nilaiIndo,
                onValueChange = { nilaiIndo = it },
                label = { Text(text = stringResource(id = R.string.nilai)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nilaiIPA,
                onValueChange = { nilaiIPA = it },
                label = { Text(text = stringResource(id = R.string.nilai)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            LaunchedEffect(nilaiMtk, nilaiIndo, nilaiIPA) {
                val mtk = nilaiMtk.toFloatOrNull() ?: 0f
                val indo = nilaiIndo.toFloatOrNull() ?: 0f
                val ipa = nilaiIPA.toFloatOrNull() ?: 0f
                nilaiAkhir = ((mtk + indo + ipa)/3f).toString()
                onNilaiChange(nilaiAkhir)
            }
        }
        Column {
            Text(text = "Pilih Kelas")
            listOf("46-01", "46-02", "46-03", "46-04", "46-05").forEach { kelasOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onKelasChange(kelasOption)
                    }
                ) {
                    RadioButton(
                        selected = kelas == kelasOption,
                        onClick = {
                            onKelasChange(kelasOption)
                        }
                    )
                    Text(text = kelasOption)
                }
            }
        }
    }

    }
@Composable
fun DeleteById(delete: () -> Unit){
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(id = R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.hapus)) }, onClick = {
                expanded = false
                delete() })
        }
    }
}
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assessment2Theme {
        DetailScreen(rememberNavController())
    }
}