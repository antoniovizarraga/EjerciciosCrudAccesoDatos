package crud;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.Scanner;



public class Metodos {

	private static final String USUARIO = "avizarraga";
	private static final String CONTA = "12345";
	private static final String DB_URL = "jdbc:mysql://dns11036.phdns11.es/ad2425_avizarraga";
	private static final String USE = "use ad2425_avizarraga;";
	public static Scanner sc = new Scanner(System.in);
	private static Statement stmt = null;
	private static Connection connect = null;
	
	
	private static void inicio() {
		try {
			connect = DriverManager.getConnection(DB_URL, USUARIO, CONTA);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
			while (consultaRes.next()) {
				res = consultaRes.getInt(1);
			}

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

	private static boolean confirmarOperacion(Connection connect) {
		boolean res = false;

		return res;
	}

	/**
	 * Función que ejecuta cualquier operación de MySQL (Insertar/Crear, Actualizar
	 * y Borrar) que se le pase por parámetro, con opción a hacerlo con una
	 * transacción (Boolean). Si no logra conectar con la base de datos, devolverá
	 * un boolean indicando si se pudo hacer la conexión correctamente y pudo
	 * realizar la operación (true) o no (false). Si se diera el caso en que no
	 * puede conectarse a la base de datos o que la consulta SQL no es correcta, la
	 * función imprimirá por consola un String indicando el error.
	 * 
	 * @param sql         La operación o consulta MySQL a ejecutar en la función.
	 * @param transaction Booleano que indica si la consulta de MySQL que se quiere
	 *                    realizar se desea hacer en una transacción. Si es true,
	 *                    preguntará al usuario si quiere confirmar la operación o
	 *                    no. Si es false, ejecutará la consulta sin ninguna
	 *                    transacción. Imprimirá por consola un mensaje, indicando
	 *                    al usuario si desea realizar los cambios o no.
	 * @return Un boolean indicando si se pudo ejecutar la consulta correctamente o
	 *         no.
	 */
	private static boolean ejecutarComando(String sql, boolean transaction, Scanner obj) {
		boolean res = false;
		

		String userInput = "";

		

		try {
			connect = DriverManager.getConnection(DB_URL, USUARIO, CONTA);
			connect.setAutoCommit(!transaction);

			stmt = connect.createStatement();

			stmt.execute(USE);
			
			

			stmt.execute(sql);

			if (transaction) {

				System.out.println("¿Está seguro de que quiere realizar los cambios?" + "\n"
						+ "Una vez aplicados los cambios, no podrán deshacerse." + "\n"
						+ "Escriba: \"Si\" para aplicar los cambios o: \"No\" para deshacerlos.");

				userInput = obj.nextLine();

				userInput = userInput.replace("í", "i");

				if (userInput.equalsIgnoreCase("si")) {
					connect.commit();
					System.out.println("Cambios aplicados.");
				} else {
					connect.rollback();
					System.out.println("Cambios deshechos.");
				}

			}

			

			res = true;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			if (transaction) {
				try {
					connect.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

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
	
	public static ResultSet listarTablas(String tabla, String condicion) {
		ResultSet res = null;
		
		String nomTabla = "";
		
		String sql = "";
		
		if(tabla != null && !tabla.equals("")) {
			
			nomTabla = tabla.toLowerCase();
			
			switch(nomTabla) {
			
			case "paciente", "pacientes":
				if(condicion != null && !condicion.equals("")) {
					sql = "SELECT * FROM Pacientes WHERE " + condicion + ";";
				} else {
					sql = "SELECT * FROM Pacientes;";
				}
				
				break;
			case "medicamento", "medicamentos":
				if(condicion != null && !condicion.equals("")) {
					sql = "SELECT * FROM Medicamentos WHERE " + condicion + ";";
				} else {
					sql = "SELECT * FROM Medicamentos;";
				}
				
				break;
			case "receta", "recetas":
				if(condicion != null && !condicion.equals("")) {
					sql = "SELECT * FROM Receta WHERE " + condicion + ";";
				} else {
					sql = "SELECT * FROM Receta;";
				}
				break;
			
			}
		} 
		
		res = ejecutarConsulta(sql);
		
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

		boolean existePacientes = ejecutarComando("SELECT * FROM Pacientes LIMIT 1", false, sc);
		boolean existeMedicamentos = ejecutarComando("SELECT * FROM Medicamentos LIMIT 1", false, sc);

		boolean connectionState = false;

		if (nombreTabla != null && !nombreTabla.equals("")) {
			tabla = nombreTabla.toLowerCase();
		}

		switch (tabla) {

		case "pacientes", "paciente":
			sql = "CREATE TABLE Pacientes(\r\n" + "idPaciente Int NOT NULL AUTO_INCREMENT,\r\n"
					+ "Nombre VarChar(45),\r\n" + "Apellidos VarChar(45),\r\n" + "NSS VarChar(45),\r\n" + "PRIMARY KEY (idPaciente)\r\n" + ");";
			res = "Tabla Pacientes creada correctamente";
			break;

		case "medicamentos", "medicamento":
			sql = "CREATE TABLE Medicamentos(\r\n" + "idMedicamento Int NOT NULL AUTO_INCREMENT,\r\n"
					+ "Composición VarChar(45),\r\n" + "PRIMARY KEY (idMedicamento)\r\n" + ");";
			res = "Tabla Medicamentos creada correctamente";
			break;

		case "receta", "recetas":
			if (!existePacientes && !existeMedicamentos) {
				res = "La tabla: \"Receta\" no se puede crear debido a que contiene relaciones con tablas maestras (Pacientes y Medicamentos). ";
			} else {
				sql = "\r\n" + "CREATE TABLE Receta(\r\n" + "idReceta Int NOT NULL AUTO_INCREMENT,\r\n"
						+ "idPaciente Int,\r\n" + "idMedicamento Int,\r\n" + "fechaFin DATE,\r\n"
						+ "FOREIGN KEY (idPaciente) REFERENCES Pacientes(idPaciente) ON DELETE CASCADE,\r\n"
						+ "FOREIGN KEY (idMedicamento) REFERENCES Medicamentos(idMedicamento) ON DELETE CASCADE,\r\n"
						+ "PRIMARY KEY (idReceta)\r\n" + ");";
				res = "Tabla Receta creada correctamente";
			}

			break;

		default:

			sql = "CREATE TABLE Pacientes(\r\n" + "idPaciente Int NOT NULL AUTO_INCREMENT,\r\n"
					+ "Nombre VarChar(45),\r\n" + "NSS VarChar(45),\r\n" + "PRIMARY KEY (idPaciente)\r\n" + ");";
			sql += "\r\n" + "CREATE TABLE Medicamentos(\r\n" + "idMedicamento Int NOT NULL AUTO_INCREMENT,\r\n"
					+ "Composición VarChar(45),\r\n" + "PRIMARY KEY (idMedicamento)\r\n" + ");" + "";

			sql += "\r\n" + "CREATE TABLE Receta(\r\n" + "idReceta Int NOT NULL AUTO_INCREMENT,\r\n"
					+ "idPaciente Int,\r\n" + "idMedicamento Int,\r\n" + "fechaFin DATE,\r\n"
					+ "FOREIGN KEY (idPaciente) REFERENCES Pacientes(idPaciente) ON DELETE CASCADE,\r\n"
					+ "FOREIGN KEY (idMedicamento) REFERENCES Medicamentos(idMedicamento) ON DELETE CASCADE,\r\n"
					+ "PRIMARY KEY (idReceta)\r\n" + ");";
			res = "Creación de todas las tablas realizada correctamente.";

		}

		if (!sql.equals("")) {
			connectionState = ejecutarComando(sql, false, sc);
		}

		if (!connectionState) {
			res = "No se pudo realizar la conexión con la base de datos";
		}

		return res;
	}

	/**
	 * Función que inserta datos en la tabla que se le indique. Tanto la tabla como
	 * los datos a insertar se pasan como parámetros (String) en la función. Luego
	 * devuelve una cadena indicando si se pudo realizar la operación con éxito o
	 * no. Siempre devuelve una cadena, nunca estará vacía ni será null. Los datos a
	 * insertar en la tabla tienen que contener únicamente el valor que se quiera
	 * insertar en la tabla, cada valor separado por un intro (\n).
	 * 
	 * @param tabla      El nombre de la tabla donde insertar los datos.
	 * @param datosTabla Los datos a insertar en la tabla. Cada valor a insertar en
	 *                   la tabla debe estar separado por un intro.
	 * @return Un String indicando si se pudo realizar la inserción o no.
	 */
	public static String insertarDatos(String tabla, String datosTabla) {
		String res = "Inserción de datos correcta.";

		StringBuilder sql = new StringBuilder();

		sql.append("INSERT INTO ");

		String[] datos = datosTabla.split("\n");

		String tablaMinus = tabla.toLowerCase();

		boolean connectionState = false;

		switch (tablaMinus) {

		case "pacientes", "paciente":

			sql.append("Pacientes ( Nombre, Apellidos, NSS ) VALUES (");

			for (String campo : datos) {
				sql.append("'" + campo + "',");
			}

			sql.deleteCharAt(sql.length() - 1);

			sql.append(");");

			break;

		case "medicamentos", "medicamento":

			sql.append("Medicamentos ( Composición ) VALUES (" + datosTabla + ");");

			break;

		case "receta", "recetas":

			sql.append("Receta ( idPaciente, idMedicamento, fechaFin ) VALUES (");

			for (String campo : datos) {

				sql.append("'" + campo + "',");
			}

			sql.deleteCharAt(sql.length() - 1);

			sql.append(");");

			break;

		}

		System.out.println(sql);
		
		if (!sql.toString().equalsIgnoreCase("")) {
			
			connectionState = ejecutarComando(sql.toString(), false, sc);
		}

		if (!connectionState) {
			res = "No se pudo realizar la conexión con la base de datos";
		}

		return res;
	}

	/**
	 * Función que obtiene y devuelve como String el nombre de un paciente pasando
	 * la Id (Int) de dicho paciente por parámetro. Si no encuentra el paciente,
	 * devuelve una cadena vacía. Si ocurre un error en la consulta, imprime un
	 * mensaje de error indicando el porqué de dicho error.
	 * 
	 * @param id El id del paciente que se desea obtener su nombre. Es de tipo int.
	 * @return Un String indicando el nombre del paciente. Si no lo encuentra,
	 *         devuelve una cadena vacía.
	 */
	public static String obtenerNombrePacientePorId(int id) {
		String res = "";

		String sql = "SELECT Nombre FROM Pacientes WHERE idPaciente = " + id + ";";

		ResultSet resultado = ejecutarConsulta(sql);

		try {
			while (resultado.next()) {
				res = resultado.getString("Nombre");
			}

		} catch (SQLException ex) {
			System.err.println("Error ejecutando la consulta: " + sql);
			System.err.println("Detalle del error: " + ex.getMessage());
		}

		return res;
	}

	public static String modificarDatosTabla(String tabla, String columna, String datos, String condicion) {
		String res = "";
		String sql = "";
		String sqlConsulta = "";
		
		if(condicion != null && !condicion.equals("")) {
			sqlConsulta = "SELECT * " + "FROM " + tabla + " WHERE " + condicion + ";";
			sql = "UPDATE " + tabla + "SET" + columna + " = " + datos + " WHERE " + condicion
					+ ";";
		} else {
			sqlConsulta = "SELECT * FROM " + tabla + ";";
			sql = "UPDATE " + tabla + "SET" + columna + " = " + datos + ";";
		}
		
		 

		ResultSet datosOriginales;

		

		try {

			datosOriginales = ejecutarConsulta(sqlConsulta);

			System.out.println("Datos antes de la modificación: ");

			while (datosOriginales.next()) {
				System.out.println(columna + ": " + datosOriginales.getString(columna));
			}
		} catch (SQLException ex) {
			System.out.println("Error al obtener los datos originales: " + ex.getMessage());
		}

		System.out.println("Datos después de la modificación: ");

		System.out.println(columna + ": " + datos);

		boolean state = ejecutarComando(sql, true, sc);

		if (!state) {
			res = "Error: No se pudo actualizar la tabla " + tabla;

		} else {

			res = "Los datos se actualizaron correctamente.";
		}

		return res;
	}

	/**
	 * Función que devuelve la cantidad de tablas que hay en la base de datos.
	 * 
	 * @return Un int que devuelve cuántas tablas existen en la Base de datos.
	 *         Devuelve 0 si no encuentra ninguna.
	 */
	public static int cuantasTablas() {

		int res = 0;

		ResultSet cuantasTablas = ejecutarConsulta("SELECT count(*) FROM information_schema.tables;");

		try {
			cuantasTablas.next();
			res = cuantasTablas.getInt(1);
		} catch (SQLException e) {
			System.err.println("Error al intentar obtener la cuenta de las tablas en la base de datos.");
			System.err.println("Causa: " + e.getMessage());
		}

		return res;
	}
	
	public static boolean existenTablas(String nomTabla) {
		
		boolean tExists = false;
		
		inicio();
		
	    try (ResultSet rs = connect.getMetaData().getTables(null, null, nomTabla, null)) {
	        while (rs.next()) { 
	            String tName = rs.getString("TABLE_NAME");
	            if (tName != null && tName.equals(nomTabla)) {
	                tExists = true;
	                break;
	            }
	        }
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return tExists;
	}

	public static boolean borrarDatos(String tabla, String condicion) {
		boolean res = false;

		String sql = "";

		String nomTabla = tabla.toLowerCase();

		ResultSet datosOriginales = null;

		int cantidadTablas = cuantasTablas();

		if (condicion != null && !condicion.equals("")) {
			sql = "DELETE FROM " + tabla + " WHERE " + condicion + ";";
			datosOriginales = ejecutarConsulta("SELECT * FROM " + tabla + " WHERE " + condicion + ";");

		} else if (condicion.equals("")) {
			sql = "TRUNCATE " + tabla + ";";
			datosOriginales = ejecutarConsulta("SELECT * FROM " + tabla + ";");
		}

		System.out.println("Datos antes de la modificación: ");

		try {

			switch (nomTabla) {

			case "paciente", "pacientes":
				while (datosOriginales.next()) {
					System.out.println("ID Paciente: " + datosOriginales.getInt(1));
					System.out.println("Nombre: " + datosOriginales.getString(2));
					System.out.println("Apellidos: " + datosOriginales.getString(3));
					System.out.println("NSS: " + datosOriginales.getString(4));
				}
				break;
			case "medicamentos", "medicamento":
				while (datosOriginales.next()) {
					System.out.println("ID Medicamento: " + datosOriginales.getInt(1));
					System.out.println("Composición: " + datosOriginales.getString(2));
				}
				break;

			case "receta", "recetas":
				while (datosOriginales.next()) {
					System.out.println("ID Receta: " + datosOriginales.getInt(1));
					System.out.println("ID Paciente: " + datosOriginales.getInt(2));
					System.out.println("ID Medicamento: " + datosOriginales.getInt(3));
					System.out.println("Fecha Fin: " + datosOriginales.getDate(4));
					nomTabla = "receta";
				}
				break;

			}
		} catch (SQLException e) {
			System.err.println("Error al procesar los datos de la tabla: " + tabla);
			System.err.println("Causa: " + e.getMessage());

		}

		if (cantidadTablas > 1 && nomTabla.equalsIgnoreCase("receta")) {
			System.out.println("No se puede borrar la tabla: \"" + tabla
					+ "\" porque hace referencia a otras tablas (Foreign Keys).");
		} else {
			res = ejecutarComando(sql, false, sc);
		}

		return res;
	}

	public static boolean eliminarTablas(String tabla) {
		boolean res = false;

		String nomTabla = "";
		
		int cantidadTablas = cuantasTablas();

		if (tabla != null && !tabla.equals("")) {

			nomTabla = tabla.toLowerCase();

			switch (nomTabla) {
			case "paciente", "pacientes":
				System.out.println("¡ATENCIÓN! Vas a borrar la tabla: \"" + tabla + "\". Con todos sus datos incluidos.");
				ejecutarComando("SET FOREIGN_KEY_CHECKS=0;", false, sc);
				res = ejecutarComando("DROP TABLE Pacientes;", true, sc);
				System.out.println("Tabla " + "\"" + tabla + "\" borrada correctamente.");
				res = ejecutarComando("SET FOREIGN_KEY_CHECKS=1;", false, sc);
				break;

			case "medicamento", "medicamentos":
				System.out.println("¡ATENCIÓN! Vas a borrar la tabla: \"" + tabla + "\". Con todos sus datos incluidos.");
				res = ejecutarComando("SET FOREIGN_KEY_CHECKS=0; DROP TABLE Medicamentos;", true, sc);
				System.out.println("Tabla " + "\"" + tabla + "\" borrada correctamente.");
				res = ejecutarComando("SET FOREIGN_KEY_CHECKS=1;", false, sc);
				break;

			case "receta", "recetas":
				
				if(existenTablas("Pacientes") && existenTablas("Medicamentos")) {
					System.out.println("No se puede borrar la tabla: \"" + tabla
					+ "\" porque hace referencia a otras tablas (Foreign Keys).");
				} else {
					System.out.println("¡ATENCIÓN! Vas a borrar la tabla: \"" + tabla + "\". Con todos sus datos incluidos.");
					res = ejecutarComando("SET FOREIGN_KEY_CHECKS=0; DROP TABLE Receta;", true, sc);
					System.out.println("Tabla " + "\"" + tabla + "\" borrada correctamente.");
					res = ejecutarComando("SET FOREIGN_KEY_CHECKS=1;", false, sc);
				}
				
				break;
			}
		} else if(tabla != null && tabla.equals("")) {
			System.out.println("¡ATENCIÓN! Vas a borrar TODAS las tablas de la base de datos. Con todos sus datos incluidos.");
			ejecutarComando("SET FOREIGN_KEY_CHECKS=0;", false, sc);
			ejecutarComando("DROP TABLE Pacientes", true, sc);
			ejecutarComando("DROP TABLE Medicamentos", false, sc);
			res = ejecutarComando("DROP TABLE Recetas", false, sc);
			ejecutarComando("SET FOREIGN_KEY_CHECKS=1;", false, sc);
			System.out.println("Todas las tablas borradas correctamente.");
		}

		return res;
	}

}
