package progWeb;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import model.Universe;




@SpringBootApplication
public class SpringLauncher {
	public static void main(String[] args) {
		SpringApplication.run(SpringLauncher.class, args);
		Universe.creation();
		System.out.println("Version 5.2 démarrée");
	}
}
