package principal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase principal encargada de procesar los archivos de datos generados
 * y construir los reportes clasificados de ventas y productos.
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public class Main {

    static class Producto {
        int id;
        String nombre;
        long precio;
        int cantidadVendida;

        public Producto(int id, String nombre, long precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.cantidadVendida = 0;
        }
    }

    static class Vendedor {
        String tipoDoc;
        long numeroDoc;
        String nombres;
        String apellidos;
        long totalRecaudado;

        public Vendedor(String tipoDoc, long numeroDoc, String nombres, String apellidos) {
            this.tipoDoc = tipoDoc;
            this.numeroDoc = numeroDoc;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.totalRecaudado = 0;
        }
    }

    public static void main(String[] args) {
        try {
            Map<Integer, Producto> mapaProductos = cargarProductos("productos.txt");
            Map<Long, Vendedor> mapaVendedores = cargarVendedores("vendedores.txt");

            procesarArchivosVentas(mapaProductos, mapaVendedores);

            generarReporteVendedores(mapaVendedores, "reporte_vendedores.csv");
            generarReporteProductos(mapaProductos, "reporte_productos.csv");

            System.out.println("Proceso de generacion de reportes finalizado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error durante el procesamiento de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map<Integer, Producto> cargarProductos(String rutaArchivo) throws IOException {
        Map<Integer, Producto> productos = new HashMap<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return productos;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    int id = Integer.parseInt(partes[0].trim());
                    String nombre = partes[1].trim();
                    long precio = Long.parseLong(partes[2].trim());
                    productos.put(id, new Producto(id, nombre, precio));
                }
            }
        }
        return productos;
    }

    private static Map<Long, Vendedor> cargarVendedores(String rutaArchivo) throws IOException {
        Map<Long, Vendedor> vendedores = new HashMap<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return vendedores;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    String tipoDoc = partes[0].trim();
                    long numeroDoc = Long.parseLong(partes[1].trim());
                    String nombres = partes[2].trim();
                    String apellidos = partes[3].trim();
                    vendedores.put(numeroDoc, new Vendedor(tipoDoc, numeroDoc, nombres, apellidos));
                }
            }
        }
        return vendedores;
    }

    private static void procesarArchivosVentas(Map<Integer, Producto> productos, Map<Long, Vendedor> vendedores) {
        File carpetaActual = new File(".");
        File[] archivos = carpetaActual.listFiles((dir, name) -> name.startsWith("ventas_") && name.endsWith(".txt"));

        if (archivos == null) return;

        for (File archivo : archivos) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String lineaCabecera = reader.readLine();
                if (lineaCabecera == null) continue;

                String[] partesCabecera = lineaCabecera.split(";");
                if (partesCabecera.length < 2) continue;

                long idVendedor = Long.parseLong(partesCabecera[1].trim());
                Vendedor vendedor = vendedores.get(idVendedor);

                String lineaVenta;
                while ((lineaVenta = reader.readLine()) != null) {
                    if (lineaVenta.trim().isEmpty()) continue;
                    String[] partesVenta = lineaVenta.split(";");
                    if (partesVenta.length >= 2) {
                        int idProducto = Integer.parseInt(partesVenta[0].trim());
                        int cantidad = Integer.parseInt(partesVenta[1].trim());

                        Producto prod = productos.get(idProducto);
                        if (prod != null) {
                            prod.cantidadVendida += cantidad;
                            if (vendedor != null) {
                                vendedor.totalRecaudado += (cantidad * prod.precio);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error procesando el archivo: " + archivo.getName());
            }
        }
    }

    private static void generarReporteVendedores(Map<Long, Vendedor> vendedores, String nombreSalida) throws IOException {
        List<Vendedor> lista = new ArrayList<>(vendedores.values());
        lista.sort((v1, v2) -> Long.compare(v2.totalRecaudado, v1.totalRecaudado));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreSalida))) {
            for (Vendedor v : lista) {
                String linea = v.nombres + " " + v.apellidos + ";" + v.totalRecaudado;
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    private static void generarReporteProductos(Map<Integer, Producto> productos, String nombreSalida) throws IOException {
        List<Producto> lista = new ArrayList<>(productos.values());
        lista.sort((p1, p2) -> Integer.compare(p2.cantidadVendida, p1.cantidadVendida));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreSalida))) {
            for (Producto p : lista) {
                String linea = p.nombre + ";" + p.precio + ";" + p.cantidadVendida;
                writer.write(linea);
                writer.newLine();
            }
        }
    }
}