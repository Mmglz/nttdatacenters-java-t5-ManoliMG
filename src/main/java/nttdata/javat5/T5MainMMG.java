package nttdata.javat5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nttdata.javat5.business.ManagementServiceImpl;

/**
 * Clase principal.
 * 
 * @author manoli
 *
 */
public class T5MainMMG {

	/** LOGGER */
	private static final Logger LOG = LoggerFactory.getLogger(T5MainMMG.class);

	/**
	 * Método principal que invoca a los métodos de la clase ManagementServiceImpl.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		LOG.info("TRAZA DE INICIO");

		ManagementServiceImpl m = new ManagementServiceImpl();

		// Añade empleados al mapa.
		m.addNewEmployee("Paco", "Programador junior");
		m.addNewEmployee("Pepa", "Analista");
		m.addNewEmployee("3", "Analista");
		m.addNewEmployee("", "Programador junior");
		m.addNewEmployee("Daniel", "");
		m.addNewEmployee("", "");
		m.addNewEmployee("Maria", "Jefe de proyecto");
		m.addNewEmployee("Daniel", "Experto tecnológico");
		m.addNewEmployee("Luisa", "Programador senior");
		m.addNewEmployee("Pedro", "Programador senior");

		// Imprime todos los empleados en una hoja excel.
		m.printAllEmployees();

		// Imprime el número total de empleados.
		m.printEmployeesTotalNum();

		// Elimina empleados del mapa.
		m.deleteEmployee(1);

		m.deleteEmployee(6);

		// Busca empleados por su id en el mapa.
		m.searchEmployee(4);

		m.searchEmployee(6);

		// Imprime el número total de empleados.
		m.printEmployeesTotalNum();

		LOG.info("TRAZA FIN");
	}
}
