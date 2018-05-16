package ro.ubb.tjfblooddonation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.tjfblooddonation.model.BaseEntity;
import ro.ubb.tjfblooddonation.model.IdClass;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
@Transactional
public interface Repository<T extends IdClass<ID>, ID extends Serializable>
        extends JpaRepository<T, ID> {
    List<T> getAll();
    T getById(ID id);
    T add(T entity);
    T update(T entity);
    void remove(ID id);
}
