package gt.umg.repository;

import gt.umg.model.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {
    
    public List<Cliente> findByNombre(String nombre) {
        return find("nombre like ?1", "%" + nombre + "%").list();
    }
    
    public Cliente findByIdCliente(Long idCliente) {
        return find("idCliente", idCliente).firstResult();
    }
}