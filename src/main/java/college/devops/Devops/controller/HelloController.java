package college.devops.Devops.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import college.devops.Devops.entity.User;
import college.devops.Devops.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// @RestController
// public class HelloController {

//     @GetMapping("/")
//     public String helloWorld() {
//         return "Hello, World from Spring Initializr!";
//     }
// }


@Controller
public class HelloController {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }
    @Autowired
    private UserRepository userRepository;
    @PostMapping(
        "add-user"
    )
    public String addUser(User user,Model model,RedirectAttributes redirectAttributes){
        userRepository.save(user);
        System.out.println("Received User: " + user);
        redirectAttributes.addFlashAttribute("successMessage", "User added successfully!");
        return "redirect:/";
    }
    @GetMapping("get_user")
    public String listUsers(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

}
