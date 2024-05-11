import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0112.mobpro1.R
import org.d3if0112.mobpro1.database.KaryawanDb
import org.d3if0112.mobpro1.ui.screen.DetailViewModel
import org.d3if0112.mobpro1.ui.screen.DisplayAlertDialog
import org.d3if0112.mobpro1.ui.theme.Mobpro1Theme
import org.d3if0112.mobpro1.util.ViewModelFactory

private val <E> List<E>?.id: Any
    get() {
        TODO("Not yet implemented")
    }
const val KEY_ID_KARYAWAN = "idKaryawan"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = KaryawanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var namaKaryawan by remember { mutableStateOf("") }
    var nomorKaryawan by remember { mutableStateOf("") }
    val posisiRadio = listOf(
        stringResource(id = R.string.dokter),
        stringResource(id = R.string.suster),
        stringResource(id = R.string.administrasi),
        stringResource(id = R.string.apoteker),
        stringResource(id = R.string.cs)
    )
    var posisiKaryawan by remember { mutableStateOf(posisiRadio[0]) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true){
        if (id == null) return@LaunchedEffect
        val data = viewModel.getCatatan(id) ?: return@LaunchedEffect
        namaKaryawan = data.name
        nomorKaryawan = data.noKaryawan
        posisiKaryawan = data.posisi
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon =
                {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary)
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_karyawan))
                    else
                        Text(text = stringResource(id = R.string.edit_karyawan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (namaKaryawan == "" || nomorKaryawan == "" || posisiKaryawan == ""){
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null){
                            viewModel.insert(namaKaryawan, nomorKaryawan, posisiKaryawan)
                        } else {
                            viewModel.update(id, namaKaryawan, nomorKaryawan, posisiKaryawan)
                        }
                        navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary)
                    }
                    if (id != null){
                        DeleteAction { showDialog = true}
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormCatatan(
            nama = namaKaryawan,
            onNamaChange = { namaKaryawan = it },
            nomorKaryawan = nomorKaryawan,
            onNomorKaryawanChange = { nomorKaryawan = it },
            posisiRadio = posisiRadio,
            posisiKaryawan = posisiKaryawan,
            posisiSelected = { posisiKaryawan = it },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit){
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true}) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
           DropdownMenuItem(
               text = {
                      Text(text = stringResource(id = R.string.hapus))
                      },
               onClick = {
                   expanded = false
                   delete()
               }
           )
        }
    }
}

@Composable
fun FormCatatan(
    nama: String, onNamaChange: (String) -> Unit,
    nomorKaryawan: String, onNomorKaryawanChange: (String) -> Unit,
    posisiRadio: List<String>,
    posisiKaryawan: String, posisiSelected: (String) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = { onNamaChange(it) },
            label = { Text(text = stringResource(id = R.string.nama)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nomorKaryawan,
            onValueChange = {onNomorKaryawanChange(it)},
            label = { Text(text = stringResource(id = R.string.noPegawai)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(5.dp))
                .padding(16.dp, 0.dp)
        ) {
            posisiRadio.forEachIndexed { index, k ->
                tombolRadio(label = k, isSelected = posisiKaryawan == k,
                    modifier = Modifier
                        .selectable(
                            selected = posisiKaryawan == k,
                            onClick = { posisiSelected(posisiRadio[index]) },
                            role = Role.RadioButton
                        )
                        .padding(0.dp, 16.dp)
                )
            }
        }
    }
}

@Composable
fun tombolRadio(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier
    ) {
        RadioButton(selected= isSelected, onClick = null)
        Text(text = label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 8.dp))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Mobpro1Theme {
        DetailScreen(rememberNavController())
    }
}
