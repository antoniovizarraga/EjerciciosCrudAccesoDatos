package crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Metodos {

	/**
	 * Función que ejecuta cualquier operación de MySQL (Insertar/Crear, Actualizar
	 * y Borrar) que se le pase por parámetro. Si no logra conectar con la base de
	 * datos, devolverá un boolean indicando si se pudo hacer la conexión
	 * correctamente y pudo realizar la operación (true) o no (false). Si se diera
	 * el caso en que no puede conectarse a la base de datos o que la consulta SQL
	 * no es correcta, la función imprimirá por consola un String indicando el
	 * error.
	 * 
	 * @param sql La operación o consulta MySQL a ejecutar en la función.
	 * @return Un boolean indicando si se pudo ejecutar la consulta correctamente o
	 *         no.
	 */
	public static boolean conectar(String sql) {
		boolean res = false;
		Statement stmt = null;
		Connection connect = null;
		// JDBC driver name and database URL
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		final String DB_URL = "jdbc:mysql://dns11036.phdns11.es/ad2425_avizarraga";
		String USUARIO = "avizarraga";
		String CONTA = "12345";

		String use = "use ad2425_avizarraga;";

		try {
			connect = DriverManager.getConnection(DB_URL, USUARIO, CONTA);

			stmt = connect.createStatement();

			stmt.execute(use);
			stmt.execute(sql);

			res = true;
		} catch (SQLException e) {
			System.err.println("Error: " + e.toString());

		}

		return res;

	}

	/**
	 * Función que crea todas las tablas de la base de datos. Si se desea crear
	 * únicamente una tabla en concreto, se especifica el nombre de dicha tabla en
	 * el parámetro: "nombreTabla" pasado a la función. Si se desea crear todas las
	 * tablas, deje el parámetro: "nombreTabla" vacío. Devuelve una cadena indicando
	 * si se pudo crear la tabla (O las tablas) o si no se pudo conectar con la base
	 * de datos.
	 * 
	 * @param nombreTabla El nombre de la tabla a crear. Si quiere crear todas las
	 *                    tablas, deje el parámetro: "nombreTabla" vacío.
	 * @return Un String indicando si se pudo crear la tabla (O las tablas) o no,
	 *         indicando también el motivo del porqué no pudo conectarse.
	 */
	public static String crearTablas(String nombreTabla) {
		String res = "";

		String sql = "";

		String tabla = "";

		boolean connectionState = false;

		if (nombreTabla != null && !nombreTabla.equals("")) {
			tabla = nombreTabla.toLowerCase();
		}

		switch (tabla) {

		case "pacientes", "paciente":
			sql = "CREATE TABLE Pacientes(\r\n" + "idPaciente Int,\r\n" + "Nombre VarChar(45),\r\n"
					+ "NSS VarChar(45),\r\n" + "PRIMARY KEY (idPaciente)\r\n" + ");";
			res = "Tabla Pacientes creada correctamente";
			break;

		case "medicamentos", "medicamento":
			sql = "CREATE TABLE Medicamentos(\r\n" + "idMedicamento Int,\r\n" + "Composición VarChar(45),\r\n"
					+ "PRIMARY KEY (idMedicamento)\r\n" + ");";
			res = "Tabla Medicamentos creada correctamente";
			break;

		case "receta", "recetas":
			res = "La tabla: \"Receta\" no se puede crear debido a que contiene relaciones con tablas maestras (Pacientes y Medicamentos). ";
			break;

		default:

			sql = "CREATE TABLE Pacientes(\r\n" + "idPaciente Int,\r\n" + "Nombre VarChar(45),\r\n"
					+ "NSS VarChar(45),\r\n" + "PRIMARY KEY (idPaciente)\r\n" + ");";
			sql += "\r\n" + "CREATE TABLE Medicamentos(\r\n" + "idMedicamento Int,\r\n" + "Composición VarChar(45),\r\n"
					+ "PRIMARY KEY (idMedicamento)\r\n" + ");" + "";

			sql += "\r\n" + "CREATE TABLE Receta(\r\n" + "idReceta Int,\r\n" + "idPaciente Int,\r\n"
					+ "idMedicamento Int,\r\n" + "fechaFin DATE,\r\n"
					+ "FOREIGN KEY (idPaciente) REFERENCES Pacientes(idPaciente),\r\n"
					+ "FOREIGN KEY (idMedicamento) REFERENCES Medicamentos(idMedicamento),\r\n"
					+ "PRIMARY KEY (idReceta)\r\n" + ");";
			res = "Creación de todas las tablas realizada correctamente.";

		}

		if (!sql.equals("")) {
			connectionState = conectar(sql);
		}

		if (!connectionState) {
			res = "No se pudo realizar la conexión con la base de datos";
		}

		return res;
	}
	
		
	
	

}
