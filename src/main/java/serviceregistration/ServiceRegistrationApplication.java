package serviceregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistrationApplication.class, args);
	}

}

// TODO
// 1) Когда нибудь реализоватьпроверку на то чтобы нельзя было записаться клиенту в этот же день
//    если время раньше чем текущее
// 2) doctorslots/mySchedule попробовать прибавить к записям еще и клиента (например через кастомный обьект)

