package com.miempresa.gestion;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.miempresa.gestion.model.Alquiler;
import com.miempresa.gestion.model.Alquiler.EstadoAlquiler;
import com.miempresa.gestion.model.Cliente;
import com.miempresa.gestion.model.DetalleAlquiler;
import com.miempresa.gestion.model.Pelicula;
import com.miempresa.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        System.out.println("======================================");
        System.out.println("🎬 BIENVENIDO AL SISTEMA DE ALQUILER 🎬");
        System.out.println("======================================");

        int opcion;
        do {
            System.out.println("\n📋 Menú Principal:");
            System.out.println("1. Ver clientes");
            System.out.println("2. Ver películas");
            System.out.println("3. Agregar cliente");
            System.out.println("4. Agregar película");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Eliminar película");
            System.out.println("7. Registrar alquiler");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1 -> verClientes(em, sc);
                case 2 -> verPeliculas(em, sc);
                case 3 -> agregarCliente(em, sc);
                case 4 -> agregarPelicula(em, sc);
                case 5 -> eliminarCliente(em, sc);
                case 6 -> eliminarPelicula(em, sc);
                case 7 -> registrarAlquiler(em, sc);
                case 0 -> System.out.println("👋 ¡Gracias por usar el sistema!");
                default -> System.out.println("⚠️ Opción inválida.");
            }

        } while (opcion != 0);

        em.close();
        JPAUtil.shutdown();
        sc.close();
    }

    static void verClientes(EntityManager em, Scanner sc) {
        List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        if (clientes.isEmpty()) {
            System.out.println("⚠️ No hay clientes registrados.");
        } else {
            System.out.println("\n📇 Lista de Clientes:");
            clientes.forEach(c -> System.out.println("🆔 " + c.getId_cliente() + " | " + c.getNombre() + " - " + c.getEmail()));
        }
        System.out.println("\nPresione ENTER para regresar al menú...");
        sc.nextLine();
    }

    static void verPeliculas(EntityManager em, Scanner sc) {
        List<Pelicula> peliculas = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class).getResultList();
        if (peliculas.isEmpty()) {
            System.out.println("⚠️ No hay películas registradas.");
        } else {
            System.out.println("\n🎞️ Lista de Películas:");
            peliculas.forEach(p -> System.out.println("🆔 " + p.getId_pelicula() + " | " + p.getTitulo() + " (" + p.getGenero() + ") - Stock: " + p.getStock()));
        }
        System.out.println("\nPresione ENTER para regresar al menú...");
        sc.nextLine();
    }

    static void agregarCliente(EntityManager em, Scanner sc) {
        System.out.print("Ingrese nombre del cliente: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese email del cliente: ");
        String email = sc.nextLine();

        Cliente cliente = new Cliente(nombre, email);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(cliente);
        tx.commit();
        System.out.println("✅ Cliente agregado correctamente.");
    }

    static void agregarPelicula(EntityManager em, Scanner sc) {
        System.out.print("Ingrese título de la película: ");
        String titulo = sc.nextLine();
        System.out.print("Ingrese género: ");
        String genero = sc.nextLine();
        System.out.print("Ingrese stock: ");
        int stock = sc.nextInt();
        sc.nextLine(); // limpiar buffer

        Pelicula pelicula = new Pelicula(titulo, genero, stock);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(pelicula);
        tx.commit();
        System.out.println("✅ Película agregada correctamente.");
    }

    static void eliminarCliente(EntityManager em, Scanner sc) {
        verClientes(em, sc);
        System.out.print("Ingrese ID del cliente a eliminar: ");
        long id = sc.nextLong();
        sc.nextLine(); // limpiar buffer
        Cliente cliente = em.find(Cliente.class, id);

        if (cliente != null) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.remove(cliente);
            tx.commit();
            System.out.println("✅ Cliente eliminado.");
        } else {
            System.out.println("❌ Cliente no encontrado.");
        }
    }

    static void eliminarPelicula(EntityManager em, Scanner sc) {
        verPeliculas(em, sc);
        System.out.print("Ingrese ID de la película a eliminar: ");
        long id = sc.nextLong();
        sc.nextLine(); // limpiar buffer
        Pelicula pelicula = em.find(Pelicula.class, id);

        if (pelicula != null) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.remove(pelicula);
            tx.commit();
            System.out.println("✅ Película eliminada.");
        } else {
            System.out.println("❌ Película no encontrada.");
        }
    }

    static void registrarAlquiler(EntityManager em, Scanner sc) {
        List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        List<Pelicula> peliculas = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class).getResultList();

        if (clientes.isEmpty() || peliculas.isEmpty()) {
            System.out.println("⚠️ Se requiere al menos un cliente y una película para registrar un alquiler.");
            return;
        }

        System.out.println("\nSeleccione cliente:");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println((i + 1) + ". " + clientes.get(i).getNombre() + " - " + clientes.get(i).getEmail());
        }
        int clienteIndex = sc.nextInt() - 1;
        sc.nextLine(); // limpiar buffer
        Cliente clienteSeleccionado = clientes.get(clienteIndex);

        System.out.println("Seleccione película:");
        for (int i = 0; i < peliculas.size(); i++) {
            System.out.println((i + 1) + ". " + peliculas.get(i).getTitulo() + " (" + peliculas.get(i).getGenero() + ") - Stock: " + peliculas.get(i).getStock());
        }
        int peliculaIndex = sc.nextInt() - 1;
        sc.nextLine(); // limpiar buffer
        Pelicula peliculaSeleccionada = peliculas.get(peliculaIndex);

        System.out.print("Ingrese cantidad: ");
        int cantidad = sc.nextInt();
        sc.nextLine(); // limpiar buffer

        if (cantidad <= 0 || cantidad > peliculaSeleccionada.getStock()) {
            System.out.println("❌ Cantidad no válida o excede el stock.");
            return;
        }

        double precioUnitario = 5.0;
        double total = cantidad * precioUnitario;
        System.out.println("Total a pagar: S/. " + total);
        System.out.print("¿Confirmar alquiler? (s/n): ");
        String confirmar = sc.nextLine();
        if (!confirmar.equalsIgnoreCase("s")) {
            System.out.println("❌ Alquiler cancelado.");
            return;
        }

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Alquiler alquiler = new Alquiler();
        alquiler.setCliente(clienteSeleccionado);
        alquiler.setFecha(LocalDate.now());
        alquiler.setTotal(total);
        alquiler.setEstado(EstadoAlquiler.Activo);
        em.persist(alquiler);

        DetalleAlquiler detalle = new DetalleAlquiler();
        DetalleAlquiler.DetalleId detalleId = new DetalleAlquiler.DetalleId(alquiler, peliculaSeleccionada);
        detalle.setId(detalleId);
        detalle.setCantidad(cantidad);
        em.persist(detalle);

        peliculaSeleccionada.setStock(peliculaSeleccionada.getStock() - cantidad);
        em.merge(peliculaSeleccionada);

        tx.commit();
        System.out.println("✅ Alquiler registrado correctamente.");
    }
}



