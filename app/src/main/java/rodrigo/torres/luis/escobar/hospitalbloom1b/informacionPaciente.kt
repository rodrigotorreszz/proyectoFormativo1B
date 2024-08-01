package rodrigo.torres.luis.escobar.hospitalbloom1b

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class informacionPaciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_informacion_paciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val idPaciente = intent.getStringExtra("idPaciente")
        val nombres = intent.getStringExtra("nombres")
        val tipoSangre = intent.getStringExtra("tipoSangre")
        val telefono = intent.getStringExtra("telefono")
        val habitacion = intent.getStringExtra("habitacion")
        val fechaNacimiento = intent.getStringExtra("fechaNacimiento")
        val enfermedad = intent.getStringExtra("enfermedad")
        val horaAplicacion = intent.getStringExtra("horaAplicacion")
        val medicamento = intent.getStringExtra("medicamento")

        val lblPaciente = findViewById<TextView>(R.id.lblIdPaciente)
        val txtNombre = findViewById<TextView>(R.id.txtNombreInfo)
        val txtSangre = findViewById<TextView>(R.id.txtSangre)
        val txtTelefono = findViewById<TextView>(R.id.lblTelefon)
        val txtHabitacion = findViewById<TextView>(R.id.txtHabitacion)
        val txtEnfermedad = findViewById<TextView>(R.id.txtEnfermedad)
        val txtHora =  findViewById<TextView>(R.id.txtHoraInfo)
        val txtMedicamento = findViewById<TextView>(R.id.txtMedicamento)
        val txtFecha = findViewById<TextView>(R.id.lblFecha)
        val btnCerrar = findViewById<Button>(R.id.btnDialogClose)
        lblPaciente.text = idPaciente
        txtNombre.text = nombres
        txtSangre.text = tipoSangre
        txtTelefono.text = telefono
        txtHabitacion.text = habitacion
        txtFecha.text = fechaNacimiento
        txtEnfermedad.text = enfermedad
        txtHora.text = horaAplicacion
        txtMedicamento.text = medicamento
        btnCerrar.setOnClickListener{
            finish()
        }
    }
}