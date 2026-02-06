package entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "FABRICANTE")
public class Fabricante {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "COD", unique = true, nullable = false)
    private int cod;

    @Column (name = "NOMBRE", unique = true, nullable = false)
    private String nombre;
    @OneToMany (mappedBy = "fabricante", cascade = CascadeType.ALL)
    private List<Producto> listaProductos;

    public Fabricante() {}

    public Fabricante(String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
