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
                case 2 -> productOptions();
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
        sb.append("0. Volver.\n");
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
        sb.append("3. Buscar productos menores de \"X\" precio.\n");
        sb.append("0. Volver.\n");
        System.out.println(sb);
    }

    //----------------------NAVEGACION PRODUCTOS----------------------------
    private static void productOptions(){
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
            if(fab.size()>0){
                System.out.println(fab.size() +  " Fabricantes asociados para el producto: " + name);
                for (Fabricante fabricante : fab) {
                    System.out.println(fabricante.getCod() + " - " + fabricante.getNombre());
                }
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
        System.out.println("----------Lista de Productos----------");
        for (Producto producto : listaProds) {
            System.out.println(producto.getCod() + " " + producto.getNombre() + " " + producto.getPrecio());
        }
        System.out.println("Introduce el id del Producto a actualizar: ");
        int id = Integer.parseInt(sc.nextLine());
        Producto producto = productoDAO.findById(id);
        if (producto == null) {
            System.out.println("Producto no encontrado");
            return;
        }
        System.out.println("Introduce el nuevo nombre del Producto a actualizar (Pulsa intro para omitir): ");
        String newName = sc.nextLine();
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
                Fabricante f = producto.getFabricante();
                System.out.println("- " + producto.getCod() + " - Nombre: " + producto.getNombre() + " - " + producto.getPrecio() + " - " + f.getNombre());
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
            if  (listaProds != null) {
                for (Producto producto : listaProds) {
                    System.out.println(producto.getCod() + " - " + producto.getNombre() + " - " + producto.getPrecio());
                }
            } else {
                System.out.println("No existe el producto .");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * FILTRAR POR MENORES A "X" PRECIO
     **/
    private static void filtrarProds() {
        System.out.println("Accediendo a productos. Porfavor Espere...");
        List<Producto> listaProds = productoDAO.showAll();
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
        listaProds.sort((p1, p2) -> Double.compare(p1.getPrecio(), p2.getPrecio()));

        int contador = 0;
        for (Producto producto : listaProds) {
            if (producto.getPrecio() <= max) {
                Fabricante f = producto.getFabricante();
                System.out.println("- " + producto.getCod() + " - " + producto.getNombre() + " - " + producto.getPrecio() + "€ - " + f.getNombre());
                contador++;
            }
        }
        System.out.println(contador + " Productos por debajo de " + precioMax + " euros.");
    }

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

