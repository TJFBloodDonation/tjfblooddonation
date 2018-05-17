package ro.ubb.tjfblooddonation.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ro.ubb.tjfblooddonation.exceptions.RepositoryException;
import ro.ubb.tjfblooddonation.model.IdClass;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public class RepositoryImpl<T extends IdClass<ID>, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements Repository<T, ID> {
    private final Logger log = LoggerFactory.getLogger(RepositoryImpl.class);

    public RepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public RepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }


    @Override
    public List<T> getAll() {
        log.trace("getAll() method enter ---");
        List<T> result = findAll();
        log.trace("getAll() method exit result={} ---", result);
        return result;
    }

    @Override
    public T getById(ID id) {
        log.trace("getById() method enter --- id={}", id);
        Optional<T> result = findById(id);
        log.trace("getById() method exit result={} ---", result);
        if(!result.isPresent())
            throw new RepositoryException("No entity with id = " + id);
        return result.get();
    }

    @Override
    public T add(T entity) {
        log.trace("add() method enter --- entity={}", entity);
//        if(findById(entity.getId()).isPresent()){
//            throw new RepositoryException("An entity with this Id already exists!");
//        }
        T result = save(entity);
        log.trace("add() method exit --- result={}", result);
        return result;

    }

    @Override
    public T update(T entity) {
        log.trace("update() method enter --- entity={}", entity);
//        Optional<T> e = findById(entity.getId());
//        if(!e.isPresent()){
//            throw new RepositoryException("An entity with this Id does not exist!");
//        }
        T result = save(entity);
        log.trace("update() method exit --- result={}", result);
        return result;
    }

    @Override
    public void remove(ID id) {
        log.trace("remove() method enter --- id={}", id);
        deleteById(id);
        log.trace("remove() method exit ---");
    }

}
