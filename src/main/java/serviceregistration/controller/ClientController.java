package serviceregistration.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceregistration.dto.ClientDTO;
import serviceregistration.model.Client;
import serviceregistration.service.ClientService;

//@Hidden
@RestController
@RequestMapping("/clients")
@Tag(name = "Клиенты TESTED CRUD", description = "Контроллер для работы с клиентами поликлиники")
public class ClientController
        extends GenericController<Client, ClientDTO> {

    public ClientController(ClientService service) {
        super(service);
    }

}
