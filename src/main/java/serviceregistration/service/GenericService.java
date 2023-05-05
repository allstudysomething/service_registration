package serviceregistration.service;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import serviceregistration.dto.GenericDTO;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.GenericModel;
import serviceregistration.repository.GenericRepository;

import java.util.List;

@Service
public abstract class GenericService<E extends GenericModel, D extends GenericDTO> {

    protected final GenericRepository<E> repository;
    protected final GenericMapper<E, D> mapper;

    public GenericService(GenericRepository<E> repository,
                          GenericMapper<E, D> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public D getOne(final Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Нет данных с id " + id)));
    }

    public D create(D newObj) {
        return mapper.toDTO(repository.save(mapper.toEntity(newObj)));
    }

    public D update(D updObj) {
        return create(updObj);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    public List<D> listAll() {
        return mapper.toDTOs(repository.findAll());
    }
}