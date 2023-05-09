package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.dto.RegistrationDTO;
import serviceregistration.model.Registration;
import serviceregistration.service.RegistrationService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/registrations")
@Tag(name = "Зарегистрированные записи TESTED CRUD",
        description = "Контроллер для работы со всеми когда-либо зарегистрированными записями к врачам")
public class RegistrationController
        extends GenericController<Registration, RegistrationDTO> {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService, RegistrationService registrationService1) {
        super(registrationService);
        this.registrationService = registrationService1;
    }

//    @Operation(description = "Обновить запись", method = "update")
//    @RequestMapping(value = "/update", method = RequestMethod.PUT,
//            produces = MediaType.APPLICATION_JSON_VALUE,
//            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationDTO> update(@RequestBody RegistrationDTO updEntity,
                                    @RequestParam(value = "id") Long id) {
        updEntity.setCreatedWhen(LocalDateTime.now());
        updEntity.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.update(updEntity));
    }

}
