package serviceregistration.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serviceregistration.model.Role;
import serviceregistration.repository.RoleRepository;

@Hidden
@RestController
@RequestMapping("/roles")
@Tag(name = "Roles ( EXPERIMENTAL ) TESTED", description = "*** EXPERIMENTAL *** Контроллер for work with Roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Operation(description = "Получить запись по ID", method = "getOneById")
    @RequestMapping(value = "/getOneById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOneById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.findById(id));
    }

    @Operation(description = "Получить все записи", method = "getAll")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.findAll());
    }

    @Operation(description = "Создать запись", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody Role newEntity) {
//        newEntity.setsetCreatedWhen(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(roleRepository.save(newEntity));
    }

    @Operation(description = "Обновить запись", method = "update")
    @RequestMapping(value = "/update", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@RequestBody Role updEntity,
                                    @RequestParam(value = "id") Long id) {
//        updEntity.setCreatedWhen(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(roleRepository.save(updEntity));
    }

    @Operation(description = "Удалить запись", method = "delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "id") Long id) {
        roleRepository.deleteById(id);
    }

}
