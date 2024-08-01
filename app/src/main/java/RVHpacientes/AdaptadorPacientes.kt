package RVHpacientes

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import modelo.*
import rodrigo.torres.luis.escobar.hospitalbloom1b.R
import rodrigo.torres.luis.escobar.hospitalbloom1b.informacionPaciente
import java.util.*

class AdaptadorPacientes(var datos: List<tbPacientes>) : RecyclerView.Adapter<ViewHolderPacientes>() {

    fun actualizarListado() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val statement = objConexion?.createStatement()
                val resultSet = statement?.executeQuery("SELECT * FROM paciente")!!

                val nuevosDatos = mutableListOf<tbPacientes>()
                while (resultSet.next()) {
                    val paciente = tbPacientes(
                        resultSet.getInt("idPaciente"),
                        resultSet.getString("nombres"),
                        resultSet.getInt("idTipoSangre"),
                        resultSet.getString("telefono"),
                        resultSet.getInt("idHabitacion"),
                        resultSet.getString("fechaNacimiento"),
                        resultSet.getInt("idEnfermedad"),
                        resultSet.getString("horaAplicacion"),
                        resultSet.getInt("idMedicamento")
                    )
                    nuevosDatos.add(paciente)
                }

                withContext(Dispatchers.Main) {
                    datos = nuevosDatos
                    notifyDataSetChanged()
                }
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }

    fun eliminarPaciente(idPaciente: Int, context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val eliminarPaciente = objConexion?.prepareStatement("DELETE FROM paciente WHERE idPaciente = ?")!!
                eliminarPaciente.setInt(1, idPaciente)
                eliminarPaciente.executeUpdate()

                objConexion.prepareStatement("commit").executeUpdate()

                withContext(Dispatchers.Main) {
                    showDialogPacienteEliminado(context)
                    actualizarListado()
                }
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }

    fun actualizarPaciente(
        nuevoNombre: String, nuevoTelefono: String, nuevaFecha: String, nuevaHora: String,
        nuevoMedicamento: Int, nuevaHabitacion: Int, nuevaSangre: Int, nuevaEnfermedad: Int, idPaciente: Int,
        context: Context
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val actualizarPacientes = objConexion?.prepareStatement(
                    "UPDATE paciente SET nombres = ?, idTipoSangre = ?, telefono = ?, idHabitacion = ?, fechaNacimiento = ?, idEnfermedad = ?, horaAplicacion = ?, idMedicamento = ? WHERE idPaciente = ?"
                )!!

                actualizarPacientes.setString(1, nuevoNombre)
                actualizarPacientes.setInt(2, nuevaSangre)
                actualizarPacientes.setString(3, nuevoTelefono)
                actualizarPacientes.setInt(4, nuevaHabitacion)
                actualizarPacientes.setString(5, nuevaFecha)
                actualizarPacientes.setInt(6, nuevaEnfermedad)
                actualizarPacientes.setString(7, nuevaHora)
                actualizarPacientes.setInt(8, nuevoMedicamento)
                actualizarPacientes.setInt(9, idPaciente)
                actualizarPacientes.executeUpdate()

                objConexion.prepareStatement("commit").executeUpdate()

                withContext(Dispatchers.Main) {
                    showDialogPacienteEditado(context)
                    actualizarListado()
                }
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPacientes {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_paciente, parent, false)
        return ViewHolderPacientes(vista)
    }

    override fun getItemCount() = datos.size

    override fun onBindViewHolder(holder: ViewHolderPacientes, position: Int) {
        val item = datos[position]
        holder.txtNombrePaciente.text = item.nombres

        holder.cvPaciente.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                val nombreSangre = obtenerNombreSangre(item.idTipoSangre)
                val nombreEnfermedad = obtenerNombreEnfermedad(item.idEnfermedad)
                val nombreMedicamento = obtenerNombreMedicamento(item.idMedicamento)

                withContext(Dispatchers.Main) {
                    val context = holder.cvPaciente.context
                    val intentInfo = Intent(context, informacionPaciente::class.java)

                    intentInfo.putExtra("idPaciente", item.idPaciente.toString())
                    intentInfo.putExtra("nombres", item.nombres)
                    intentInfo.putExtra("tipoSangre", nombreSangre)
                    intentInfo.putExtra("telefono", item.telefono)
                    intentInfo.putExtra("habitacion", item.idHabitacion.toString())
                    intentInfo.putExtra("fechaNacimiento", item.fechaNacimiento)
                    intentInfo.putExtra("enfermedad", nombreEnfermedad)
                    intentInfo.putExtra("horaAplicacion", item.horaAplicacion)
                    intentInfo.putExtra("medicamento", nombreMedicamento)

                    context.startActivity(intentInfo)
                }
            }
        }


        holder.imvEliminar.setOnClickListener {
            showConfirmDeleteDialog(holder.itemView.context, item.idPaciente)
        }

        holder.imvEditar.setOnClickListener {
            val context = holder.cvPaciente.context
            val builder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_editar_paciente, null)

            val txtNombre = dialogView.findViewById<TextInputEditText>(R.id.txtNombreE)
            val txtTelefono = dialogView.findViewById<TextInputEditText>(R.id.txtTelefonoE)
            val txtFecha = dialogView.findViewById<TextInputEditText>(R.id.txtFechaE)
            val txtHora = dialogView.findViewById<TextInputEditText>(R.id.txtHoraE)
            val spMedicamentos = dialogView.findViewById<Spinner>(R.id.spMedicamentosE)
            val spHabitacion = dialogView.findViewById<Spinner>(R.id.spHabitacionE)
            val spSangre = dialogView.findViewById<Spinner>(R.id.spSangreE)
            val spEnfermedad = dialogView.findViewById<Spinner>(R.id.spEnfermedadE)
            val btnGuardar = dialogView.findViewById<Button>(R.id.btnEditarE)
            val btnCerrar = dialogView.findViewById<Button>(R.id.btnCerrarE)

            txtNombre.setText(item.nombres)
            txtTelefono.setText(item.telefono)
            txtFecha.setText(item.fechaNacimiento)
            txtHora.setText(item.horaAplicacion)

            var listaMedicamentos: List<tbMedicamentos> = emptyList()
            var listaHabitaciones: List<tbHabitaciones> = emptyList()
            var listaSangre: List<dtTipoSangre> = emptyList()
            var listaEnfermedades: List<tbEnfermedad> = emptyList()

            cargarDatosSpinner(context, spSangre, ::obtenerSangre, { it.tipoSangre }) { data ->
                listaSangre = data
            }
            cargarDatosSpinner(context, spEnfermedad, ::obtenerEnfermedad, { it.enfermedad }) { data ->
                listaEnfermedades = data
            }
            cargarDatosSpinner(context, spMedicamentos, ::obtenerMedicamentos, { it.medicamento }) { data ->
                listaMedicamentos = data
            }
            cargarDatosSpinner(context, spHabitacion, ::obtenerHabitaciones, { "Habitación ${it.idHabitacion}" }) { data ->
                listaHabitaciones = data
            }

            txtHora.setOnClickListener {
                showTimePickerDialog(txtHora)
            }

            txtFecha.setOnClickListener {
                showDatePickerDialog(txtFecha)
            }

            builder.setView(dialogView)
            val dialog = builder.create()

            btnGuardar.setOnClickListener {
                val nuevoNombre = txtNombre.text.toString()
                val nuevoTelefono = txtTelefono.text.toString()
                val nuevaFecha = txtFecha.text.toString()
                val nuevaHora = txtHora.text.toString()

                val nuevoMedicamento = listaMedicamentos[spMedicamentos.selectedItemPosition].idMedicamento
                val nuevaHabitacion = listaHabitaciones[spHabitacion.selectedItemPosition].idHabitacion
                val nuevaSangre = listaSangre[spSangre.selectedItemPosition].idTipoSangre
                val nuevaEnfermedad = listaEnfermedades[spEnfermedad.selectedItemPosition].idEnfermedad

                actualizarPaciente(
                    nuevoNombre,
                    nuevoTelefono,
                    nuevaFecha,
                    nuevaHora,
                    nuevoMedicamento,
                    nuevaHabitacion,
                    nuevaSangre,
                    nuevaEnfermedad,
                    item.idPaciente,
                    context
                )
                dialog.dismiss()
            }

            btnCerrar.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            editText.context, R.style.TimePicker,
            { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(time)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun obtenerSangre(): List<dtTipoSangre> {
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("select * from tipoSangre")!!

        val listadoSangre = mutableListOf<dtTipoSangre>()

        while (resultSet.next()) {
            listadoSangre.add(dtTipoSangre(resultSet.getInt("idTipoSangre"), resultSet.getString("tipoSangre")))
        }
        return listadoSangre
    }

    private fun obtenerEnfermedad(): List<tbEnfermedad> {
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("select * from enfermedad")!!

        val listadoEnfermedad = mutableListOf<tbEnfermedad>()

        while (resultSet.next()) {
            listadoEnfermedad.add(tbEnfermedad(resultSet.getInt("idEnfermedad"), resultSet.getString("enfermedad")))
        }
        return listadoEnfermedad
    }

    private fun obtenerMedicamentos(): List<tbMedicamentos> {
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("select * from medicamento")!!

        val listadoMedicamentos = mutableListOf<tbMedicamentos>()

        while (resultSet.next()) {
            listadoMedicamentos.add(tbMedicamentos(resultSet.getInt("idMedicamento"), resultSet.getString("nombre"), resultSet.getString("descripcion")))
        }
        return listadoMedicamentos
    }

    private fun obtenerHabitaciones(): List<tbHabitaciones> {
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("select * from habitaciones")!!

        val listadoHabitaciones = mutableListOf<tbHabitaciones>()

        while (resultSet.next()) {
            listadoHabitaciones.add(tbHabitaciones(resultSet.getInt("idHabitacion"), resultSet.getInt("idCama")))
        }
        return listadoHabitaciones
    }

    private fun <T> cargarDatosSpinner(
        context: Context,
        spinner: Spinner,
        obtenerDatos: () -> List<T>,
        obtenerTexto: (T) -> String,
        onLoaded: (List<T>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val listado = obtenerDatos()
            val textos = listado.map { obtenerTexto(it) }

            withContext(Dispatchers.Main) {
                val miAdaptador = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, textos)
                spinner.adapter = miAdaptador
                onLoaded(listado)
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            editText.context,
            R.style.datePickerTheme,
            { _, añoSeleccionado, mesSeleccionado, diaSeleccionado ->
                val fechaSeleccionada = "$diaSeleccionado/${mesSeleccionado + 1}/$añoSeleccionado"
                editText.setText(fechaSeleccionada)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun showDialogPacienteEditado(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_editado_correctamente)

        val btnClose = dialog.findViewById<Button>(R.id.btnDialogClose)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showConfirmDeleteDialog(context: Context, idPaciente: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_eliminar)

        val btnYes = dialog.findViewById<Button>(R.id.btnDialogYes)
        val btnNo = dialog.findViewById<Button>(R.id.btnDialogNo)

        btnYes.setOnClickListener {
            eliminarPaciente(idPaciente, context)
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialogPacienteEliminado(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_eliminado_correctamente)

        val btnClose = dialog.findViewById<Button>(R.id.btnDialogClose)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun obtenerNombreSangre(idTipoSangre: Int): String {
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.prepareStatement("SELECT tipoSangre FROM tipoSangre WHERE idTipoSangre = ?")
        statement?.setInt(1, idTipoSangre)
        val resultSet = statement?.executeQuery()
        return if (resultSet?.next() == true) {
            resultSet.getString("tipoSangre")
        } else {
            "Desconocido"
        }
    }



    private fun obtenerNombreEnfermedad(idEnfermedad: Int): String {
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.prepareStatement("SELECT enfermedad FROM enfermedad WHERE idEnfermedad = ?")
        statement?.setInt(1, idEnfermedad)
        val resultSet = statement?.executeQuery()
        return if (resultSet?.next() == true) {
            resultSet.getString("enfermedad")
        } else {
            "Desconocido"
        }
    }

    private fun obtenerNombreMedicamento(idMedicamento: Int): String {
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.prepareStatement("SELECT nombre FROM medicamento WHERE idMedicamento = ?")
        statement?.setInt(1, idMedicamento)
        val resultSet = statement?.executeQuery()
        return if (resultSet?.next() == true) {
            resultSet.getString("nombre")
        } else {
            "Desconocido"
        }
    }

}
