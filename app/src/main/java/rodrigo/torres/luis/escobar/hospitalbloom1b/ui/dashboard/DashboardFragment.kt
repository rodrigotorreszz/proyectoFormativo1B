package rodrigo.torres.luis.escobar.hospitalbloom1b.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        btnAgregar.setOnClickListener {
            showCustomDialog()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
