package com.miempresa.gestion.model;



import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DetalleAlquiler implements Serializable {

    @EmbeddedId
    private DetalleId id;

    private int cantidad;

    public DetalleAlquiler() {}

    public DetalleAlquiler(DetalleId id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    @Embeddable
    public static class DetalleId implements Serializable {
        @ManyToOne
        @JoinColumn(name = "id_alquiler", nullable = false)
        private Alquiler alquiler;

        @ManyToOne
        @JoinColumn(name = "id_pelicula", nullable = false)
        private Pelicula pelicula;

        public DetalleId() {}

        public DetalleId(Alquiler alquiler, Pelicula pelicula) {
            this.alquiler = alquiler;
            this.pelicula = pelicula;
        }

        public Alquiler getAlquiler() { return alquiler; }
        public void setAlquiler(Alquiler alquiler) { this.alquiler = alquiler; }

        public Pelicula getPelicula() { return pelicula; }
        public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DetalleId)) return false;
            DetalleId that = (DetalleId) o;
            return Objects.equals(alquiler, that.alquiler) &&
                   Objects.equals(pelicula, that.pelicula);
        }

        @Override
        public int hashCode() {
            return Objects.hash(alquiler, pelicula);
        }
    }

    public DetalleId getId() { return id; }
    public void setId(DetalleId id) { this.id = id; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
