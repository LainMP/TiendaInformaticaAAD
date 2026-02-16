package UI;

import dao.FabricanteDAO;
import dao.ProductoDAO;
import entity.Fabricante;
import entity.Producto;

import java.util.List;
import java.util.Scanner;

public class ProductoUI {

    static FabricanteDAO fabricanteDAO = new FabricanteDAO();
    static ProductoDAO productoDAO = new ProductoDAO();
    static Scanner sc = new Scanner(System.in);

    //----------------------PRODUCTOS----------------------------
    private static void menuProducto(){
        StringBuilder sb  = new StringBuilder();
        sb.append("----------MENU Productos-------------\n");
        sb.append("1.Crear.\n");
        sb.append("2.Leer / Buscar.\n");
        sb.append("3.Actualizar.\n");
        sb.append("4.Eliminar.\n");
        sb.append("0.Volver.\n");
        System.out.println(sb);
    }

    private static void menuBuscarProducto(){
        StringBuilder sb = new StringBuilder();
        sb.append("----------Fabricantes-------------\n");
        sb.append("1. Ver todos. \n");
        sb.append("2. Buscar el Id por NOMBRE.\n");
        sb.append("3. Buscar productos menores de \"X\" precio.\n");
        sb.append("0. Volver.\n");
        System.out.println(sb);
    }

