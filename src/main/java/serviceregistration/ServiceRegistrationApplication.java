package serviceregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceRegistrationApplication	{

	// TODO
	// 1) Когда нибудь реализоватьпроверку на то чтобы нельзя было записаться клиенту в этот же день
	//    если время раньше чем текущее
	// 2) doctorslots/mySchedule попробовать прибавить к записям еще и клиента (например через кастомный обьект)
	// 3) Добавить viewDoctor profile

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistrationApplication.class, args);
	}

}



