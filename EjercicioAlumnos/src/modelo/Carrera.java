package modelo;

/* Java Bean
* Clase: Carrera  */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Carrera{
	private IntegerProperty codigoCarrera;
	private StringProperty nombreCarrera;
	private IntegerProperty cantidadAsignaturas;

	public Carrera(Integer codigoCarrera, String nombreCarrera, Integer cantidadAsignaturas){
		this.codigoCarrera = new SimpleIntegerProperty(codigoCarrera);
		this.nombreCarrera = new SimpleStringProperty(nombreCarrera);
		this.cantidadAsignaturas = new SimpleIntegerProperty(cantidadAsignaturas);
	}

	public Integer getCodigoCarrera(){
		return codigoCarrera.get();
	}

	public void setCodigoCarrera(Integer codigoCarrera){
		this.codigoCarrera = new SimpleIntegerProperty(codigoCarrera);
	}

	public String getNombreCarrera(){
		return nombreCarrera.get();
	}

	public void setNombreCarrera(String nombreCarrera){
		this.nombreCarrera = new SimpleStringProperty(nombreCarrera);
	}

	public Integer getCantidadAsignaturas(){
		return cantidadAsignaturas.get();
	}

	public void setCantidadAsignaturas(Integer cantidadAsignaturas){
		this.cantidadAsignaturas = new SimpleIntegerProperty(cantidadAsignaturas);
	}

	public IntegerProperty codigoCarreraProperty(){
		return codigoCarrera;
	}

	public StringProperty nombreCarreraProperty(){
		return nombreCarrera;
	}

	public IntegerProperty cantidadAsignaturasProperty(){
		return cantidadAsignaturas;
	}

	public static void llenarInformacion(Connection connection, ObservableList<Carrera> lista){
		try {
			Statement statement = connection.createStatement();
			ResultSet resultado = statement.executeQuery(
					"SELECT codigo_carrera, "
					+ "nombre_carrera, "
					+ "cantidad_asignaturas "
					+ "FROM tbl_carreras"
			);
			while (resultado.next()){
				lista.add(
						new Carrera(
								resultado.getInt("codigo_carrera"),
								resultado.getString("nombre_carrera"),
								resultado.getInt("cantidad_asignaturas")
						)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString(){
		return nombreCarrera.get() + " ("+cantidadAsignaturas.get()+")";
	}
}