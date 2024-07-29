package modelo

import RVHpacientes.AdaptadorPacientes

data class tbPacientes(
    val idPaciente: Int,
    val nombres: String,
    val idTipoSangre: String,
    val telefono: String,
    val enfermedad: String,
    val idHabitacion: Int,
    val fechaNacimiento: String
)
