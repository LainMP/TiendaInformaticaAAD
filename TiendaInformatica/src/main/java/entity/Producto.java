package entity;


import jakarta.persistence.*;


@Entity
@Table (name = "PRODUCTO")
public class Producto {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "COD", unique = true, nullable = false)
    private int cod;

    @Column (name = "NOMBRE", nullable = false)
    private String nombre;

    @Column (name = "PRECIO", nullable = false)
    private double precio;

    @ManyToOne
    @JoinColumn (name = "COD_FAB")
    private Fabricante fabricante;

    public Producto() {}

    public Producto(String nombre, double precio, Fabricante fabricante) {
        this.nombre = nombre;
        this.precio = precio;
        this.fabricante = fabricante;
    }

    public Fabricante getFabricante() {
        return fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
