package serviceregistration.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import serviceregistration.dto.DoctorSlotDTO;
import serviceregistration.model.DoctorSlot;
import serviceregistration.model.GenericModel;
import serviceregistration.repository.CabinetRepository;
import serviceregistration.repository.DoctorRepository;
import serviceregistration.repository.RegistrationRepository;
import serviceregistration.repository.SlotRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DoctorSlotMapper  extends GenericMapper<DoctorSlot, DoctorSlotDTO> {
    private final DoctorRepository doctorRepository;
    private final SlotRepository slotRepository;
    private final CabinetRepository cabinetRepository;
    private final RegistrationRepository registrationRepository;
    protected DoctorSlotMapper(ModelMapper modelMapper, DoctorRepository doctorRepository, SlotRepository slotRepository, CabinetRepository cabinetRepository, RegistrationRepository registrationRepository) {
        super(DoctorSlot.class, DoctorSlotDTO.class, modelMapper);
        this.doctorRepository = doctorRepository;
        this.slotRepository = slotRepository;
        this.cabinetRepository = cabinetRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(DoctorSlot.class, DoctorSlotDTO.class)
                .addMappings(m -> m.skip(DoctorSlotDTO::setDoctorId))
                .addMappings(m -> m.skip(DoctorSlotDTO::setSlotId))
                .addMappings(m -> m.skip(DoctorSlotDTO::setCabinetId))
                .addMappings(m -> m.skip(DoctorSlotDTO::setRegistrationsId))
                .setPostConverter(toDTOConverter());
        modelMapper.createTypeMap(DoctorSlotDTO.class, DoctorSlot.class)
                .addMappings(m -> m.skip(DoctorSlot::setDoctor))
                .addMappings(m -> m.skip(DoctorSlot::setSlot))
                .addMappings(m -> m.skip(DoctorSlot::setCabinet))
                .addMappings(m -> m.skip(DoctorSlot::setRegistrations))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(DoctorSlotDTO source, DoctorSlot destination) {
        destination.setDoctor(doctorRepository.findById(source.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found by Id in mapper")));
        destination.setSlot(slotRepository.findById(source.getSlotId())
                .orElseThrow(() -> new NotFoundException("Slot not found by Id in mapper")));
        destination.setCabinet(cabinetRepository.findById(source.getCabinetId())
                .orElseThrow(() -> new NotFoundException("SCabinet not found by Id in mapper")));
        destination.setRegistrations(registrationRepository.findAllById(source.getRegistrationsId()));
    }

    @Override
    protected void mapSpecificFields(DoctorSlot source, DoctorSlotDTO destination) {
        destination.setDoctorId(source.getDoctor().getId());
        destination.setSlotId(source.getSlot().getId());
        destination.setCabinetId(source.getCabinet().getId());
        destination.setRegistrationsId(getIds(source));
    }

    @Override
    protected List<Long> getIds(DoctorSlot entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getRegistrations())
                ? Collections.emptyList()
                : entity.getRegistrations().stream().map(GenericModel::getId).collect(Collectors.toList());
    }

}
