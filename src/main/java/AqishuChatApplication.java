import initApplication.InitChatApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"initApplication","dataHandle","channelHandle"})
@SpringBootApplication
public class AqishuChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AqishuChatApplication.class, args);
    }



}
