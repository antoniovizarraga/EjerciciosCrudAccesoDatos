package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.Scanner;

import crud.Metodos;

public class Principal {

	public static void main(String[] args) {
		
		
		startMenu();
		
	}

	public static void startMenu() {
		
		int choice = -1;

		String tabla = "";

		String nomTabla = "";

		String condicion = "";
		
		String columna = "";
		
		String datos = "";
		
		boolean state = false;

		ResultSet resultadosConsulta = null;
		
		int idPaciente = 0;
		
		String nombre = "";
		
		String apellidos = "";
		
		String nss = "";
		
		int idReceta = 0;
		
		int idMedicamento = 0;
		
		String fechaFin = "";
		
		String composicion = "";

		while (choice != 7) { // Opción para salir
			printMenu();
			System.out.print("Seleccione una opción: ");

			try {
				choice = Integer.parseInt(Metodos.sc.nextLine());

				switch (choice) {
				case 1:
					System.out.println("¿Cuál tabla quieres listar?");
					System.out.println("Pacientes.\nMedicamentos.\nRecetas.");
					tabla = Metodos.sc.nextLine();
					nomTabla = tabla.toLowerCase();
					System.out.println("¿Quieres listar la tabla " + tabla
							+ " con una condición? De ser así, indique la condición sin poner punto y coma al final.");
					System.out.println(
							"Si quiere saber todos los datos de la tabla, deje el texto vacío y pulse la tecla enter.");
					condicion = Metodos.sc.nextLine();
					
					resultadosConsulta = Metodos.listarTablas(tabla, condicion);
					
					if(resultadosConsulta != null) {
						switch (nomTabla) {

						case "pacientes", "paciente":
							try {
								while (resultadosConsulta.next()) {
									System.out.println("Id Paciente: " + resultadosConsulta.getInt(1));
									System.out.println("Nombre: " + resultadosConsulta.getString(2));
									System.out.println("Apellidos: " + resultadosConsulta.getString(3));
									System.out.println("NSS: " + resultadosConsulta.getString(4));
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;

						case "medicamento", "medicamentos":
							try {
								while (resultadosConsulta.next()) {
									System.out.println("Id Medicamento: " + resultadosConsulta.getInt(1));
									System.out.println("Composición: " + resultadosConsulta.getString(2));
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						case "receta", "recetas":
							try {
								while (resultadosConsulta.next()) {
									System.out.println("Id receta: " + resultadosConsulta.getInt(1));
									System.out.println("Id Paciente: " + resultadosConsulta.getInt(2));
									System.out.println("Id Medicamento: " + resultadosConsulta.getInt(3));
									System.out.println("Fecha Fin: " + resultadosConsulta.getDate(4));
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;

						}
					}
					

					
					
					break;

				case 2:
					System.out.println(
							"¿Qué tabla quieres crear? Si quieres crear todas, pulsa Enter sin escribir nada.");
					System.out.println("Pacientes.\nMedicamentos.\nRecetas.");
					tabla = Metodos.sc.nextLine();

					if (tabla != null && !tabla.equals("")) {
						nomTabla = tabla.toLowerCase();

						switch (nomTabla) {
						case "pacientes", "paciente":
							
							
							
							Metodos.crearTablas("Pacientes");
							System.out.println("Tabla pacientes creada.");
							break;

						case "medicamento", "medicamentos":
							Metodos.crearTablas("Medicamentos");
							System.out.println("Tabla medicamentos creada.");
							break;
						case "receta", "recetas":
							if (Metodos.cuantasTablas() < 2) {
								System.out.println(
										"No se puede crear la tabla: \"Receta\" porque depende de otras tablas. Crea las otras tablas.");
							} else {
								System.out.println(Metodos.crearTablas("Receta"));
							}
							break;
						}
					}
					
					if(tabla != null && tabla.equals("")) {
						Metodos.crearTablas("Pacientes");
						Metodos.crearTablas("Medicamentos");
						Metodos.crearTablas("Recetas");
						System.out.println("Todas las tablas creadas.");
					}
					
					

					break;
				case 3:
					System.out.println("Indique el nombre de la tabla a eliminar. Si quiere borrar todas las tablas, pulse Enter.");
					tabla = Metodos.sc.nextLine();
					

					if (tabla != null) {

						if(!tabla.equals("")) {
							nomTabla = tabla.toLowerCase();
							
							switch (nomTabla) {
							
							case "paciente", "pacientes":
								state = Metodos.eliminarTablas(tabla);
								break;
								
							case "medicamento", "medicamentos":
								state = Metodos.eliminarTablas(tabla);
								break;
								
							case "receta", "recetas":
								state = Metodos.eliminarTablas(tabla);
								break;
							
							}
						} else {
							state = Metodos.eliminarTablas(tabla);
						}
						
						
						
						

					}

					break;
				case 4:
					System.out.println("¿Qué tabla quieres modificar?");
					
					tabla = Metodos.sc.nextLine();
					
					
					if(tabla != null && !tabla.equals("")) {
						System.out.println("¿Deseas establecer un filtro por el que editar la tabla? Si no, pulse Enter.");
						System.out.println("Escriba la condición de la siguiente forma:\nEjemplo: \"id=4\".");
						condicion = Metodos.sc.nextLine();
						
						if(condicion != null && !condicion.equals("")) {
							System.out.println("Introduzca la columna en la que quiera establecer el valor: ");
							columna = Metodos.sc.nextLine();
							
							System.out.println("Introduzca el dato o valor que quiera introducir en la columna: ");
							datos = Metodos.sc.nextLine();
							
							if(datos != null && !datos.equals("")) {
								System.out.println(Metodos.modificarDatosTabla(tabla, columna, datos, condicion));
							}
						}
						
						
						
					}
					
					
					break;
				case 5:
					System.out.println("¿Sobre qué tabla quieres insertar datos?");
					tabla = Metodos.sc.nextLine();
					
					if(tabla != null && !tabla.equals("")) {
						nomTabla = tabla.toLowerCase();
						
						System.out.println("Introduzca todos los datos que quiera insertar en la tabla: " + tabla);
						
						switch(nomTabla) {
						
						case "pacientes", "paciente":
							
							System.out.println("Nombre: ");
							nombre = Metodos.sc.nextLine();
							
							System.out.println("Apellidos: ");
							apellidos = Metodos.sc.nextLine();
							
							System.out.println("NSS: ");
							nss = Metodos.sc.nextLine();
							
							
							datos = nombre + "\n" + apellidos + "\n" + nss;
							
							System.out.println(Metodos.insertarDatos(tabla, datos));	
						
							break;
							
						case "medicamentos", "medicamento":
							
							
							System.out.println("Composición: ");
							composicion = "\"";
							composicion += Metodos.sc.nextLine();
							
							composicion += "\"";
							
							datos = composicion;
							
							System.out.println(Metodos.insertarDatos(tabla, datos));	
						
							break;
							
						case "receta", "recetas":
							System.out.println("Id Paciente: ");
							idPaciente = Metodos.sc.nextInt();
							
							Metodos.sc.nextLine();
							
							System.out.println("Id Medicamento: ");
							idMedicamento = Metodos.sc.nextInt();
							
							Metodos.sc.nextLine();
							
							System.out.println("Fecha Fin: ");
							System.out.println("El formato de la fecha es:");
							System.out.println("Ejemplo: 2024-04-15");
							fechaFin = Metodos.sc.nextLine();
							
							datos = Integer.toString(idPaciente);
							datos += "\n" + Integer.toString(idMedicamento) + "\n" + fechaFin;
							
							System.out.println(Metodos.insertarDatos(tabla, datos));	
						
							break;
						
						}
					}
					
					
					
					
					
					
					break;
				case 6:
					System.out.println("Indique la tabla sobre la que quiera borrar sus datos: ");
					
					tabla = Metodos.sc.nextLine();
					
					
					
					if(tabla != null && !tabla.equals("")) {
						
						nomTabla = tabla.toLowerCase();
						
						switch(nomTabla) {
						
						case "paciente", "pacientes":
							tabla = "Pacientes";
							break;
						case "medicamento", "medicamentos":
							tabla = "Medicamentos";
							break;
						case "receta", "recetas":
							tabla = "Receta";
							break;
						
						}
						
						System.out.println("Indique la condición para borrar datos de la tabla. Si quiere borrar todos los datos de la tabla, pulse Enter.");
						System.out.println("Escriba la condición siguiendo el siguiente formato de ejemplo: \"id=4\".");
						condicion = Metodos.sc.nextLine();
						
						state = Metodos.borrarDatos(tabla, condicion);
						
						if(state) {
							System.out.println("Datos borrados correctamente");
						} else {
							System.out.println("Los datos no pudieron borrarse.");
						}
						
					}
					
					
					
					break;
				case 7:
					System.out.println("Saliendo del programa...");
					
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija un número entre 1 y 6.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Entrada no válida. Por favor, introduzca un número.");
			}
		}

		Metodos.sc.close();
	}

	private static void printMenu() {
		System.out.println("\n=== Menú de Base de Datos ===");
		System.out.println("1. Listar tablas");
		System.out.println("2. Agregar tabla");
		System.out.println("3. Eliminar tabla");
		System.out.println("4. Editar tabla");
		System.out.println("5. Agregar datos en tablas");
		System.out.println("6. Eliminar datos en tablas");
		System.out.println("7. Salir");
	}

}
