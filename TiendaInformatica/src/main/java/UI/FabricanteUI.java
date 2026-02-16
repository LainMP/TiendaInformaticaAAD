package UI;

import dao.FabricanteDAO;
import dao.ProductoDAO;
import entity.Fabricante;
import entity.Producto;

import java.util.List;
import java.util.Scanner;

public class FabricanteUI {
    static FabricanteDAO fabricanteDAO = new FabricanteDAO();
    static ProductoDAO productoDAO = new ProductoDAO();
    static Scanner sc = new Scanner(System.in);

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

    //----------------------NAVEGACION FABRICANTES----------------------------
    public static void fabricanteOptions() {
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
            if (listaFabs.isEmpty()) {
                System.out.println("No hay fabricantes registrados.");
                return;
            }

            System.out.println("\n========================================");
            System.out.printf("%-5s | %-30s\n", "ID", "NOMBRE DEL FABRICANTE");
            System.out.println("----------------------------------------");

            for (Fabricante f : listaFabs) {
                System.out.printf("%-5d | %-30s\n",
                        f.getCod(),
                        f.getNombre());
            }
            System.out.println("========================================");
            System.out.println("Total: " + listaFabs.size() + " fabricantes.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    /**
     * MOSTRAR TODOS LOS PRODUCTOS DE UN FABRICANTE
     * */
    private static  void prodsByFab() {
        System.out.println("Introduce el nombre del Fabricante para listar productos: ");
        String name = sc.nextLine().trim().toLowerCase();
        if (name.isEmpty()) {
            System.out.println("Nombre del Fabricante no puede estar vacio.");
            return;
        }
        try {
            List<Producto> listaProds = fabricanteDAO.getProductsByFab(name);
            if (listaProds.isEmpty()) {
                System.out.println("No se han encontrado productos para el Fabricante: " + name);
                return;
            }

            System.out.println("\nPRODUCTOS DEL FABRICANTE: " + name.toUpperCase());
            System.out.println("==========================================================");
            System.out.printf("%-5s | %-35s | %-10s\n", "ID", "NOMBRE DEL PRODUCTO", "PRECIO");
            System.out.println("----------------------------------------------------------");

            for (Producto prod : listaProds) {
                System.out.printf("%-5d | %-35s | %-10.2f€\n",
                        prod.getCod(),
                        prod.getNombre(),
                        prod.getPrecio());
            }
            System.out.println("==========================================================");
            System.out.println("Total: " + listaProds.size() + " productos.");
        } catch (Exception ex) {
            System.out.println("No se han encontrado productos.");
        }
    }

    /**
     * MOSTRAR EL FABRICANTE DE UN PRODUCTO
     * */
    private static void fabByProduct(){
        System.out.println("Introduce el nombre del Producto: ");
        String name = sc.nextLine().trim().toLowerCase();
        if (name.isEmpty()) {
            System.out.println("Nombre del Producto no puede estar vacio.");
            return;
        }
        try {
            List<Fabricante> fab = fabricanteDAO.searchByProduct(name);
            if (!fab.isEmpty()) {
                System.out.println("\nFABRICANTES QUE PRODUCEN: " + name.toUpperCase());
                System.out.println("========================================");
                System.out.printf("%-5s | %-30s\n", "ID", "NOMBRE DEL FABRICANTE");
                System.out.println("----------------------------------------");

                for (Fabricante fabricante : fab) {
                    System.out.printf("%-5d | %-30s\n",
                            fabricante.getCod(),
                            fabricante.getNombre());
                }
                System.out.println("========================================");
                System.out.println("Total: " + fab.size() + " fabricantes encontrados.");
            } else {
                System.out.println("No se han encontrado fabricantes para ese producto.");
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
        String name = sc.nextLine().trim().toLowerCase();
        if (name.isEmpty()) {
            System.out.println("Nombre del Fabricante no puede estar vacio.");
            return;
        }
        try {
            List<Fabricante> fab = fabricanteDAO.searchByName(name);
            if (!fab.isEmpty()) {
                System.out.println("\nRESULTADOS DE BÚSQUEDA: " + name.toUpperCase());
                System.out.println("========================================");
                System.out.printf("%-5s | %-30s\n", "ID", "NOMBRE DEL FABRICANTE");
                System.out.println("----------------------------------------");

                for (Fabricante fabricante : fab) {
                    System.out.printf("%-5d | %-30s\n",
                            fabricante.getCod(),
                            fabricante.getNombre());
                }
                System.out.println("========================================");
            } else {
                System.out.println("No se encontró ningún fabricante con el nombre: " + name);
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
        String nombre = sc.nextLine().trim().toLowerCase();

        if(nombre.isEmpty()){
            System.out.println("El nombre esta vacio");
            return;
        }
        try {
            Fabricante fabricante = new Fabricante();
            fabricante.setNombre(nombre);

            fabricanteDAO.guardar(fabricante);
            System.out.println("Fabricante guardado correctamente.");
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
        String nombre = sc.nextLine().trim().toLowerCase();

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
            Fabricante fab = fabricanteDAO.findById(id);

            if(fab ==  null) {
                System.out.println("No existe ningun fabricante con ese ID.");
                return;
            }

            if(!fab.getListaProductos().isEmpty()) {
                System.out.println("Este fabricante tiene productos asociados, si continua se eliminaran.");
                System.out.println("¿Desea eliminar el fabricante? (S/N)?");
                String confirmar = sc.nextLine().toLowerCase().trim();
                if(!confirmar.equals("s")) {
                    System.out.println("Operacion cancelada.");
                    return;
                }
            }

            if(fabricanteDAO.deleteFab(id) == 1) {
                System.out.println("Fabricante y productos asociados se eliminaron.");
            } else {
                System.out.println("No se ha podido eliminar fabricante.");
            }
        }  catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
