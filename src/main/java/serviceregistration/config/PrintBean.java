//package serviceregistration.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import serviceregistration.repository.RegistrationRepository;
//
//@Component
//public class PrintBean {
//
//    private RegistrationRepository registrationRepository;
//
//    @Autowired
//    public void setRegistrationRepository(RegistrationRepository repository) {
//        this.registrationRepository = repository;
//    }
//
//    @Bean
//    public String printCurrentDatePlus() {
//		System.out.println(registrationRepository.getCurrentDate());
//		System.out.println(registrationRepository.getCurrentYear());
//		System.out.println(registrationRepository.getCurrentMonth());
//		System.out.println(registrationRepository.getCurrentDay());
//        return new String();
//    }
//}
