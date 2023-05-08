package serviceregistration.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import serviceregistration.dto.DoctorDTO;
import serviceregistration.model.Doctor;
import serviceregistration.model.DoctorSlot;
import serviceregistration.repository.DoctorSlotRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class DoctorMapper
        extends GenericMapper<Doctor, DoctorDTO> {

    private final DoctorSlotRepository doctorSlotRepository;

    public DoctorMapper(ModelMapper modelMapper,
                        DoctorSlotRepository doctorSlotRepository) {
        super(Doctor.class, DoctorDTO.class, modelMapper);
        this.doctorSlotRepository = doctorSlotRepository;
    }

    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Doctor.class, DoctorDTO.class)
                .addMappings(m -> m.skip(DoctorDTO::setDoctorSlotsId)).setPostConverter(toDTOConverter());
        modelMapper.createTypeMap(DoctorDTO.class, Doctor.class)
                .addMappings(m -> m.skip(Doctor::setDoctorSlots)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(DoctorDTO source, Doctor destination) {
        if (!Objects.isNull(source.getDoctorSlotsId())) {
            destination.setDoctorSlots(doctorSlotRepository.findAllById(source.getDoctorSlotsId()));
        } else {
            destination.setDoctorSlots(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Doctor source, DoctorDTO destination) {
        destination.setDoctorSlotsId(getIds(source));
    }

    @Override
    protected List<Long> getIds(Doctor entity) {
        if (Objects.isNull(entity)) {
            return Collections.emptyList();
        } else {
            List<Long> list = new ArrayList<>();
            for (DoctorSlot doctorSlot : entity.getDoctorSlots()) {
                list.add(doctorSlot.getId());
            }
            return list;
        }
    }
}
