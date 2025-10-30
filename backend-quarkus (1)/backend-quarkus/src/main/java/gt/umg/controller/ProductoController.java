package gt.umg.controller;

import gt.umg.model.Producto;
import gt.umg.repository.ProductoRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoController {

    @Inject
    ProductoRepository productoRepository;

    // GET - Obtener todos los productos
    @GET
    public List<Producto> getAllProductos() {
        return productoRepository.listAll();
    }

    // GET - Obtener producto por ID
    @GET
    @Path("/{id}")
    public Response getProducto(@PathParam("id") Long id) {
        Producto producto = productoRepository.findById(id);
        if (producto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(producto).build();
    }

    // POST - Crear nuevo producto
    @POST
    @Transactional
    public Response createProducto(Producto producto) {
        productoRepository.persist(producto);
        return Response.status(Response.Status.CREATED).entity(producto).build();
    }

    // PUT - Actualizar producto existente
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateProducto(@PathParam("id") Long id, Producto productoActualizado) {
        Producto producto = productoRepository.findById(id);
        if (producto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        producto.setNombre(productoActualizado.getNombre());
        producto.setPrecio(productoActualizado.getPrecio());

        productoRepository.persist(producto);
        return Response.ok(producto).build();
    }

    // DELETE - Eliminar producto
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteProducto(@PathParam("id") Long id) {
        Producto producto = productoRepository.findById(id);
        if (producto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productoRepository.delete(producto);
        return Response.noContent().build();
    }
}