package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {
            val url = "jdbc.oracle:thin:@192.168.1.31:1521:xe"
            val user = "hospitalbloom"
            val contrasena = "1234"

            val connection = DriverManager.getConnection(url, user, contrasena)
            return connection
        }catch (e: Exception){
            println("error: $e")
            return null
        }
    }
}