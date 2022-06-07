package nttdata.javat5.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que implementa la interfaz ManagmentServiceI.
 * 
 * @author manoli
 *
 */
public class ManagementServiceImpl implements ManagementServiceI {

	/** LOGGER */
	private static final Logger LOG = LoggerFactory.getLogger(ManagementServiceImpl.class);

	/** Constantes String para Logger */
	private static final String LOG_START = "TRAZA DE INICIO";
	private static final String LOG_END = "TRAZA FIN\n";

	/** Mapa database de clave integer y de valor un objeto Empleado */
	private Map<Integer, Employee> database = new HashMap<>(Employee.getMaximumEmployees());

	/**
	 * Método para dar de alta a empleados.
	 */
	@Override
	public void addNewEmployee(String name, String rank) {

		LOG.info(LOG_START);

		// Si la base de datos ha llegado a su máximo no deja dar de alta a otro
		// empleado.
		if (database.size() == Employee.getMaximumEmployees()) {

			LOG.warn("No se pueden dar de alta más empleados porque la empresa ha llegado a su máximo.");

			// No deja dar de alta al empleado si su nombre o categoría no son correctos.
		} else if (name.isBlank() || rank.isBlank() || !onlyLetters(name)) {

			LOG.error("Nombre y/o categoria incorrectos.");

			// Da de alta a un empleado.
		} else {

			Employee e = new Employee();

			e.setName(name);

			e.setRank(rank);

			database.put(e.getId(), e);

			LOG.info("{} ha sido dado/a de alta como {}", e.getName(), e.getRank());
		}

		LOG.info(LOG_END);
	}

	/**
	 * Método que vuelca todos los empleados del sistema en un fichero XLS.
	 */
	@Override
	public void printAllEmployees() {

		LOG.info(LOG_START);

		// Genera el xlsx.
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {

			// Creación de la hoja.
			XSSFSheet sheet = workbook.createSheet("employeeInfo");

			// Estilo del título.
			CellStyle titleStyle = titleStyle(workbook);

			// Estilo de la cabecera.
			CellStyle headerIdStyle = headerIdStyle(workbook);
			CellStyle headerNameStyle = headerNameStyle(workbook);
			CellStyle headerRankStyle = headerRankStyle(workbook);
			CellStyle headerCompanyStyle = headerCompanyStyle(workbook);

			// Creación de la fila del título.
			XSSFRow titleRow = sheet.createRow(0);
			titleRow.setHeightInPoints(40);

			// Creación de la celda del título
			XSSFCell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("Empleados");
			titleCell.setCellStyle(titleStyle);

			// Ancho de la celda del título.
			sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$D$1"));

			// Creación de la fila de las cabeceras de las columnas.
			XSSFRow headerRow = sheet.createRow(1);
			headerRow.setHeightInPoints(15);

			// Creación de la celda ID.
			XSSFCell headerId = headerRow.createCell(0);
			headerId.setCellValue("ID");
			headerId.setCellStyle(headerIdStyle);

			// Creación de la celda Nombre.
			XSSFCell headerName = headerRow.createCell(1);
			headerName.setCellValue("Nombre");
			headerName.setCellStyle(headerNameStyle);

			// Creación de la celda Categoria.
			XSSFCell headerRank = headerRow.createCell(2);
			headerRank.setCellValue("Categoria");
			headerRank.setCellStyle(headerRankStyle);

			// Creación de la celda Nombre empresa.
			XSSFCell headerNameCompany = headerRow.createCell(3);
			headerNameCompany.setCellValue("Nombre empresa");
			headerNameCompany.setCellStyle(headerCompanyStyle);

			// Variable número de fila.
			int rowNumber = 2;

			// Recorre el diccionario database.
			for (Entry<Integer, Employee> entry : database.entrySet()) {

				// Ancho de las filas 2 y 3.
				sheet.setColumnWidth(2, 5000);
				sheet.setColumnWidth(3, 5000);

				// Crea la fila de los campos sumando 1 a la variable rowNumber
				XSSFRow row = sheet.createRow(rowNumber++);

				// Estilo de las celdas de id.
				CellStyle idStyle = workbook.createCellStyle();
				idStyle.setAlignment(HorizontalAlignment.CENTER);

				// Crea la celda para el id.
				XSSFCell idCell = row.createCell(0);
				idCell.setCellValue(entry.getKey());
				idCell.setCellStyle(idStyle);

				// Crea la celda para el nombre del empleado.
				row.createCell(1).setCellValue(((entry.getValue()).getName()));

				// Crea la celda para la categoría.
				row.createCell(2).setCellValue(((entry.getValue()).getRank()));

				// Crea la celda para el nombre de la empresa.
				row.createCell(3).setCellValue(Employee.getCompanyName());
			}

			// Crea un achivo en la ruta seleccionada.
			FileOutputStream file = new FileOutputStream("C:\\dev\\employeeInfo.xlsx");

			// Escribe en el archivo.
			workbook.write(file);

			// Cierra el archivo.
			file.close();

			LOG.info("Se ha creado el fichero xslx.");

		} catch (IOException e) {

			LOG.error("[ERROR]Cierre el archivo antes de ejecutar el programa.");
		}

		LOG.info(LOG_END);
	}

