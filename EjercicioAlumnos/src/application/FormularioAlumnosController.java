package application;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Alumno;
import modelo.Carrera;
import modelo.CentroEstudio;
import modelo.Conexion;

public class FormularioAlumnosController implements Initializable{
	//Columnas
	@FXML private TableColumn<Alumno,String> clmnNombre;
	@FXML private TableColumn<Alumno,String> clmnApellido;
	@FXML private TableColumn<Alumno,Number> clmnEdad;
	@FXML private TableColumn<Alumno,String> clmnGenero;
	@FXML private TableColumn<Alumno,Date> clmnFechaIngreso;
	@FXML private TableColumn<Alumno,CentroEstudio> clmnCentroEstudio;
	@FXML private TableColumn<Alumno,Carrera> clmnCarrera;

	//Componentes GUI
	@FXML private TextField txtCodigo;
	@FXML private TextField txtNombre;
	@FXML private TextField txtApellido;
	@FXML private TextField txtEdad;
	@FXML private RadioButton rbtFemenino;
	@FXML private RadioButton rbtMasculino;
	@FXML private DatePicker dtpkrFecha;
	@FXML private Button btnGuardar;
	@FXML private Button btnEliminar;
	@FXML private Button btnActualizar;

	@FXML private ComboBox<Carrera> cmbCarrera;
	@FXML private ComboBox<CentroEstudio> cmbCentroEstudio;
	@FXML private TableView<Alumno> tblViewAlumnos;

	//Colecciones
	private ObservableList<Carrera> listaCarreras;
	private ObservableList<CentroEstudio> listaCentrosEstudios;
	private ObservableList<Alumno> listaAlumnos;

	private Conexion conexion;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		conexion = new Conexion();
		conexion.establecerConexion();

		//Inicializar listas
		listaCarreras = FXCollections.observableArrayList();
		listaCentrosEstudios = FXCollections.observableArrayList();
		listaAlumnos = FXCollections.observableArrayList();

		//Llenar listas
		Carrera.llenarInformacion(conexion.getConnection(), listaCarreras);
		CentroEstudio.llenarInformacion(conexion.getConnection(), listaCentrosEstudios);
		Alumno.llenarInformacionAlumnos(conexion.getConnection(), listaAlumnos);

		//Enlazar listas con ComboBox y TableView
		cmbCarrera.setItems(listaCarreras);
		cmbCentroEstudio.setItems(listaCentrosEstudios);
		tblViewAlumnos.setItems(listaAlumnos);

