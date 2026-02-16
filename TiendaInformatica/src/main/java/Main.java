import UI.FabricanteUI;
import UI.ProductoUI;
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

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){

        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        int opcion;

        do {
            menuPrincipal();
            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch(opcion){
                    case 1 -> FabricanteUI.fabricanteOptions();
                    case 2 -> ProductoUI.productOptions();
                    case 0 -> System.out.println("Cerrando el programa...");
                    default -> System.out.println("Opcion no valida.\n");
                }
            } catch (Exception ex) {
                System.out.println("Por favor introduzca una opcion valida.\n");
                opcion = -1;
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

}
