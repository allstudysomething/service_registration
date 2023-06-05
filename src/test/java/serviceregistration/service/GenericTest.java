package serviceregistration.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import serviceregistration.dto.GenericDTO;
import serviceregistration.mapper.GenericMapper;
import serviceregistration.model.GenericModel;
import serviceregistration.repository.GenericRepository;
import serviceregistration.service.userdetails.CustomUserDetails;

public abstract class GenericTest<E extends GenericModel, D extends GenericDTO> {
    protected GenericService<E, D> service;
    protected GenericRepository<E> repository;
    protected GenericMapper<E, D> mapper;

//    @BeforeEach
//    void init() {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(CustomUserDetails
//                .builder()
//                .username("CLIENT"),
//                        null,
//                        null);
//
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//    }

    protected abstract void getAll();
    protected abstract void getOne();
    protected abstract void create();
    protected abstract void update();
    protected abstract void delete();
//    protected abstract void restore();
//    protected abstract void getAllNotDeleted();
}
