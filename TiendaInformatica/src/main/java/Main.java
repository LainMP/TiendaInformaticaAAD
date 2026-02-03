import dao.FabricanteDAO;
import dao.ProductoDAO;
import entity.Fabricante;
import entity.Producto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    static FabricanteDAO fabricanteDAO = new FabricanteDAO();
    static ProductoDAO productoDAO = new ProductoDAO();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){

        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        int opcion;

        do {
            menuPrincipal();
            opcion = Integer.parseInt(sc.nextLine());
            switch(opcion){
                case 1 -> fabricanteOptions();
                case 2 -> menuProducto();
                case 0 -> System.out.println("Cerrando el programa...");
                default -> System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 0);
    }

    //____________________________________________________________________
    //----------------------IMPRESION DE MENUS----------------------------

    private static void menuPrincipal(){
        StringBuilder sb  = new StringBuilder();
        sb.append("-----------MENU PRINCIPAL-------------\n");
        sb.append("1.Fabricante.\n");
        sb.append("2.Producto.\n");
        sb.append("0.Salir.\n");
        System.out.println(sb);
    }
    //----------------------FABRICANTES----------------------------
    private static void menuFabricante(){
        StringBuilder sb  = new StringBuilder();
        sb.append("----------MENU Fabricante-------------\n");
        sb.append("1.Crear.\n");
        sb.append("2.Leer / Buscar.\n");
        sb.append("3.Actualizar.\n");
        sb.append("4.Eliminar.\n");
        sb.append("0.Volver.\n");
        System.out.println(sb);
    }

    private static void menuBuscarFabricante(){
        StringBuilder sb = new StringBuilder();
        sb.append("----------Fabricantes-------------\n");
        sb.append("1. Ver todos. \n");
        sb.append("2. Buscar el Id por NOMBRE.\n");
        sb.append("3. Buscar por NOMBRE de PRODUCTO.\n");
        sb.append("4. Buscar por NOMBRE de FABRICANTE y obtener productos.\n");
        sb.append("0.Salir.\n");
        System.out.println(sb);
    }

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
        sb.append("3. Buscar por NOMBRE de FABRICANTE.\n");
        sb.append("4. Buscar por NOMBRE de PRODUCTO y obtener FABRICANTES.\n");
        sb.append("0.Salir.\n");
        System.out.println(sb);
    }

    //----------------------NAVEGACION PRODUCTOS----------------------------
    private void productOptions(){
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
                case 3 -> prodByFab();
                case 4 -> fabsByProd();
                default -> System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 0);
    }

    //----------------------NAVEGACION FABRICANTES----------------------------
    private static void fabricanteOptions() {
        int opcion;
        do {
            menuFabricante();
            opcion = Integer.parseInt(sc.nextLine());
            switch(opcion){
                case 1 -> crearFabricante();
                case 2 -> buscarFabOptions();
                case 3 -> actualizarFab();
                case 4 -> eliminarFab();
                case 0 -> System.out.println("Volviendo al menu principal...");
                default -> System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 0);
    }

    private static void buscarFabOptions() {
        int opcion;
        do {
            menuBuscarFabricante();
            opcion = Integer.parseInt(sc.nextLine());
            switch(opcion){
               case 1 -> mostrarFabs();
               case 2 -> fabByName();
               case 3 -> fabByProduct();
               case 4 -> prodsByFab();
               default -> System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 0);
    }

    //----------------------FUNCIONES DE FABRICANTES----------------------------
    /**
     * MOSTRAR TODOS LOS FABRICANTES
     * */
    private static void mostrarFabs() {
        System.out.println("Fabricantes registrados:");
        try {
            List<Fabricante> listaFabs = fabricanteDAO.showAllFabs();
            for (Fabricante fabricante : listaFabs) {
                System.out.println("- " + fabricante.getCod() + " - Nombre: " + fabricante.getNombre());
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    /**
     * MOSTRAR TODOS LOS PRODUCTOS DE UN FABRICANTE
     * */
    private static  void prodsByFab() {
        System.out.println("Introduce el nombre del Fabricante para listar productos: ");
        String name = sc.nextLine();
        if (name.isEmpty()) {
            System.out.println("Nombre del Fabricante no puede estar vacio.");
            return;
        }
        try {
            List<Producto> listaProds = fabricanteDAO.getProductsByFab(name);
            System.out.println("Se han encontrado " +  listaProds.size() + " productos para el Fabricante: " + name);
            for (Producto prod : listaProds) {
                System.out.println(prod.getCod() + "-" + prod.getNombre() + "-" + prod.getPrecio() + ".");
            }
        } catch (Exception ex) {
            System.out.println("No se han encontrado productos.");
        }
    }

    /**
     * MOSTRAR EL FABRICANTE DE UN PRODUCTO
     * */
    private static void fabByProduct(){
        System.out.println("Introduce el nombre del Producto: ");
        String name = sc.nextLine();
        if (name.isEmpty()) {
            System.out.println("Nombre del Producto no puede estar vacio.");
            return;
        }
        try {
            List<Fabricante> fab = fabricanteDAO.searchByProduct(name);
            Fabricante fabricante = null;
            for (Fabricante f : fab) {
                fabricante = f;
            }
            if (fabricante != null) {
                System.out.println("El fabricante es: " + fabricante.getCod() + " - " + fabricante.getNombre());
            } else  {
                System.out.println("Fabricante no encontrado");
            }
        } catch (Exception e) {
            System.out.println("Error al intentar buscar el Fabricante.");
        }
    }

    /**
     * MOSTRAR ID FABRICANTES POR NOMBRE
     * */
    private static void fabByName() {
        System.out.println("Introduce el nombre del Fabricante a buscar: ");
        String name = sc.nextLine();
        if (name.isEmpty()) {
            System.out.println("Nombre del Fabricante no puede estar vacio.");
            return;
        }
        try {
            List<Fabricante> fab = fabricanteDAO.searchByName(name);
            Fabricante fabricante = null;
            for (Fabricante f : fab) {
                fabricante = f;
            }
            if (fabricante != null) {
                System.out.println("El Id del fabricante " + name + " es: " + fabricante.getCod());
            } else  {
                System.out.println("No se encontro el Fabricante.");
            }
        } catch (Exception e) {
            System.out.println("Error al intentar buscar el nombre del Fabricante.");
        }
    }

    /**
     * REGISTRAR UN FABRICANTE
     * */
    private static void crearFabricante(){
        System.out.println("Introduce el nombre del Fabricante: ");
        String nombre = sc.nextLine().trim();

        if(nombre.isEmpty()){
            System.out.println("El nombre esta vacio");
            return;
        }
        try {
            Fabricante fabricante = new Fabricante();
             fabricante.setNombre(nombre);

            fabricanteDAO.guardar(fabricante);
        } catch (Exception e) {
            System.out.println("Error al intentar crear fabricante");
        }
    }

    /**
     * MODIFICAR EL NOMBRE DE UN FABRICANTE
     * */
    private static void actualizarFab(){
        System.out.println("Introduce el id del Fabricante a actualizar: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.println("Introduce el nuevo nombre del Fabricante: ");
        String nombre = sc.nextLine();

        try {
            if(fabricanteDAO.updateFab(id, nombre) == 1) {
                System.out.println("Fabricante actualizado correctamente.");
            } else  {
                System.out.println("No se ha podido actualizar fabricante.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    };

    /**
     * ELIMINAR UN FABRICANTE
     * */
    private static void eliminarFab(){
        System.out.println("Introduce el id del Fabricante a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            if(fabricanteDAO.deleteFab(id) == 1) {
                System.out.println("Fabricante eliminado correctamente.");
            }  else  {
                System.out.println("No se ha podido eliminar fabricante.");
            }
        }  catch (Exception ex) {
            ex.printStackTrace();
        }
    };

    //----------------------FUNCIONES DE FABRICANTES----------------------------
    /**
     * REGISTRAR PRODUCTO
     * */
    private static void crearProducto() {
        System.out.println("Introduce el nombre del Producto: ");
        String nombre = sc.nextLine().trim().toLowerCase();
        System.out.println("Introduce el precio del Producto: ");
        double precio = Double.parseDouble(sc.nextLine());
        System.out.println("Introduce el fabricante del Producto (Si no exsiste se creará): ");
        String fabName = sc.nextLine();
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
            if (fab == null) {
                Fabricante newFab = new Fabricante(fabName);
                fabricanteDAO.guardar(newFab);
                System.out.println("Fabricante creado correctamente.");
                producto.setFabricante(newFab);
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
        String nombre = "";
        double precio = 0;
        List<Producto> listaProds = productoDAO.showAll();
        System.out.println("----------Lista de Productos----------");
        for (Producto producto : listaProds) {
            System.out.println(producto.getCod() + " " + producto.getNombre() + " " + producto.getPrecio());
        }
        System.out.println("Introduce el id del Producto a actualizar: ");
        int id = Integer.parseInt(sc.nextLine());
        Producto producto = listaProds.get(id);
        System.out.println("Introduce el nuevo nombre del Producto a actualizar (Pulsa intro para omitir): ");
        String newName = sc.nextLine();
        System.out.println("Introduce el nuevo precio del Producto a actualizar (Pulsa intro para omitir): ");
        String newPrecio = sc.nextLine();
        if (!newName.isEmpty()) {
            nombre = newName;
        }
        if (!newPrecio.isEmpty()) {
            precio = Double.parseDouble(newPrecio);
        }
        try {
            productoDAO.updateProd(id, nombre, precio);
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
            if(productoDAO.deleteProd(id) == 1) {
                System.out.println("Producto eliminado correctamente.");
            }  else  {
                System.out.println("No se ha podido eliminar producto.");
            }
        }  catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * MOSTRAR PRODUCTOS
     * */
    private static void mostrarProds() {
        System.out.println("LISTA DE PRODUCTOS:");
        try {
            List<Producto> listaProds = productoDAO.showAll();
            for (Producto producto : listaProds) {
                System.out.println("- " + producto.getCod() + " - Nombre: " + producto.getNombre());
            }
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

            for (Producto producto : listaProds) {

            }
        }
    }

    //-----TODO _(HASTA AQUÍ ME CUADRA)
    /**
     * MOSTRAR PRODUCTOS DE UN FABRICANTE
     * */
    private static void prodByFab() {}

    /**
     * MOSTRAR LOS FABRICANTES DE UN PRODUCTO
     * */
    private static void fabsByProd() {}
}


/**
 * Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
 *         SessionFactory factory = new Configuration().
 *                 configure("hibernate.cfg.xml")
 *                 .buildSessionFactory();
 *
 *         Session session = factory.openSession();
 *
 *         try {
 *
 *             session.beginTransaction();
 *             List<Fabricante> listaF = session.createQuery(
 *                             "SELECT f FROM Fabricante f",
 *                             Fabricante.class)
 *                     .getResultList();
 *             for (Fabricante fabricante : listaF) {
 *                 int numP = 0;
 *
 *                 System.out.println(fabricante.getNombre());
 *                 for (Producto producto : fabricante.getListaProductos()) {
 *                     System.out.println("    -" + producto.getNombre());
 *                     numP = fabricante.getListaProductos().size();
 *                 }
 *                 System.out.println(numP + " - Productos.");
 *             }
 *
 *
 *             System.out.println("----------------------------");
 *
 *         //esto está mal.
 *             System.out.println("Fabricante con codigo 1: "
 *                     + listaF.get(0).getNombre()
 *                     + " - Productos."
 *                     + listaF.get(0).getListaProductos().size());
 *
 *         } catch (Exception e) {
 *             e.printStackTrace();
 *         } finally {
 *             session.close();
 *             factory.close();
 *         }
 * */

