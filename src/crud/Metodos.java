package crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Metodos {

	private static final String USUARIO = "avizarraga";
	private static final String CONTA = "12345";
	private static final String DB_URL = "jdbc:mysql://dns11036.phdns11.es/ad2425_avizarraga";
	private static final String USE = "use ad2425_avizarraga;";

	/**
	 * Función que obtiene la última Id de una tabla pasada por parámetro y la
	 * devuelve. Si no encuentra una Id porque la tabla está vacía, te devuelve un
	 * entero con valor 0 o te imprime en pantalla/terminal el error.
	 * 
	 * @param tableName El nombre de la tabla sobre la cuál quieres saber la última
	 *                  Id
	 * @return un Int con la última Id de la tabla.
	 */
	private static int obtenerUltimaId(String tableName) {

		int res = 0;

		String tablaMinus = tableName.toLowerCase();
		String id = "";

		switch (tablaMinus) {

		case "paciente", "pacientes":
			id = "idPaciente";
			break;
		case "medicamentos", "medicamento":
			id = "idMedicamento";
			break;
		case "receta", "recetas":
			id = "idReceta";
			break;

		default:
			id = "id";

		}

		String consulta = "SELECT " + id + " FROM " + tableName + " ORDER BY " + id + " DESC LIMIT 1";

		ResultSet consultaRes = ejecutarConsulta(consulta);

		try {
			consultaRes.next();

			res = consultaRes.getInt(1);

		} catch (SQLException e) {
			System.err.println("Error: " + e.toString());
		} finally {
			try {
				consultaRes.close();
			} catch (SQLException e) {
				System.err.println("Error: " + e.toString());
			}
		}

		return res;

	}

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
	private static boolean ejecutarComando(String sql) {
		boolean res = false;
		Statement stmt = null;
		Connection connect = null;
		try {
			connect = DriverManager.getConnection(DB_URL, USUARIO, CONTA);

			stmt = connect.createStatement();

			stmt.execute(USE);
			stmt.execute(sql);

			res = true;
		} catch (SQLException e) {
			System.err.println("Error: " + e.toString());

		}

		return res;

	}

	/**
	 * Función que se encarga de ejecutar una consulta en MySQL pasada por parámetro
	 * y te devuelve un ResultSet con todas las filas que devuelva la base de datos.
	 * 
	 * @param sql La consulta a ejecutar en la base de datos
	 * @return Un ResultSet que contiene todas las filas que devuelve la consulta.
	 *         Para leer su contenido, tendrás que hacer un bucle while y usar el
	 *         método next() para pasar de una fila a otra. Inicialmente empieza en
	 *         0 (Una fila vacía), y a cada next que hagas irá pasando de fila en
	 *         fila. Luego con métodos como: getInt(numeroColumna),
	 *         getString(numeroColumna)... Etc irás recogiendo los valores de sus
	 *         respectivas columnas con sus respectivos tipos primitivos. Los
	 *         valores de: "numeroColumna" empiezan inicialmente en 1, por lo que la
	 *         primera columna de una fila es 1.
	 */
	private static ResultSet ejecutarConsulta(String sql) {

		Connection connect = null;

		ResultSet resultado = null;

		try {
			connect = DriverManager.getConnection(DB_URL, USUARIO, CONTA);

			// Paso 1 establecer conexión + pasar la consulta
			PreparedStatement stat = connect.prepareStatement(sql);

			// Paso 2. Definir resultSet y ejecutar la consulta
			resultado = stat.executeQuery();
		} catch (SQLException e) {
			System.err.println("Error: " + e.toString());
		}

		return resultado;
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

		boolean existePacientes = ejecutarComando("SELECT * FROM Pacientes LIMIT 1");
		boolean existeMedicamentos = ejecutarComando("SELECT * FROM Medicamentos LIMIT 1");

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
			if (!existePacientes && !existeMedicamentos) {
				res = "La tabla: \"Receta\" no se puede crear debido a que contiene relaciones con tablas maestras (Pacientes y Medicamentos). ";
			} else {
				sql = "\r\n" + "CREATE TABLE Receta(\r\n" + "idReceta Int,\r\n" + "idPaciente Int,\r\n"
						+ "idMedicamento Int,\r\n" + "fechaFin DATE,\r\n"
						+ "FOREIGN KEY (idPaciente) REFERENCES Pacientes(idPaciente),\r\n"
						+ "FOREIGN KEY (idMedicamento) REFERENCES Medicamentos(idMedicamento),\r\n"
						+ "PRIMARY KEY (idReceta)\r\n" + ");";
				res = "Tabla Receta creada correctamente";
			}

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
			connectionState = ejecutarComando(sql);
		}

		if (!connectionState) {
			res = "No se pudo realizar la conexión con la base de datos";
		}

		return res;
	}

	public static String insertarDatos(String tabla, String datosTabla) {
		String res = "";

		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO " + tabla + " ");

		String[] datos = datosTabla.split("\n");

		String tablaMinus = tabla.toLowerCase();

		
		
		

		switch (tablaMinus) {

		case "pacientes", "paciente":

			sql.append("( Nombre, ");

			for (String campo : datos) {

			}
			break;

		}

		return res;
	}

}
