//package serviceregistration.mapper;
//
//import jakarta.annotation.PostConstruct;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Component;
//import serviceregistration.dto.ClientDTO;
//import serviceregistration.dto.RoleDTO;
//import serviceregistration.model.Client;
//import serviceregistration.model.Registration;
//import serviceregistration.model.Role;
//import serviceregistration.repository.RegistrationRepository;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//@Component
//public class RoleMapper
//        extends GenericMapper<Role, RoleDTO> {
//
//    public RoleMapper(ModelMapper modelMapper) {
//        super(Role.class, RoleDTO.class, modelMapper);
//    }
//
//    @PostConstruct
//    @Override
//    protected void setupMapper() {
////        modelMapper.createTypeMap(Client.class, ClientDTO.class)
////                .addMappings(m -> m.skip(ClientDTO::setRegistrationIDs)).setPostConverter(toEntityConverter());
////        modelMapper.createTypeMap(ClientDTO.class, Client.class)
////                .addMappings(m -> m.skip(Client::setRegistrations)).setPostConverter(toDTOConverter());
//    }
//
//    @Override
//    protected void mapSpecificFields(RoleDTO src, Role dst) {
////        if (!Objects.isNull(src.getRegistrationIDs())) {
////            dst.setRegistrations(registrationRepository.findAllById(src.getRegistrationIDs()));
////        } else {
////            dst.setRegistrations(Collections.emptyList());
////        }
//    }
//
//    @Override
//    protected void mapSpecificFields(Role src, RoleDTO dst) {
////        dst.setRegistrationIDs(getIds(src));
//    }
//
//    @Override
//    protected List<Long> getIds(Role entity) {
//        throw new RuntimeException("is it not used here");
////        if (Objects.isNull(entity)) {
////            return Collections.emptyList();
////        } else {
////            List<Long> list = new ArrayList<>();
////            for (Registration registration : entity.getRegistrations()) {
////                list.add(registration.getId());
////            }
////            return list;
////        }
//    }
//
//}
