package com.ganmashop.controller;
import com.ganmashop.entity.User;
import com.ganmashop.service.UserService;
import com.ganmashop.utils.AdminViewHelper;
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
        AdminViewHelper.putWelcomeNameIfLoggedIn(session, model);

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
    public String showEditForm(@PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/auth/login";
        }
        User user = userService.findUserById(id);
        if (user == null || user.getUserType() == null
                || !"admin".equalsIgnoreCase(user.getUserType().trim())) {
            return "redirect:/admin/users";
        }
        user.setPassword("");
        model.addAttribute("user", user);
        return "admin/updateUser";
    }

    @PostMapping("/users/updateUser/{id}")
    public String updateAccount(@PathVariable String id, @ModelAttribute User user, HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }
        User existing = userService.findUserById(id);
        if (existing == null || existing.getUserType() == null
                || !"admin".equalsIgnoreCase(existing.getUserType().trim())) {
            redirectAttributes.addFlashAttribute("errorMessage", "只能编辑管理员账号。");
            return "redirect:/admin/users";
        }
        user.setId(id);
        userService.updateUser(user);
        if (id.equals(loggedInUser.getId())) {
            session.setAttribute("loggedInUser", userService.findUserById(id));
        }
        redirectAttributes.addFlashAttribute("message", "用户更新成功!");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/deleteUser/{id}")
    public String deleteUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
        userService.deleteUserById(id);
        redirectAttributes.addFlashAttribute("message", "用户删除成功！");
        return "redirect:/admin/users";
    }

}
