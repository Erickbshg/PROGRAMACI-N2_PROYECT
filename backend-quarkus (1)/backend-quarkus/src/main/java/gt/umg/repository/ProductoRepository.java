package gt.umg.repository;

import gt.umg.model.Producto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ProductoRepository implements PanacheRepository<Producto> {
    
    public List<Producto> findByNombre(String nombre) {
        return find("nombre like ?1", "%" + nombre + "%").list();
    }
}