package RVHpacientes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import modelo.tbPacientes
import rodrigo.torres.luis.escobar.hospitalbloom1b.R

class AdaptadorPacientes(var Datos: List<tbPacientes>): RecyclerView.Adapter<ViewHolderPacientes>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPacientes {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_paciente, parent, false)
        return ViewHolderPacientes(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderPacientes, position: Int) {
        val item = Datos[position]
        holder.txtNombrePaciente.text = item.nombres
    }
}