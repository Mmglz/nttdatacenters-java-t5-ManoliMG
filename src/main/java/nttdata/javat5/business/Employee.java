package nttdata.javat5.business;

/**
 * Clase Empleado
 * 
 * @author manoli
 *
 */
public class Employee {

	/** Id del empleado */
	private int id;

	/** Nombre del empleado */
	private String name;

	/** Categoría del empleado */
	private String rank;

	/** Contador para generar el id */
	private static int counter = 1;

	/** Constante para el nombre de la empresa */
	private static final String COMPANY_NAME = "NttData";

	/** Constante para el número máximo de empleados */
	private static final int MAXIMUM_EMPLOYEES = 5;

	
	/**
	 * Constructor por defecto de la clase Employee
	 */
	public Employee() {

		// El id se aumenta así automaticamente.
		this.id = counter;
		counter++;
	}
	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the category
	 */
	public String getRank() {
		return rank;
	}


	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @return the companyName
	 */
	public static String getCompanyName() {
		return COMPANY_NAME;
	}


	/**
	 * @return the maximumEmployees
	 */
	public static int getMaximumEmployees() {
		return MAXIMUM_EMPLOYEES;
	}


	/**
	 * Método toString
	 */
	@Override
	public String toString() {
		return "Empleado: " + id + " " + name + " " + rank + " " + COMPANY_NAME;
	}
}