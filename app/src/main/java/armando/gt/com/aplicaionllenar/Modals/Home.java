package armando.gt.com.aplicaionllenar.Modals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Home implements Parcelable {
    private String titulo;
    private String descripcion;
    private String ranking;
    private @ServerTimestamp Date timestamp;
    private String cartelera_id;
    private String id_pos;
    private String url_portada;
    private String temporadas;
    private String usuarios;

    public Home(String titulo, String descripcion, String ranking ,Date timestamp, String cartelera_id, String id_pos, String url_portada, String temporadas, String usuarios) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ranking = ranking;
        this.timestamp = timestamp;
        this.cartelera_id = cartelera_id;
        this.id_pos = id_pos;
        this.url_portada = url_portada;
        this.temporadas = temporadas;
        this.usuarios = usuarios;
    }

    public Home() {

    }

    protected Home(Parcel in) {
        titulo = in.readString();
        descripcion = in.readString();
        ranking = in.readString();
        cartelera_id = in.readString();
        id_pos = in.readString();
        url_portada = in.readString();
        temporadas = in.readString();
        usuarios = in.readString();
    }

    public static final Creator<Home> CREATOR = new Creator<Home>() {
        @Override
        public Home createFromParcel(Parcel in) {
            return new Home(in);
        }

        @Override
        public Home[] newArray(int size) {
            return new Home[size];
        }
    };

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCartelera_id() {
        return cartelera_id;
    }

    public void setCartelera_id(String cartelera_id) {
        this.cartelera_id = cartelera_id;
    }

    public String getId_pos() {
        return id_pos;
    }

    public void setId_pos(String id_pos) {
        this.id_pos = id_pos;
    }

    public String getUrl_portada() {
        return url_portada;
    }

    public void setUrl_portada(String url_portada) {
        this.url_portada = url_portada;
    }

    public String getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(String temporadas) {
        this.temporadas = temporadas;
    }

    public String getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(String usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(titulo);
        parcel.writeString(descripcion);
        parcel.writeString(ranking);
        parcel.writeString(cartelera_id);
        parcel.writeString(id_pos);
        parcel.writeString(url_portada);
        parcel.writeString(temporadas);
        parcel.writeString(usuarios);
    }
}
