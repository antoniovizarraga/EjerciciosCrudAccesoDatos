package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import crud.Metodos;

public class Principal {

	public static void main(String[] args) {

	}

	public static void startMenu() {
		Scanner sc = new Scanner(System.in);
		int choice = -1;

		String tabla = "";

		String nomTabla = "";

		String condicion = "";

		ResultSet resultadosConsulta = null;

		while (choice != 7) { // Opción para salir
			printMenu();
			System.out.print("Seleccione una opción: ");

			try {
				choice = Integer.parseInt(sc.nextLine());

				switch (choice) {
				case 1:
					System.out.println("¿Cuál tabla quieres listar?");
					System.out.println("Pacientes.\nMedicamentos.\nRecetas.");
					tabla = sc.nextLine();
					nomTabla = tabla.toLowerCase();
					System.out.println("¿Quieres listar la tabla " + tabla
							+ "con una condición? De ser así, indique la condición sin poner punto y coma al final.");
					System.out.println(
							"Si quiere saber todos los datos de la tabla, deje el texto vacío y pulse la tecla enter.");
					condicion = sc.nextLine();
					resultadosConsulta = Metodos.listarTablas(tabla, condicion);

					switch (nomTabla) {

					case "pacientes", "paciente":
						while (resultadosConsulta.next()) {
							System.out.println("Id Paciente: " + resultadosConsulta.getInt(1));
							System.out.println("Nombre: " + resultadosConsulta.getString(2));
							System.out.println("Apellidos: " + resultadosConsulta.getString(3));
							System.out.println("NSS: " + resultadosConsulta.getString(4));
						}
						break;
						
					case "medicamento", "medicamentos":
						while (resultadosConsulta.next()) {
							System.out.println("Id Medicamento: " + resultadosConsulta.getInt(1));
							System.out.println("Composición: " + resultadosConsulta.getString(2));
						}
						break;
					case "receta", "recetas":
						while (resultadosConsulta.next()) {
							System.out.println("Id receta: " + resultadosConsulta.getInt(1));
							System.out.println("Id Paciente: " + resultadosConsulta.getInt(2));
							System.out.println("Id Medicamento: " + resultadosConsulta.getInt(3));
							System.out.println("Fecha Fin: " + resultadosConsulta.getDate(4));
						}
						break;

					}

					
				case 2:
					addTable();
					break;
				case 3:
					deleteTable();
					break;
				case 4:
					editTable();
					break;
				case 5:
					manageTableData();
					break;
				case 6:

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

		sc.close();
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
