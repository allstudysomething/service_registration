package serviceregistration.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Registration;
import serviceregistration.repository.ClientRepository;
import serviceregistration.repository.DoctorSlotRepository;

import java.util.List;

@Component
public class RegistrationMapper extends GenericMapper<Registration, RegistrationDTO> {
    private final ClientRepository clientRepository;
    private final DoctorSlotRepository doctorSlotRepository;

    protected RegistrationMapper(ModelMapper mapper,
                                 ClientRepository clientRepository,
                                 DoctorSlotRepository doctorSlotRepository) {
        super(Registration.class, RegistrationDTO.class, mapper);
        this.clientRepository = clientRepository;
        this.doctorSlotRepository = doctorSlotRepository;
    }

    @Override
//    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Registration.class, RegistrationDTO.class)
                .addMappings(m -> m.skip(RegistrationDTO::setClientId))
                .addMappings(m -> m.skip(RegistrationDTO::setDoctorSlotId))
                .setPostConverter(toDTOConverter());

        modelMapper.createTypeMap(RegistrationDTO.class, Registration.class)
                .addMappings(m -> m.skip(Registration::setClient))
                .addMappings(m -> m.skip(Registration::setDoctorSlot))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(Registration source, RegistrationDTO destination) {
        destination.setClientId(source.getClient().getId());
        destination.setDoctorSlotId(source.getDoctorSlot().getId());
    }

    @Override
    protected void mapSpecificFields(RegistrationDTO source, Registration destination) {
        destination.setClient(clientRepository.findById(source.getClientId())
                .orElseThrow(() -> new NotFoundException("Клиента с не найдено")));
        destination.setDoctorSlot(doctorSlotRepository.findById(source.getDoctorSlotId())
                .orElseThrow(() -> new NotFoundException("Доктор Слот не найден")));
    }

    @Override
    protected List<Long> getIds(Registration entity) {
        throw new UnsupportedOperationException("Метод недоступен");
    }

}
