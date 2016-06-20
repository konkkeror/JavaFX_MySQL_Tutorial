package modelo;

/* Java Bean
* Clase: CentroEstudio  */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class CentroEstudio{
	private IntegerProperty codigoCentro;
	private StringProperty nombreCentro;

	public CentroEstudio(Integer codigoCentro, String nombreCentro){
		this.codigoCentro = new SimpleIntegerProperty(codigoCentro);
		this.nombreCentro = new SimpleStringProperty(nombreCentro);
	}

	public Integer getCodigoCentro(){
		return codigoCentro.get();
	}

	public void setCodigoCentro(Integer codigoCentro){
		this.codigoCentro = new SimpleIntegerProperty(codigoCentro);
	}

	public String getNombreCentro(){
		return nombreCentro.get();
	}

	public void setNombreCentro(String nombreCentro){
		this.nombreCentro = new SimpleStringProperty(nombreCentro);
	}

	public IntegerProperty codigoCentroProperty(){
		return codigoCentro;
	}

	public StringProperty nombreCentroProperty(){
		return nombreCentro;
	}
	
	public static void llenarInformacion(Connection connection, ObservableList<CentroEstudio> lista){
		try {
			Statement statement = connection.createStatement();
			ResultSet resultado = statement.executeQuery(
					"SELECT codigo_centro, nombre_centro_estudio FROM tbl_centros_estudio"
			);
			while (resultado.next()){
				lista.add(
						new CentroEstudio(
								resultado.getInt("codigo_centro"),
								resultado.getString("nombre_centro_estudio")
						)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString(){
		return nombreCentro.get();
	}
}