package principal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Clase principal encargada de la generacion pseudoaleatoria de archivos 
 * de prueba para el sistema de ventas del proyecto.
 * 
 * @author Tu Nombre O Nombre de tu Grupo
 * @version 1.0
 */
public class GenerateInfoFiles {

    private static final String[] TIPOS_DOCUMENTO = {"CC", "CE", "NIT"};
    private static final String[] NOMBRES_VENDEDOR = {"Juan", "Maria", "Carlos", "Ana", "Luis", "Laura", "Pedro", "Sofia"};
    private static final String[] APELLIDOS_VENDEDOR = {"Gomez", "Rodriguez", "Perez", "Martinez", "Garcia", "Lopez", "Torres"};
    private static final String[] NOMBRES_PRODUCTO = {"Cuaderno", "Lapicero", "Borrador", "Regla", "Marcador", "Carpeta", "Tijeras", "Pegante"};

    public static void main(String[] args) {
        try {
            int cantidadProductos = 10;
            int cantidadVendedores = 5;

            createProductsFile(cantidadProductos);
            createSalesManInfoFile(cantidadVendedores);

            System.out.println("Proceso de generacion de archivos finalizado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error durante la generacion de archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createProductsFile(int productsCount) {
        String fileName = "productos.txt";
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 1; i <= productsCount; i++) {
                int idProducto = 100 + i;
                String nombreProducto = NOMBRES_PRODUCTO[random.nextInt(NOMBRES_PRODUCTO.length)] + " " + i;
                long precioUnitario = (random.nextInt(50) + 1) * 1000L;

                String linea = idProducto + ";" + nombreProducto + ";" + precioUnitario;
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("Archivo " + fileName + " generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al crear el archivo de productos: " + e.getMessage());
        }
    }

    public static void createSalesManInfoFile(int salesmanCount) {
        String fileName = "vendedores.txt";
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDoc = TIPOS_DOCUMENTO[random.nextInt(TIPOS_DOCUMENTO.length)];
                long numeroDoc = 100000000L + random.nextInt(900000000);
                String nombre = NOMBRES_VENDEDOR[random.nextInt(NOMBRES_VENDEDOR.length)];
                String apellido = APELLIDOS_VENDEDOR[random.nextInt(APELLIDOS_VENDEDOR.length)];

                String linea = tipoDoc + ";" + numeroDoc + ";" + nombre + ";" + apellido;
                writer.write(linea);
                writer.newLine();

                int cantidadVentasRandom = random.nextInt(10) + 1;
                String nombreCompleto = nombre + "_" + apellido;
                createSalesMenFile(cantidadVentasRandom, nombreCompleto, numeroDoc);
            }
            System.out.println("Archivo " + fileName + " generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al crear el archivo de vendedores: " + e.getMessage());
        }
    }

    public static void createSalesMenFile(int randomSalesCount, String name, long id) {
        String fileName = "ventas_" + name + "_" + id + ".txt";
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String tipoDoc = TIPOS_DOCUMENTO[random.nextInt(TIPOS_DOCUMENTO.length)];
            writer.write(tipoDoc + ";" + id);
            writer.newLine();

            for (int i = 0; i < randomSalesCount; i++) {
                int idProducto = 101 + random.nextInt(10);
                int cantidadVendida = random.nextInt(15) + 1;

                String lineaVenta = idProducto + ";" + cantidadVendida + ";";
                writer.write(lineaVenta);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al crear el archivo de ventas para " + name + ": " + e.getMessage());
        }
    }
}