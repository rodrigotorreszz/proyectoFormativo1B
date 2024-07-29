package RVHpacientes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rodrigo.torres.luis.escobar.hospitalbloom1b.R

class ViewHolderPacientes(view: View): RecyclerView.ViewHolder(view) {
    val txtNombrePaciente = view.findViewById<TextView>(R.id.lblNombrePaciente)
}