		//Enlazar columnas con atributos
		clmnNombre.setCellValueFactory(new PropertyValueFactory<Alumno,String>("nombre"));
		clmnApellido.setCellValueFactory(new PropertyValueFactory<Alumno,String>("apellido"));
		clmnEdad.setCellValueFactory(new PropertyValueFactory<Alumno,Number>("edad"));
		clmnGenero.setCellValueFactory(new PropertyValueFactory<Alumno,String>("genero"));
		clmnFechaIngreso.setCellValueFactory(new PropertyValueFactory<Alumno,Date>("fechaIngreso"));
		clmnCentroEstudio.setCellValueFactory(new PropertyValueFactory<Alumno,CentroEstudio>("centroEstudio"));
		clmnCarrera.setCellValueFactory(new PropertyValueFactory<Alumno,Carrera>("carrera"));
		gestionarEventos();
		conexion.cerrarConexion();
	}

	public void gestionarEventos(){
		tblViewAlumnos.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<Alumno>() {
					@Override
					public void changed(ObservableValue<? extends Alumno> arg0,
							Alumno valorAnterior, Alumno valorSeleccionado) {
							if (valorSeleccionado!=null){
								txtCodigo.setText(String.valueOf(valorSeleccionado.getCodigoAlumno()));
								txtNombre.setText(valorSeleccionado.getNombre());
								txtApellido.setText(valorSeleccionado.getApellido());
								txtEdad.setText(String.valueOf(valorSeleccionado.getEdad()));
								if (valorSeleccionado.getGenero().equals("F")){
									rbtFemenino.setSelected(true);
									rbtMasculino.setSelected(false);
								}else if (valorSeleccionado.getGenero().equals("M")){
									rbtFemenino.setSelected(false);
									rbtMasculino.setSelected(true);
								}
								dtpkrFecha.setValue(valorSeleccionado.getFechaIngreso().toLocalDate());
								cmbCarrera.setValue(valorSeleccionado.getCarrera());
								cmbCentroEstudio.setValue(valorSeleccionado.getCentroEstudio());

								btnGuardar.setDisable(true);
								btnEliminar.setDisable(false);
								btnActualizar.setDisable(false);
							}
					}

				}
		);
	}

	@FXML
	public void guardarRegistro(){
		//Crear una nueva instancia del tipo Alumno
		Alumno a = new Alumno(0,
					txtNombre.getText(),
					txtApellido.getText(),
					Integer.valueOf(txtEdad.getText()),
					rbtFemenino.isSelected()?"F":"M",//Condicion?ValorVerdadero:ValorFalso
					Date.valueOf(dtpkrFecha.getValue()),
					cmbCentroEstudio.getSelectionModel().getSelectedItem(),
					cmbCarrera.getSelectionModel().getSelectedItem());
		//Llamar al metodo guardarRegistro de la clase Alumno
		conexion.establecerConexion();
		int resultado = a.guardarRegistro(conexion.getConnection());
		conexion.cerrarConexion();

		if (resultado == 1){
			listaAlumnos.add(a);
			//JDK 8u>40
			Alert mensaje = new Alert(AlertType.INFORMATION);
			mensaje.setTitle("Registro agregado");
			mensaje.setContentText("El registro ha sido agregado exitosamente");
			mensaje.setHeaderText("Resultado:");
			mensaje.show();
		}
	}

	@FXML
	public void actualizarRegistro(){
		Alumno a = new Alumno(
				Integer.valueOf(txtCodigo.getText()),
				txtNombre.getText(),
				txtApellido.getText(),
				Integer.valueOf(txtEdad.getText()),
				rbtFemenino.isSelected()?"F":"M",//Condicion?ValorVerdadero:ValorFalso
				Date.valueOf(dtpkrFecha.getValue()),
				cmbCentroEstudio.getSelectionModel().getSelectedItem(),
				cmbCarrera.getSelectionModel().getSelectedItem());
		conexion.establecerConexion();
		int resultado = a.actualizarRegistro(conexion.getConnection());
		conexion.cerrarConexion();

		if (resultado == 1){
			listaAlumnos.set(tblViewAlumnos.getSelectionModel().getSelectedIndex(),a);
			//JDK 8u>40
			Alert mensaje = new Alert(AlertType.INFORMATION);
			mensaje.setTitle("Registro actualizado");
			mensaje.setContentText("El registro ha sido actualizado exitosamente");
			mensaje.setHeaderText("Resultado:");
			mensaje.show();
		}
	}

	@FXML
	public void eliminarRegistro(){
		conexion.establecerConexion();
		int resultado = tblViewAlumnos.getSelectionModel().getSelectedItem().eliminarRegistro(conexion.getConnection());
		conexion.cerrarConexion();

		if (resultado == 1){
			listaAlumnos.remove(tblViewAlumnos.getSelectionModel().getSelectedIndex());
			//JDK 8u>40
			Alert mensaje = new Alert(AlertType.INFORMATION);
			mensaje.setTitle("Registro eliminado");
			mensaje.setContentText("El registro ha sido eliminado exitosamente");
			mensaje.setHeaderText("Resultado:");
			mensaje.show();
		}
	}

	@FXML
	public void limpiarComponentes(){
		txtCodigo.setText(null);
		txtNombre.setText(null);
		txtApellido.setText(null);
		txtEdad.setText(null);
		rbtFemenino.setSelected(false);
		rbtMasculino.setSelected(false);
		dtpkrFecha.setValue(null);
		cmbCarrera.setValue(null);
		cmbCentroEstudio.setValue(null);

		btnGuardar.setDisable(false);
		btnEliminar.setDisable(true);
		btnActualizar.setDisable(true);
	}
}
