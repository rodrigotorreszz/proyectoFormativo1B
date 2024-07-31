package rodrigo.torres.luis.escobar.hospitalbloom1b.ui.home

import RVHpacientes.AdaptadorPacientes
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tbPacientes
import rodrigo.torres.luis.escobar.hospitalbloom1b.R
import rodrigo.torres.luis.escobar.hospitalbloom1b.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root
      val rcvPacientes = root.findViewById<RecyclerView>(R.id.rcvPacientes)
      rcvPacientes.layoutManager = LinearLayoutManager(requireContext())

      fun obtenerPacientes(): List<tbPacientes>{
          val objConexion = ClaseConexion().cadenaConexion()
          val statement = objConexion?.createStatement()
          val resulSet = statement?.executeQuery("select * from paciente")!!

          val listaPacientes = mutableListOf<tbPacientes>()

          while (resulSet.next()) {
              val idPaciente = resulSet.getInt("idPaciente")
              val nombres = resulSet.getString("nombres")
              val idTipoSangre = resulSet.getInt("idTipoSangre") // Cambiado a getInt
              val telefono = resulSet.getString("telefono")
              val idHabitacion = resulSet.getInt("idHabitacion")
              val fechaNacimiento = resulSet.getString("fechaNacimiento")
              val idEnfermedad = resulSet.getInt("idEnfermedad")
              val horaAplicacion = resulSet.getString("horaAplicacion") // Añadido
              val idMedicamento = resulSet.getInt("idMedicamento") // Añadido

              val pacienteRcv = tbPacientes(
                  idPaciente, nombres, idTipoSangre, telefono, idHabitacion,
                  fechaNacimiento, idEnfermedad, horaAplicacion, idMedicamento
              )
              listaPacientes.add(pacienteRcv)
          }
          return listaPacientes
      }

      CoroutineScope(Dispatchers.IO).launch {
          val pacientesDB = obtenerPacientes()
          withContext(Dispatchers.Main){
              val adapter = AdaptadorPacientes(pacientesDB)
              rcvPacientes.adapter = adapter
          }
      }

    return root
  }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}