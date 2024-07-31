package RVHpacientes

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import rodrigo.torres.luis.escobar.hospitalbloom1b.R

class ViewHolderPacientes(view: View): RecyclerView.ViewHolder(view) {
    val txtNombrePaciente = view.findViewById<TextView>(R.id.lblNombrePaciente)
    val cvPaciente = view.findViewById<CardView>(R.id.cvPaciente)
    val imvEditar = view.findViewById<LottieAnimationView>(R.id.imvEditar)
    val imvEliminar = view.findViewById<LottieAnimationView>(R.id.imvEliminar)
}