    //----------------------NAVEGACION PRODUCTOS----------------------------
    public static void productOptions(){
        int opcion;
        do {
            menuProducto();
            opcion = Integer.parseInt(sc.nextLine());
            switch(opcion){
                case 1 -> crearProducto();
                case 2 -> buscarProdOptions();
                case 3 -> actualizarProd();
                case 4 -> eliminarProd();
                case 0 -> System.out.println("Volviendo al menu principal...");
                default -> System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 0);
    }

    private static void buscarProdOptions() {
        int opcion;
        do {
            menuBuscarProducto();
            opcion = Integer.parseInt(sc.nextLine());
            switch(opcion){
                case 1 -> mostrarProds();
                case 2 -> prodByName();
                case 3 -> filtrarProds();
                default -> System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 0);
    }

    //----------------------FUNCIONES DE PRODUCTOS----------------------------
    /**
     * REGISTRAR PRODUCTO
     * */
    private static void crearProducto() {
        System.out.println("Introduce el nombre del Producto: ");
        String nombre = sc.nextLine().trim().toLowerCase();
        System.out.println("Introduce el precio del Producto: ");
        double precio = Double.parseDouble(sc.nextLine());
        System.out.println("Introduce el fabricante del Producto (Si no exsiste se creará): ");
        String fabName = sc.nextLine().trim().toLowerCase();
        if (nombre.isEmpty()) {
            System.out.println("Nombre del Producto no puede estar vacio.");
            return;
        }
        if (precio < 0) {
            System.out.println("Precio del Producto negativo.");
            return;
        }
        if (fabName.isEmpty()) {
            System.out.println("Fabricante no puede estar vacio.");
            return;
        }
        try {
            Producto producto = new Producto();
            List<Fabricante> fab = fabricanteDAO.searchByName(fabName);
            if (fab.isEmpty()) { // Aquí estaba comprobando si fab era null, List devuelve listas vacias si no encuentra con que llenarlas.
                Fabricante newFab = new Fabricante(fabName);
                fabricanteDAO.guardar(newFab);
                System.out.println("Fabricante creado correctamente.");
                producto.setFabricante(newFab);
            } else { //Faltaba el else para asignar el fabricante que ya existe.
                Fabricante fabricante = fab.get(0);
                producto.setFabricante(fabricante);
            }
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            productoDAO.guardar(producto);
            System.out.println("Producto creado con exito");
        } catch (Exception e) {
            System.out.println("Error al intentar crear producto.");
        }
    }

    /**
     * MODIFICAR NOMBRE O PRECIO DE PRODUCTO
     * */
    private static void actualizarProd() {

        List<Producto> listaProds = productoDAO.showAll();
        if (listaProds.isEmpty()) {
            System.out.println("No hay productos para actualizar.");
            return;
        }

        System.out.println("\n================= SELECCIONE EL PRODUCTO A MODIFICAR ======================");
        System.out.printf("%-5s | %-30s | %-10s | %-15s\n", "ID", "NOMBRE", "PRECIO", "FABRICANTE");
        System.out.println("---------------------------------------------------------------------------");

        for (Producto p : listaProds) {
            Fabricante f = p.getFabricante();
            System.out.printf("%-5d | %-30s | %-10.2f€ | %-15s\n",
                    p.getCod(),
                    p.getNombre(),
                    p.getPrecio(),
                    f.getNombre());
        }
        System.out.println("===========================================================================");
        System.out.println("Introduce el id del Producto a actualizar: ");
        int id = Integer.parseInt(sc.nextLine());
        Producto producto = productoDAO.findById(id);
        if (producto == null) {
            System.out.println("Producto no encontrado");
            return;
        }
        System.out.println("Introduce el nuevo nombre del Producto a actualizar (Pulsa intro para omitir): ");
        String newName = sc.nextLine().trim().toLowerCase();
        System.out.println("Introduce el nuevo precio del Producto a actualizar (Pulsa intro para omitir): ");
        String newPrecio = sc.nextLine();
        if (!newName.isEmpty()) {
            producto.setNombre(newName);
        }
        if (!newPrecio.isEmpty()) {
            producto.setPrecio(Double.parseDouble(newPrecio));
        }
        try {
            int exito = productoDAO.updateProd(producto);
            if(exito == 1) {
                System.out.println("Producto actualizado correctamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al intentar actualizar producto.");
        }
    }

    /**
     * ELIMINAR PRODUCTO
     * */
    private static void eliminarProd() {
        System.out.println("Introduce el id del Producto a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            Producto prod = productoDAO.findById(id);
            if (prod == null) {
                System.out.println("Producto no encontrado");
                return;
            }
            System.out.println("¿Seguro que desea eliminar el producto " + prod.getNombre() + " ?");
            System.out.println("(S/N)");
            String confirmar = sc.nextLine().trim().toLowerCase();

            if (!confirmar.equals("s")) {
                System.out.println("Operacion cancelada.");
                return;
            }
            if(productoDAO.deleteProd(id) == 1)  {
                System.out.println("Producto eliminado correctamente.");
            } else {
                System.out.println("Error al intentar eliminar producto.");
            }
        }  catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * MOSTRAR PRODUCTOS
     * */
    private static void mostrarProds() {
        try {
            List<Producto> listaProds = productoDAO.showAll();

            if (listaProds.isEmpty()) {
                System.out.println("No hay productos registrados en la base de datos.");
                return;
            }

            System.out.println("\n======================= LISTA COMPLETA DE PRODUCTOS =======================");
            System.out.printf("%-5s | %-30s | %-10s | %-15s\n", "ID", "NOMBRE", "PRECIO", "FABRICANTE");
            System.out.println("---------------------------------------------------------------------------");
            for (Producto p : listaProds) {
                Fabricante f = p.getFabricante();
                System.out.printf("%-5d | %-30s | %-10.2f€ | %-15s\n",
                        p.getCod(),
                        p.getNombre(),
                        p.getPrecio(),
                        f.getNombre());
            }
            System.out.println("===========================================================================");
            System.out.println("Total: " + listaProds.size() + " productos listados.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * MOSTRAR ID PRODUCTOS POR NOMBRE
     * */

    private static void prodByName() {
        System.out.println("Introduce el nombre del Producto: ");
        String nombre = sc.nextLine().trim().toLowerCase();
        if (nombre.isEmpty()) {
            System.out.println("El nombre del Producto no puede estar vacio.");
            return;
        }
        try {
            List<Producto> listaProds = productoDAO.searchByName(nombre);
            if (!listaProds.isEmpty()) {
                System.out.println("\nRESULTADOS PARA EL PRODUCTO: " + nombre.toUpperCase());
                System.out.println("===============================================================");
                System.out.printf("%-5s | %-30s | %-10s | %-15s\n", "ID", "NOMBRE", "PRECIO", "FABRICANTE");
                System.out.println("---------------------------------------------------------------");

                for (Producto p : listaProds) {
                    System.out.printf("%-5d | %-30s | %-10.2f€ | %-15s\n",
                            p.getCod(),
                            p.getNombre(),
                            p.getPrecio(),
                            p.getFabricante().getNombre());
                }
                System.out.println("===============================================================");
            } else {
                System.out.println("No se encontró ningún producto con el nombre: " + nombre);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * FILTRAR POR MENORES A "X" PRECIO
     **/
    private static void filtrarProds() {
        System.out.println("Introduce el precio maximo del Producto: ");
        String precioMax = sc.nextLine().trim();
        if (precioMax.isEmpty()) {
            System.out.println("El precio maximo no puede estar vacio.");
            return;
        }
        double max = 0;
        try {
            max = Double.parseDouble(precioMax);
        } catch (Exception ex) {
            System.out.println("Error al establecer el filtro del precio.");
            return;
        }
        try {
            List<Producto> listaProds = productoDAO.findByMax(max);
            if (listaProds.isEmpty()) {
                System.out.println("No se han encontrado productos por de bajo de " + max + " €");
                return;
            }
            // Formateo de salida con títulos de campos
            System.out.println("\n===============================================================");
            System.out.printf("%-5s | %-30s | %-10s | %-15s\n", "ID", "NOMBRE", "PRECIO", "FABRICANTE");
            System.out.println("---------------------------------------------------------------");

            for (Producto p : listaProds) {
                System.out.printf("%-5d | %-30s | %-10.2f | %-15s\n",
                        p.getCod(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getFabricante().getNombre());
            }
            System.out.println("===============================================================");
            System.out.println("Total: " + listaProds.size() + " productos encontrados.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
