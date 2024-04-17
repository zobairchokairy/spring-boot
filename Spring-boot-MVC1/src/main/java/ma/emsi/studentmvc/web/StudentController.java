package ma.emsi.studentmvc.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.emsi.studentmvc.Repository.StudentRepository;
import ma.emsi.studentmvc.entites.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class StudentController {
    private StudentRepository studentRepository;

    @GetMapping("/index")
    public String students(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "name", defaultValue = "") String name) { // @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size
        Page<Student> pageStudents = studentRepository.findByNomContains(name, PageRequest.of(page, size));  // in the parameter of findAll(PageRequest.of(page,size));
        model.addAttribute("students", pageStudents.getContent());
        model.addAttribute("pages", new int[pageStudents.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        return "index";
    }

    @GetMapping("/delete")
    public String delete(Long id, String name, int page) {
        studentRepository.deleteById(id);
        return "redirect:/index?page=" + page + "&name=" + name;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/students")
    @ResponseBody
    public List<Student> listStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/formStudents")
    public String formStudents(Model model) {
        model.addAttribute("student", new Student());
        return "formStudents";
    }

    @PostMapping(path = "/save")
    public String save(Model model, @Valid Student student, BindingResult bindingResult,
                       @RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "0") int page) {
        if (bindingResult.hasErrors()) return "formStudents";
        studentRepository.save(student);
        return "redirect:/index?name=" + name + "&page=" + page;
    }

    @GetMapping("/editStudent")
    public String editStudent(Model model, Long id, String name, int page) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) throw new RuntimeException("Student not found");
        model.addAttribute("student", student);
        model.addAttribute("name", name);
        model.addAttribute("page", page);
        return "editStudent";
    }
}
