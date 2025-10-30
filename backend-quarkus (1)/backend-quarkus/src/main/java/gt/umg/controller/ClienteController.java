package gt.umg.controller;

import gt.umg.model.Cliente;
import gt.umg.repository.ClienteRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteController {

    @Inject
    ClienteRepository clienteRepository;

    // GET - Obtener todos los clientes
    @GET
    public Response getAllClientes() {
        try {
            List<Cliente> clientes = clienteRepository.listAll();
            return Response.ok(clientes).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener clientes: " + e.getMessage())
                    .build();
        }
    }

    // GET - Obtener cliente por ID
    @GET
    @Path("/{id}")
    public Response getCliente(@PathParam("id") Long id) {
        try {
            Cliente cliente = clienteRepository.findByIdCliente(id);
            if (cliente == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(cliente).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener cliente: " + e.getMessage())
                    .build();
        }
    }

    // POST - Crear nuevo cliente
    @POST
    @Transactional
    public Response createCliente(Cliente cliente) {
        try {
            // Validaciones básicas
            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El nombre es obligatorio")
                        .build();
            }
            
            clienteRepository.persist(cliente);
            return Response.status(Response.Status.CREATED).entity(cliente).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear cliente: " + e.getMessage())
                    .build();
        }
    }

    // PUT - Actualizar cliente existente
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateCliente(@PathParam("id") Long id, Cliente clienteActualizado) {
        try {
            Cliente cliente = clienteRepository.findByIdCliente(id);
            if (cliente == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setDireccion(clienteActualizado.getDireccion());
            cliente.setTelefono(clienteActualizado.getTelefono());
            cliente.setCorreo(clienteActualizado.getCorreo());

            return Response.ok(cliente).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar cliente: " + e.getMessage())
                    .build();
        }
    }

    // DELETE - Eliminar cliente
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteCliente(@PathParam("id") Long id) {
        try {
            Cliente cliente = clienteRepository.findByIdCliente(id);
            if (cliente == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            clienteRepository.delete(cliente);
            return Response.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar cliente: " + e.getMessage())
                    .build();
        }
    }
}