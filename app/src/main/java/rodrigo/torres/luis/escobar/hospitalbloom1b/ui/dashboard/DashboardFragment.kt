package rodrigo.torres.luis.escobar.hospitalbloom1b.ui.dashboard

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Space
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tbHabitaciones
import modelo.tbMedicamentos
import rodrigo.torres.luis.escobar.hospitalbloom1b.R
import rodrigo.torres.luis.escobar.hospitalbloom1b.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnAgregar = root.findViewById<Button>(R.id.btnAgregar)
        val txtFecha = root.findViewById<EditText>(R.id.txtFecha)
        val spMedicamentos = root.findViewById<Spinner>(R.id.spMedicamentos)
        val spHabitaciones = root.findViewById<Spinner>(R.id.spHabitacion)

        fun ObtenerMedicamentos(): List<tbMedicamentos>{
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from medicamento")!!

            val listadoMedicamentos = mutableListOf<tbMedicamentos>()

            while (resultSet.next()){
                val idMedicamento = resultSet.getInt("idMedicamento")
                val nombre = resultSet.getString("nombre")
                val descripcion = resultSet.getString("descripcion")
                val medicamentoCompleto = tbMedicamentos(idMedicamento, nombre, descripcion)
                listadoMedicamentos.add(medicamentoCompleto)
            }
            return listadoMedicamentos
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listadoDeMedicamentos = ObtenerMedicamentos()
            val nombreMedicamentos = listadoDeMedicamentos.map { it.medicamento }

            withContext(Dispatchers.Main){
                val miAdaptador =ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombreMedicamentos)
                spMedicamentos.adapter = miAdaptador
            }
        }


        fun ObtenerHabitaciones(): List<tbHabitaciones>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from habitaciones")!!
            val listadoHabitaciones = mutableListOf<tbHabitaciones>()

            while (resultSet.next()){
                val idHabitacion = resultSet.getInt("idHabitacion")
                val idCama = resultSet.getInt("idCama")
                val habitacioCompleta = tbHabitaciones(idHabitacion, idCama)
                listadoHabitaciones.add(habitacioCompleta)
            }
            return listadoHabitaciones
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listadoDeHabitaciones = ObtenerHabitaciones()
            val idHabitacion = listadoDeHabitaciones.map { it.idHabitacion }

            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, idHabitacion)
                spHabitaciones.adapter = miAdaptador
            }
        }


        txtFecha.setOnClickListener {
            showDatePickerDialog(txtFecha)
        }
        btnAgregar.setOnClickListener {

            try {
                CoroutineScope(Dispatchers.IO).launch {
                        //Aqui va el insert
                }
            }catch (ex: Exception){
                println(ex.message)
            }
        }



        return root

    }

    private fun showCustomDialog() {
        CoroutineScope(Dispatchers.Main).launch {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_paciente_agregado)

            val btnClose = dialog.findViewById<Button>(R.id.btnDialogClose)
            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),R.style.datePickerTheme,{ _, añoSeleccionado, mesSeleccionado, diaSeleccionado ->

            val fechaSeleccionada = "$diaSeleccionado/${mesSeleccionado}/$añoSeleccionado"
            editText.setText(fechaSeleccionada)
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
