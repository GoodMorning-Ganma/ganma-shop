package com.ganmashop.controller;

import com.ganmashop.entity.User;
import com.ganmashop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ganma")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String showAccountPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            User user = userService.findUserById(loggedInUser.getId());
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return "account"; // Render the "account.html" template
    }

    @PostMapping("/account/edit")
    public String updateAccount(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            user.setId(loggedInUser.getId()); // Ensure the correct ID is used
            userService.updateUser(user);
            session.setAttribute("loggedInUser", userService.findUserById(loggedInUser.getId())); // Update session

            // Add a success message as a flash attribute
            redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
        }
        return "redirect:/ganma/account"; // Redirect back to the account page
    }


}
