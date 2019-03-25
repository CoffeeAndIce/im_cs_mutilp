package initApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(value=2)
@Component
public class afterInitApplication implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        new InitChatApplication().run();
    }
}
