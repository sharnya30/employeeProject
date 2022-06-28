package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //display list of employees

    @GetMapping("/")
    public String viewHomePage( Model model){
         return findPaginated(1, "firstName", "asc", model);
    }

    @GetMapping("/showNewEmployeeForm")
    public String shownewEmployeeForm(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee){
        //save employee to database

        employeeService.saveEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value="id") long id, Model model) {
        // get employee from the service

        Employee employee = employeeService.getEmployeebyId(id);

        // set employee as a model attribute to pre-populate the form
         model.addAttribute("employee", employee);
         return "update_employee";
    }


    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value="id") long id){

        //call delete employee method

        this.employeeService.deleteEmployeebyId(id);

        return "redirect:/";
    }

    //page/1?sortField=name&sortOrder=asc
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value="pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortOrder") String sortOrder, Model model) {

        int pageSize = 6;

        Page<Employee> cpage = employeeService.findPaginated(pageNo, pageSize, sortField, sortOrder);
        List<Employee> listEmployees = cpage.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", cpage.getTotalPages());
        model.addAttribute("totalItems", cpage.getNumberOfElements());
        model.addAttribute("listEmployees", listEmployees);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("reverseSortOrder", sortOrder.equals("asc") ? "desc" : "asc");

        return "index";

    }
}
