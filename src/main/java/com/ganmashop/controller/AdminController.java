package com.ganmashop.controller;
import com.ganmashop.entity.User;
import com.ganmashop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author Desmondlzk
 * Date: 09/11/2024
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String showUserPage(Model model, HttpSession session){
        // 修改为获取管理员列表
        List<User> admins = userService.findAllAdmins();
        model.addAttribute("users", admins);

        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("isLoggedIn", user != null);

        return "admin/users";
    }

    @GetMapping("/addAdmin")
    public String showAddForm(Model model) {
        model.addAttribute("admin", new User());
        return "admin/addAdmin"; // 修正模板名称
    }

    // 处理添加请求（POST）
    @PostMapping("/addAdmin")
    public String addProduct(@ModelAttribute User admin, HttpSession session, RedirectAttributes redirectAttributes) {
        // 验证登录状态
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }
        try {
            userService.adminUser(admin);
            redirectAttributes.addFlashAttribute("message", "管理员添加成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "添加失败: " + e.getMessage());
            return "redirect:/admin/addAdmin";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/editUser/{id}")
    public String showEditForm(@ModelAttribute User user, Model model, HttpSession session) {
        if(user != null){
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        user = userService.findUserById(loggedInUser.getId());
        model.addAttribute("user", user);
        }

        return "admin/updateUser";
    }

    @PostMapping("/users/updateUser/{id}")
    public String updateAccount(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            user.setId(loggedInUser.getId()); // Ensure the correct ID is used
            userService.updateUser(user);
            session.setAttribute("loggedInUser", userService.findUserById(loggedInUser.getId())); // Update session

            // Add a success message as a flash attribute
            redirectAttributes.addFlashAttribute("message", "用户更新成功!");
        }
        return "redirect:/admin/users"; // Redirect back to the account page
    }

    @GetMapping("/users/deleteUser/{id}")
    public String deleteUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
        userService.deleteUserById(id);
        redirectAttributes.addFlashAttribute("message", "用户删除成功！");
        return "redirect:/admin/users";
    }

}