	/**
	 * Método para mostrar el número total de empleados.
	 */
	@Override
	public void printEmployeesTotalNum() {

		System.out.println("\nEn la empresa " + Employee.getCompanyName() + " hay en total " + database.size() + " empleados.\n");
	}

	/**
	 * Método para dar de baja a empleados.
	 * 
	 * @param id
	 */
	public void deleteEmployee(int id) {

		LOG.info(LOG_START);

		// Comprueba si el id está en el mapa y borra al empleado.
		if (database.containsKey(id)) {

			database.remove(id);

			LOG.info("El empleado con id {} ha sido dado de baja.", id);

		} else {

			LOG.warn("No hay ningún empleado con id {}.", id);
		}

		LOG.info(LOG_END);
	}

	/**
	 * Método para buscar a un empleado por su id.
	 * 
	 * @param id
	 */
	public void searchEmployee(int id) {

		// Comprueba si el id está en el mapa e imprime la información del empleado.
		if (database.containsKey(id)) {

			System.out.println("El id " + id + " corresponde a " + database.get(id).getName() + ", con categoria " + database.get(id).getRank());

		} else {

			System.out.println("No hay ningún empleado con el id " + id);
		}
	}

	/**
	 * Método para comprobar que una cadena sólo tiene letras.
	 * 
	 * @param text
	 * @return boolean
	 */
	public static boolean onlyLetters(String text) {

		// Recorre la palabra (text) letra a letra
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			// Comprueba que no está entre a y z, A y Z, ni es un espacio
			if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Método de estilo para la cabecera ID del documento xlsx.
	 * 
	 * @param workbook
	 * @return headerIdStyle
	 */
	private CellStyle headerIdStyle(XSSFWorkbook workbook) {
		CellStyle headerIdStyle = workbook.createCellStyle();

		// Estilo de la fuente.
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerIdStyle.setFont(headerFont);

		// Alineación del texto y color del fondo.
		headerIdStyle.setAlignment(HorizontalAlignment.CENTER);
		headerIdStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		headerIdStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return headerIdStyle;
	}

	/**
	 * Método de estilo para la cabecera Nombre del documento xlsx.
	 * 
	 * @param workbook
	 * @return headerNameStyle
	 */
	private CellStyle headerNameStyle(XSSFWorkbook workbook) {
		CellStyle headerNameStyle = workbook.createCellStyle();

		// Estilo de la fuente.
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerNameStyle.setFont(headerFont);

		// Alineación del texto y color del fondo.
		headerNameStyle.setAlignment(HorizontalAlignment.CENTER);
		headerNameStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
		headerNameStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return headerNameStyle;
	}

	/**
	 * Método de estilo para la cabecera Categoria del documento xlsx.
	 * 
	 * @param workbook
	 * @return headerRankStyle
	 */
	private CellStyle headerRankStyle(XSSFWorkbook workbook) {
		CellStyle headerRankStyle = workbook.createCellStyle();

		// Estilo de la fuente.
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerRankStyle.setFont(headerFont);

		// Alineación del texto y color del fondo.
		headerRankStyle.setAlignment(HorizontalAlignment.CENTER);
		headerRankStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		headerRankStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return headerRankStyle;
	}

	/**
	 * Método de estilo para la cabecera Nombre empresa del documento xlsx.
	 * 
	 * @param workbook
	 * @return headerCompanyStyle
	 */
	private CellStyle headerCompanyStyle(XSSFWorkbook workbook) {
		CellStyle headerCompanyStyle = workbook.createCellStyle();

		// Estilo de la fuente.
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerCompanyStyle.setFont(headerFont);

		// Alineación del texto y color del fondo.
		headerCompanyStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCompanyStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerCompanyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return headerCompanyStyle;
	}

	/**
	 * Método de estilo para el título del documento xlsx.
	 * 
	 * @param workbook
	 * @return titleStyle
	 */
	private CellStyle titleStyle(XSSFWorkbook workbook) {
		CellStyle titleStyle = workbook.createCellStyle();

		// Estilo de la fuente.
		Font titleFont = workbook.createFont();
		titleFont.setFontHeightInPoints((short) 18);
		titleFont.setBold(true);
		titleStyle.setFont(titleFont);

		// Alineación del texto.
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		return titleStyle;
	}
}