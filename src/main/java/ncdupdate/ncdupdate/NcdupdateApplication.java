package ncdupdate.ncdupdate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class NcdupdateApplication {

	public static void main(String[] args) {
		SpringApplication.run(NcdupdateApplication.class, args);
	}
	//--- Section to auto-load your browser to start app --
	@EventListener(ApplicationReadyEvent.class)
	public void openBrowser() {
		String url = "http://localhost:8080"; // Replace with your application's URL
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Desktop is not supported. Please open the URL manually: " + url);
		}
	}
//--

}
